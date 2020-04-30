package net.gittab.githubtravis.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * CommonResult.
 *
 * @param <T> object.
 * @author xiaohua zhou
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public final class CommonResult<T> {

	private Integer status = 200;

	private String errorCode = "";

	private String errorMsg = "";

	private T data;

	public CommonResult(T data) {
		this.data = data;
	}

	public static <T> CommonResult<T> errorResult(String errorCode, String errorMsg) {
		CommonResult<T> commonResult = new CommonResult<>();
		commonResult.errorCode = errorCode;
		commonResult.errorMsg = errorMsg;
		commonResult.status = -1;
		return commonResult;
	}

}
