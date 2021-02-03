package ant.zip;

import java.io.IOException;

/**
 * zip压缩异常
 */
public class ZipCompressException extends IOException {

	private static final long serialVersionUID = 8196350619449601433L;

	public ZipCompressException() {
		super();
	}

	public ZipCompressException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZipCompressException(String message) {
		super(message);
	}

	public ZipCompressException(Throwable cause) {
		super(cause);
	}

}
