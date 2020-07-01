package net.gittab.githubtravis.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.constant.AuthConstant;
import net.gittab.githubtravis.exception.AuthExceptionCode;
import net.gittab.githubtravis.feign.api.AuthFeignApi;
import net.gittab.githubtravis.threadlocal.ContestThreadLocal;
import net.gittab.githubtravis.feign.wrapper.CustomRequestWrapper;

/**
 * AuthFilter.
 *
 * @author xiaohua zhou
 **/
@Slf4j
@WebFilter(filterName = "serviceFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

	private static final String INTERNAL_PATH_PREFIX = "/internal/api/";

	private static final String BACKEND_PATH_PREFIX = "/v2/backend/";

	@Autowired
	private AuthFeignApi authFeignApi;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI().substring(req.getContextPath().length())
				.replaceAll("[/]+$", "");
		log.info("request path is {}", path);

		Map<String, Object> accessTokenInfo;

		if (path.startsWith(INTERNAL_PATH_PREFIX)) {
			// internal api call
			String customerId = req.getHeader(AuthConstant.CUSTOMER_ID_KEY);
			if (StringUtils.isEmpty(customerId)) {
				sendError((HttpServletResponse) response,
						HttpServletResponse.SC_BAD_REQUEST, AuthExceptionCode.BAD_REQUEST,
						"参数错误.");
				return;
			}
			accessTokenInfo = new HashMap<>(1);
			accessTokenInfo.put("customerId", customerId);
			// String params = StreamUtils.copyToString(req.getInputStream(),
			// StandardCharsets.UTF_8);
			// log.info("body params is {}" + JSONObject.toJSON(params).toString());
		}
		else {
			// external api call
			String customerIdHeader = req.getHeader(AuthConstant.CUSTOMER_ID_KEY);
			log.info("get the authorization is {}", customerIdHeader);

			if (StringUtils.isEmpty(customerIdHeader)
					&& !path.startsWith(BACKEND_PATH_PREFIX)) {
				String domain = req.getHeader(AuthConstant.FRONT_HEADER_DOMAIN_KEY);
				if (StringUtils.isEmpty(domain)) {
					sendError((HttpServletResponse) response,
							HttpServletResponse.SC_UNAUTHORIZED,
							AuthExceptionCode.MUST_LOGIN, "请先登录.");
					return;
				}
				Long customerId = this.authFeignApi.findByDomain(domain);
				if (StringUtils.isEmpty(customerId)) {
					sendError((HttpServletResponse) response,
							HttpServletResponse.SC_UNAUTHORIZED,
							AuthExceptionCode.MUST_LOGIN, "请先登录.");
					return;
				}
				accessTokenInfo = new HashMap<>(1);
				accessTokenInfo.put("customerId", customerId);
			}
			else {
				accessTokenInfo = new HashMap<>(1);
				accessTokenInfo.put("customerId", customerIdHeader);
			}
		}

		CustomRequestWrapper customRequestWrapper = new CustomRequestWrapper(req);

		// print request body params
		if (!StringUtils.isEmpty(customRequestWrapper.getBody())) {
			Object obj = JSONObject.parse(customRequestWrapper.getBody());
			log.info("request body params is {}", JSON.toJSONString(obj));
		}

		ContestThreadLocal.set(accessTokenInfo);
		chain.doFilter(customRequestWrapper, response);
		ContestThreadLocal.remove();
	}

	private void sendError(HttpServletResponse response, int status, String errorCode,
			String message) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(status);
		response.getWriter()
				.write("{\n" + "  \"code\": \"" + errorCode + "\",\n" + "  \"msg\": \""
						+ message + "\",\n" + "  \"status\": " + status + "\n" + "}");
		response.getWriter().flush();
		response.getWriter().close();
	}

}
