package net.gittab.githubtravis.configuration;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSONObject;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.exception.CommonException;

/**
 * CustomErrorDecoderConfig.
 *
 * @author xiaohua zhou
 **/
@Configuration
public class CustomErrorDecoderConfig {

	@Bean
	public ErrorDecoder errorDecoder() {
		return new CustomErrorDecoder();
	}

	/**
	 * custom error decoder.
	 */
	@Slf4j
	public static class CustomErrorDecoder extends ErrorDecoder.Default {

		@Override
		public Exception decode(String methodKey, Response response) {
			// 仅处理 400-500 之内的错误
			if (HttpStatus.BAD_REQUEST.value() <= response.status()
					&& response.status() < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				try {
					// 获取内部 api 原始的返回的错误信息
					String errorMsgStr = Util.toString(response.body().asReader());
					JSONObject jsonObject = JSONObject.parseObject(errorMsgStr);
					HttpStatus status = HttpStatus
							.valueOf(jsonObject.getIntValue("status"));
					// 仅处理 400-500 之内的错误
					if (HttpStatus.BAD_REQUEST.value() <= status.value() && status
							.value() < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
						// 自定义异常
						String errorCode = jsonObject.getString("error_code");
						String errorMsg = jsonObject.getString("error_msg");
						return new CommonException(status, errorCode, errorMsg);
					}
				}
				catch (IOException ignore) {
					return super.decode(methodKey, response);
				}
			}
			return super.decode(methodKey, response);
		}

	}

}
