package ant.zip;

import java.io.IOException;

/**
 * zip解压缩异常
 */
public class ZipUnCompressException extends IOException {

	private static final long serialVersionUID = 8196350619449601433L;

	public ZipUnCompressException() {
		super();
	}

	public ZipUnCompressException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZipUnCompressException(String message) {
		super(message);
	}

	public ZipUnCompressException(Throwable cause) {
		super(cause);
	}

}
