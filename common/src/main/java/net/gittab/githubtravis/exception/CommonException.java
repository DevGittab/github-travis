package net.gittab.githubtravis.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CommonException.
 *
 * @author xiaohua zhou
 **/
@Data
@NoArgsConstructor
public class CommonException extends RuntimeException {

	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	private String errorCode;

	private String errorMsg;

	public static CommonException unauthorized() {
		return new CommonException(HttpStatus.UNAUTHORIZED, "000001", "请先登录");
	}

	public static CommonException forbidden() {
		return new CommonException(HttpStatus.FORBIDDEN, "000002", "禁止操作");
	}

	public static CommonException badParams() {
		return new CommonException(HttpStatus.BAD_REQUEST, "000003", "参数错误");
	}

	public CommonException(HttpStatus status, String errorCode, String errorMsg) {
		super(errorMsg);
		this.status = status;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

}
