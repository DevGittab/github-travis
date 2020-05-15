package net.gittab.githubtravis.mybatis.interceptor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.mybatis.filter.TenantFilter;
import net.gittab.githubtravis.mybatis.filter.TenantFilterImpl;
import net.gittab.githubtravis.mybatis.parser.TenantSqlParser;
import net.gittab.githubtravis.mybatis.parser.TenantSqlParserImpl;
import net.gittab.githubtravis.threadlocal.ContestThreadLocal;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * QueryInterceptor.
 *
 * @author xiaohua zhou
 **/
@Slf4j
@Component
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }) })
public class TenantInterceptor implements Interceptor {

	/**
	 * tenant id column name.
	 */
	private static final String TENANT_COLUMN = "customer_id";

	/**
	 * table filter pattern arr.
	 */
	private static final String[] TENANT_TABLE_ARR = new String[] { "activities",
			"templates" };

	private TenantSqlParser tenantSqlParser;

	public TenantInterceptor() {
		TenantFilter tenantFilter = new TenantFilterImpl(Arrays.asList(TENANT_TABLE_ARR),
				"");
		this.tenantSqlParser = new TenantSqlParserImpl(tenantFilter, TENANT_COLUMN);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		if (!(invocation.getTarget() instanceof Executor)) {
			return invocation.proceed();
		}

		Object[] args = invocation.getArgs();
		if (Objects.isNull(args)) {
			return invocation.proceed();
		}

		// get sql
		MappedStatement mappedStatement = (MappedStatement) args[0];

		if (this.tenantSqlParser.doStatementFilter(mappedStatement.getId())) {
			// 直接过滤不添加租户条件
			return invocation.proceed();
		}

		Object parameter = args[1];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);

		String customerId = ContestThreadLocal.getCustomerId();
		// set tenant id
		String newSql = this.tenantSqlParser.setTenantParameter(boundSql.getSql(),
				customerId);

		// rebuild sql
		BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), newSql,
				boundSql.getParameterMappings(), boundSql.getParameterObject());

		// rebuild mapped statement
		MappedStatement newMs = buildMappedStatement(mappedStatement,
				new BoundSqlSource(newBoundSql));
		// add params mapping
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop,
						boundSql.getAdditionalParameter(prop));
			}
		}
		// reset mapped statement
		invocation.getArgs()[0] = newMs;

		return invocation.proceed();
	}

	private MappedStatement buildMappedStatement(MappedStatement ms,
			BoundSqlSource boundSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(
				ms.getConfiguration(), ms.getId(), boundSqlSource,
				ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
			StringBuilder keyProperties = new StringBuilder();
			for (String keyProperty : ms.getKeyProperties()) {
				keyProperties.append(keyProperty).append(",");
			}
			keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
			builder.keyProperty(keyProperties.toString());
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {

	}

	/**
	 * bound sql source.
	 */
	static class BoundSqlSource implements SqlSource {

		private BoundSql boundSql;

		BoundSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			return this.boundSql;
		}

	}

}
