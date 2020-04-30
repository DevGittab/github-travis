package net.gittab.githubtravis.configuration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.exception.CommonException;
import net.gittab.githubtravis.exception.CommonResult;

/**
 * UnifiedReturnConfig.
 *
 * @author xiaohua zhou
 **/
@Slf4j
@Configuration
public class UnifiedReturnConfig {

	/**
	 * 统一结果返回，当没有数据返回时，将状态码设置为 204.
	 */
	@RestControllerAdvice("me.shijue.copyright.controller")
	static class CommonResultResponseAdvice implements ResponseBodyAdvice<Object> {

		@Override
		public boolean supports(MethodParameter methodParameter,
				Class<? extends HttpMessageConverter<?>> aClass) {
			return true;
		}

		@Override
		public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
				MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
				ServerHttpRequest serverHttpRequest,
				ServerHttpResponse serverHttpResponse) {
			if (StringUtils.isEmpty(body)) {
				serverHttpResponse.setStatusCode(HttpStatus.NO_CONTENT);
				return null;
			}
			return body;
		}

	}

	/**
	 * UnifiedExceptionHandler.
	 */
	@RestControllerAdvice("me.shijue.copyright.controller")
	static class UnifiedExceptionHandler {

		@ExceptionHandler(Exception.class)
		public CommonResult<Void> handleException(Exception exception,
				HttpServletResponse response) {
			if (exception instanceof HttpMessageNotReadableException) {
				log.warn("warning", exception);
				response.setStatus(400);
				return CommonResult.errorResult("400", "请求参数格式有误");
			}
			else if (exception instanceof MissingServletRequestParameterException) {
				log.warn("warning", exception);
				response.setStatus(400);
				return CommonResult.errorResult("400", exception.getLocalizedMessage());
			}
			response.setStatus(500);
			log.error("error", exception);
			return CommonResult.errorResult("500", exception.getLocalizedMessage());
		}

		@ExceptionHandler(CommonException.class)
		public CommonResult<Void> handleBusinessException(CommonException commonException,
				HttpServletResponse response) {
			log.error("error", commonException);
			response.setStatus(commonException.getStatus().value());
			return CommonResult.errorResult(commonException.getErrorCode(),
					commonException.getErrorMsg());
		}

		@ExceptionHandler(MethodArgumentNotValidException.class)
		public CommonResult<Void> handleValidationException(
				MethodArgumentNotValidException validException,
				HttpServletResponse response) {
			log.error("error", validException);
			response.setStatus(400);
			// 数据校验异常信息
			return CommonResult.errorResult("400", validException.getBindingResult()
					.getAllErrors().get(0).getDefaultMessage());
		}

	}

}
