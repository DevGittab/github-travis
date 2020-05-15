package net.gittab.githubtravis.mybatis.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.mybatis.filter.TenantFilter;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;

/**
 * TenantSqlParserImpl.
 *
 * @author xiaohua zhou
 **/
@Slf4j
public class TenantSqlParserImpl implements TenantSqlParser {

	private TenantFilter tenantFilter;

	private String tenantColumn;

	public TenantSqlParserImpl(String tenantColumn) {
		this.tenantColumn = tenantColumn;
	}

	public TenantSqlParserImpl(TenantFilter tenantFilter, String tenantColumn) {
		this.tenantFilter = tenantFilter;
		this.tenantColumn = tenantColumn;
	}

	@Override
	public boolean doStatementFilter(String statementId) {
		if (Objects.isNull(this.tenantFilter)) {
			return true;
		}

		return this.tenantFilter.statementFilter(statementId);
	}

	public boolean doTableFilter(String tableName) {
		if (Objects.isNull(this.tenantFilter)) {
			return true;
		}

		return this.tenantFilter.tableFilter(tableName);
	}

	@Override
	public String setTenantParameter(String sql, String tenantId) {
		Statement stmt;
		try {
			stmt = CCJSqlParserUtil.parse(sql);
		}
		catch (JSQLParserException e) {
			e.printStackTrace();
			// 解析失败不进行任何处理防止业务中断
			return sql;
		}
		// insert
		if (stmt instanceof Insert) {
			processInsert((Insert) stmt, tenantId);
		}
		// update
		if (stmt instanceof Update) {
			processUpdate((Update) stmt, tenantId);
		}
		// select
		if (stmt instanceof Select) {
			Select select = (Select) stmt;
			processSelectBody(select.getSelectBody(), tenantId);
		}
		log.info("new sql : {}", stmt);
		return stmt.toString();
	}

	@Override
	public void processSelectBody(SelectBody selectBody, String tenantId) {
		if (selectBody instanceof PlainSelect) {
			processPlainSelect((PlainSelect) selectBody, false, tenantId);
		}
		else if (selectBody instanceof WithItem) {
			WithItem withItem = (WithItem) selectBody;
			if (withItem.getSelectBody() != null) {
				processSelectBody(withItem.getSelectBody(), tenantId);
			}
		}
		else {
			SetOperationList operationList = (SetOperationList) selectBody;
			if (operationList.getSelects() != null
					&& operationList.getSelects().size() > 0) {
				List<SelectBody> plainSelects = operationList.getSelects();
				for (SelectBody plainSelect : plainSelects) {
					processSelectBody(plainSelect, tenantId);
				}
			}
		}
	}

	/**
	 * process plain select.
	 * @param plainSelect plain select
	 * @param addColumn is add column, insert into select need
	 * @param tenantId tenant id
	 */
	public void processPlainSelect(PlainSelect plainSelect, boolean addColumn,
			String tenantId) {
		FromItem fromItem = plainSelect.getFromItem();
		if (fromItem instanceof Table) {
			Table fromTable = (Table) fromItem;
			if (doTableFilter(fromTable.getName())) {
				// 外层表名过滤，看子查询里面是否需要添加租户条件
				processWhereExpression(plainSelect.getWhere(), tenantId);
			}
			else {
				plainSelect.setWhere(
						buildExpression(plainSelect.getWhere(), fromTable, tenantId));
				if (addColumn) {
					plainSelect.getSelectItems().add(
							new SelectExpressionItem(new Column("'" + tenantId + "'")));
				}
			}
		}
		else {
			processFromItem(fromItem, tenantId);
		}
		List<Join> joins = plainSelect.getJoins();
		if (joins != null && joins.size() > 0) {
			for (Join join : joins) {
				processJoin(join, tenantId);
				processFromItem(join.getRightItem(), tenantId);
			}
		}
	}

	/**
	 * process join query.
	 * @param join join
	 * @param tenantId tenant id
	 */
	public void processJoin(Join join, String tenantId) {
		if (!(join.getRightItem() instanceof Table)) {
			return;
		}

		Table fromTable = (Table) join.getRightItem();
		if (doTableFilter(fromTable.getName())) {
			// 直接过滤，不需要加租户条件
			return;
		}

		join.setOnExpression(
				buildExpression(join.getOnExpression(), fromTable, tenantId));

	}

	public void processWhereExpression(Expression expression, String tenantId) {

		if (Objects.isNull(expression)) {
			return;
		}

		if (!(expression instanceof BinaryExpression)) {
			return;
		}

		// 包含子查询
		BinaryExpression binaryExpression = (BinaryExpression) expression;
		if (binaryExpression.getLeftExpression() instanceof FromItem) {
			processFromItem((FromItem) binaryExpression.getLeftExpression(), tenantId);
		}
		if (binaryExpression.getRightExpression() instanceof FromItem) {
			processFromItem((FromItem) binaryExpression.getRightExpression(), tenantId);
		}
	}

