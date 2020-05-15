package net.gittab.githubtravis.mybatis.parser;

import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;

/**
 * SqlParser.
 *
 * @author xiaohua zhou
 **/
public interface TenantSqlParser {

	/**
	 * 指定 sql id 过滤，不需要添加租户条件字段.
	 * @param statementId statement id
	 * @return boolean
	 */
	boolean doStatementFilter(String statementId);

	/**
	 * set tenant parameter.
	 * @param sql sql
	 * @param tenantId tenant id
	 * @return sql
	 */
	String setTenantParameter(String sql, String tenantId);

	/**
	 * process select body.
	 * @param selectBody select body
	 * @param tenantId tenant id
	 */
	void processSelectBody(SelectBody selectBody, String tenantId);

	/**
	 * process insert.
	 * @param insert insert
	 * @param tenantId tenant id
	 */
	void processInsert(Insert insert, String tenantId);

	/**
	 * process update.
	 * @param update update
	 * @param tenantId tenant id
	 */
	void processUpdate(Update update, String tenantId);

}
