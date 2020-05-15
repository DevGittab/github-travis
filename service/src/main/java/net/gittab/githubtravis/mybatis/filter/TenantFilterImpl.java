package net.gittab.githubtravis.mybatis.filter;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * TenantFilterImpl.
 *
 * @author xiaohua zhou
 **/
public class TenantFilterImpl implements TenantFilter {

	/**
	 * table patterns.
	 */
	private List<String> tenantTables;

	/**
	 * statement patterns.
	 */
	private Pattern[] statementPatterns;

	public TenantFilterImpl() {
	}

	public TenantFilterImpl(List<String> tenantTables, String statementPatternStr) {
		this.tenantTables = tenantTables;
		this.statementPatterns = compile(statementPatternStr);
	}

	public TenantFilterImpl(List<String> tenantTables, String[] statementPatternArr) {
		this.tenantTables = tenantTables;
		this.statementPatterns = compile(statementPatternArr);
	}

	@Override
	public boolean tableFilter(String tableName) {
		// 默认过滤,不加租户条件
		if (CollectionUtils.isEmpty(this.tenantTables)
				|| StringUtils.isEmpty(tableName)) {
			return true;
		}

		return !this.tenantTables.contains(tableName);
	}

	@Override
	public boolean statementFilter(String statementId) {
		// 默认过滤,不加租户条件
		if (Objects.isNull(this.statementPatterns) || StringUtils.isEmpty(statementId)) {
			return true;
		}

		for (Pattern p : this.statementPatterns) {
			if (p.matcher(statementId).find()) {
				return false;
			}
		}
		return true;
	}

	public static Pattern[] compile(String patterString) {
		if (patterString == null) {
			return new Pattern[] {};
		}
		String[] patterStrings = patterString.split(",");
		return compile(patterStrings);
	}

	public static Pattern[] compile(String[] patterStrings) {
		if (patterStrings == null) {
			return new Pattern[] {};
		}
		Pattern[] patterns = new Pattern[patterStrings.length];
		for (int i = 0; i < patterStrings.length; i++) {
			Pattern pattern = Pattern.compile(patterStrings[i]);
			patterns[i] = pattern;
		}
		return patterns;
	}

}
