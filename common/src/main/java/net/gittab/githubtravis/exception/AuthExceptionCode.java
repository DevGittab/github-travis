package net.gittab.githubtravis.exception;

/**
 * AuthExceptionCode.
 *
 * @author xiaohua zhou
 **/
public interface AuthExceptionCode {

	/**
	 * User must log in first.
	 */
	String MUST_LOGIN = "000001";

	/**
	 * forbidden.
	 */
	String FORBIDDEN = "000002";

	/**
	 * bad request.
	 */
	String BAD_REQUEST = "000003";

}
