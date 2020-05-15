package net.gittab.githubtravis.mybatis.interceptor;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;

import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.annotation.TenantClass;
import net.gittab.githubtravis.annotation.TenantField;
import net.gittab.githubtravis.threadlocal.ContestThreadLocal;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * UpdateInterceptor.
 *
 * @author xiaohua zhou
 **/
@Slf4j
@ConditionalOnProperty(value = "domain.customerId", havingValue = "true")
@Intercepts({ @Signature(type = Executor.class, method = "update", args = {
		MappedStatement.class, Object.class }) })
public class UpdateInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		log.info(" update interceptor ");
		if (!(invocation.getTarget() instanceof Executor)) {
			return invocation.proceed();
		}

		Object parameter = invocation.getArgs()[1];
		TenantClass tenantClass = AnnotationUtils.findAnnotation(parameter.getClass(),
				TenantClass.class);
		if (Objects.isNull(tenantClass)) {
			return invocation.proceed();
		}

		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

		Field[] declaredFields = parameter.getClass().getDeclaredFields();

		String customerId = ContestThreadLocal.getCustomerId();

		for (Field field : declaredFields) {
			TenantField tenantField = field.getAnnotation(TenantField.class);
			if (Objects.isNull(tenantField)) {
				continue;
			}

			if (!Objects.equals(SqlCommandType.INSERT, sqlCommandType)) {
				continue;
			}
			// set tenant id
			field.setAccessible(true);
			field.set(parameter, customerId);
		}

		return invocation.proceed();
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

}
