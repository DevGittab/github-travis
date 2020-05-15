package net.gittab.githubtravis.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.gittab.githubtravis.exception.CommonException;

/**
 * ContestThreadLocal.
 *
 * @author xiaohua zhou
 **/
public class ContestThreadLocal {

	private final static ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal
			.withInitial(HashMap::new);

	public static Map<String, Object> get() {
		return THREAD_LOCAL.get();
	}

	public static void set(Map<String, Object> value) {
		THREAD_LOCAL.set(value);
	}

	public static void remove() {
		THREAD_LOCAL.remove();
	}

	public static String getCurrentUser() {
		Map<String, Object> accessTokenInfo = get();
		if (Objects.isNull(accessTokenInfo)) {
			throw CommonException.unauthorized();
		}

		Object userIdObj = accessTokenInfo.get("userId");

		if (Objects.isNull(userIdObj)) {
			throw CommonException.unauthorized();
		}

		return userIdObj.toString();
	}

	public static String getCustomerId() {
		Map<String, Object> accessTokenInfo = get();
		if (Objects.isNull(accessTokenInfo)) {
			throw CommonException.unauthorized();
		}
		Object customerIdObj = accessTokenInfo.get("customerId");

		if (Objects.isNull(customerIdObj)) {
			throw CommonException.unauthorized();
		}

		return customerIdObj.toString();
	}

}
