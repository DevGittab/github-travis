package net.gittab.githubtravis.mybatis.filter;

/**
 * TenantTableFilter.
 *
 * @author xiaohua zhou
 **/
public interface TenantFilter {

	/**
	 * 指定表名过滤，是否不需要添加租户条件字段.
	 * @param tableName table name
	 * @return boolean
	 */
	boolean tableFilter(String tableName);

	/**
	 * 指定 sql id 过滤，是否不需要添加租户条件字段.
	 * @param statementId statement id
	 * @return boolean
	 */
	boolean statementFilter(String statementId);

}
