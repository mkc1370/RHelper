package name.mkc1370.rhelper.exceptions;

/**
 * An exception representing a problem of RHelper.
 */
public class RHelperException extends RuntimeException {

	public RHelperException(String message, Throwable cause) {
		super(message, cause);
	}

	public RHelperException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return "Please, report that bug to issue tracker on https://github.com/mkc1370/RHelper . Attach your code and this stack trace. "
		       + super.getMessage();
	}
}
