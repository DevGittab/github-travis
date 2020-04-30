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

	public CommonException(String errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
	}

}
