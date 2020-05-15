package net.gittab.githubtravis.feign.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.constant.AuthConstant;

/**
 * FeignRequestInterceptor.
 *
 * @author xiaohua zhou
 **/
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();

		// if attributes is null, return
		if (attributes == null) {
			return;
		}

		HttpServletRequest request = attributes.getRequest();
		Enumeration<String> headerNames = request.getHeaderNames();

		// if header names is null, return
		if (headerNames == null) {
			return;
		}

		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			if (AuthConstant.FRONT_HEADER_TOKEN_KEY.equals(name)) {
				String value = request.getHeader(name);
				template.header(name, value);
			}
		}
		template.header("test", "test");
	}

}
