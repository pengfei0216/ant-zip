package ant.zip;

import org.apache.commons.io.FilenameUtils;

/**
 * zip 操作时用到的常量
 */
public final class ZipConstant {

	/**
	 * 默认的编码
	 */
	public static final String DEFAULT_ENCODING = "GBK";

	/**
	 * 保存全路径的拦截器
	 */
	public static final ZipCompressFileNameInterceptor saveFullNameCompressFileNameInterceptor = x -> x;

	/**
	 * 默认的拦截器，只保存文件名称
	 */
	public static final ZipCompressFileNameInterceptor defaultCompressFileNameInterceptor = FilenameUtils::getName;

}
