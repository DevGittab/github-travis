package net.gittab.githubtravis.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.annotation.BackendApi;
import net.gittab.githubtravis.annotation.MustLogin;
import net.gittab.githubtravis.constant.AuthConstant;
import net.gittab.githubtravis.exception.AuthExceptionCode;

/**
 * AuthInterceptor.
 *
 * @author xiaohua zhou
 **/
@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod hm = (HandlerMethod) handler;

		return this.checkMustLogin(hm, request, response);
	}

	private boolean checkBackendApi(HandlerMethod method, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (!method.hasMethodAnnotation(BackendApi.class)) {
			return true;
		}
		else {
			BackendApi backendApi = method.getMethodAnnotation(BackendApi.class);
			boolean requireLogin = backendApi.requireLogin();
			if (!requireLogin) {
				return true;
			}
			else if (StringUtils
					.isEmpty(request.getHeader(AuthConstant.FRONT_HEADER_TOKEN_KEY))) {
				sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
						AuthExceptionCode.MUST_LOGIN, "请先登录.");
				return false;
			}
			else {
				return true;
			}
		}
	}

	private boolean checkMustLogin(HandlerMethod method, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (!method.hasMethodAnnotation(MustLogin.class)) {
			return true;
		}

		String authorization = request.getHeader(AuthConstant.FRONT_HEADER_TOKEN_KEY);
		if (StringUtils.isEmpty(authorization)) {
			sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
					AuthExceptionCode.MUST_LOGIN, "请先登录.");
			return false;
		}
		return true;
	}

	private void sendError(HttpServletResponse response, int status, String errorCode,
			String message) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(status);
		response.getWriter()
				.write("{\n" + "  \"code\": \"" + errorCode + "\",\n" + "  \"msg\": \""
						+ message + "\",\n" + "  \"status\": " + errorCode + "\n" + "}");
		response.getWriter().flush();
		response.getWriter().close();
	}

}
