package ant.zip;

/**
 * 压缩文件名称拦截器。可以修改写入到压缩包中的文件名称
 * 
 * @author PengFei
 * @date 2021年2月3日上午9:14:52
 */
@FunctionalInterface
public interface ZipCompressFileNameInterceptor {

	/**
	 * 处理写入到压缩包中的文件名称
	 * 
	 * @author PengFei
	 * @date 2021年2月3日上午9:14:19
	 * @param fileName 文件名称
	 * @return 处理后的文件名称
	 */
	String process(String fileName);

}