	/**
	 * process sub query.
	 * @param fromItem from item
	 * @param tenantId tenant id
	 */
	public void processFromItem(FromItem fromItem, String tenantId) {
		if (fromItem instanceof SubJoin) {
			SubJoin subJoin = (SubJoin) fromItem;
			// FIXME fix joins
			List<Join> joins = subJoin.getJoinList();
			if (!CollectionUtils.isEmpty(joins)) {
				for (Join join : joins) {
					processJoin(join, tenantId);
				}
			}
			if (subJoin.getLeft() != null) {
				processFromItem(subJoin.getLeft(), tenantId);
			}
		}
		else if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			if (subSelect.getSelectBody() != null) {
				processSelectBody(subSelect.getSelectBody(), tenantId);
			}
		}
		else if (fromItem instanceof LateralSubSelect) {
			LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
			if (Objects.isNull(lateralSubSelect.getSubSelect())) {
				return;
			}
			SubSelect subSelect = lateralSubSelect.getSubSelect();
			if (subSelect.getSelectBody() != null) {
				processSelectBody(subSelect.getSelectBody(), tenantId);
			}
		}
		// else if (fromItem instanceof ValuesList) {
		//
		// }
	}

	/**
	 * build expression.
	 * @param expression expression
	 * @param table table
	 * @param tenantId tenant id
	 * @return expression
	 */
	public Expression buildExpression(Expression expression, Table table,
			String tenantId) {

		String[] tenantIds = tenantId.split(",");

		StringBuilder tenantIdColumnName = new StringBuilder();

		Expression tenantExpression;
		// 当传入 table 时,字段前加上别名或者 table 名
		if (table != null) {
			tenantIdColumnName.append(table.getAlias() != null
					? table.getAlias().getName() : table.getName());
			tenantIdColumnName.append(".");
		}
		// append tenant column
		tenantIdColumnName.append(this.tenantColumn);
		// 生成字段名
		Column tenantColumn = new Column(tenantIdColumnName.toString());

		if (tenantIds.length == 1) {
			EqualsTo equalsTo = new EqualsTo();
			tenantExpression = equalsTo;
			equalsTo.setLeftExpression(tenantColumn);
			equalsTo.setRightExpression(new StringValue("'" + tenantIds[0] + "'"));
		}
		else {
			// 多租户身份
			InExpression inExpression = new InExpression();
			tenantExpression = inExpression;
			inExpression.setLeftExpression(tenantColumn);
			List<Expression> valueList = new ArrayList<>();
			for (String tid : tenantIds) {
				valueList.add(new StringValue("'" + tid + "'"));
			}
			inExpression.setRightItemsList(new ExpressionList(valueList));
		}

		// 加入判断防止条件为空时生成 "and null" 导致查询结果为空
		if (expression == null) {
			return tenantExpression;
		}

		processWhereExpression(expression, tenantId);

		return new AndExpression(tenantExpression, expression);

	}

	@Override
	public void processInsert(Insert insert, String tenantId) {

		if (doTableFilter(insert.getTable().getName())) {
			// 不需要添加租户 id 字段
			return;
		}

		insert.getColumns().add(new Column(this.tenantColumn));

		if (insert.getSelect() != null) {
			// 带子查询的 insert， insert into () select
			processPlainSelect((PlainSelect) insert.getSelect().getSelectBody(), true,
					tenantId);
		}
		else if (insert.getItemsList() != null) {
			// 直接带值的 insert， insert into () values ()
			((ExpressionList) insert.getItemsList()).getExpressions()
					.add(new StringValue(tenantId));
		}
		else {
			throw new RuntimeException("unprocessable sql");
		}

	}

	@Override
	public void processUpdate(Update update, String tenantId) {
		// 获得where条件表达式
		EqualsTo equalsTo = null;
		Expression where = update.getWhere();
		if (!doTableFilter(update.getTable().getName())) {
			// 添加租户 id 字段
			equalsTo = new EqualsTo();
			equalsTo.setLeftExpression(new Column(this.tenantColumn));
			equalsTo.setRightExpression(new StringValue(tenantId));
		}

		if (Objects.isNull(where) && Objects.nonNull(equalsTo)) {
			update.setWhere(equalsTo);
		}

		// 处理子查询
		if (where instanceof BinaryExpression) {
			processWhereExpression(where, tenantId);
			if (Objects.nonNull(equalsTo)) {
				AndExpression andExpression = new AndExpression(equalsTo, where);
				update.setWhere(andExpression);
			}
		}
	}

}
