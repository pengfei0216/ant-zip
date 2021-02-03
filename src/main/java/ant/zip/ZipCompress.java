package ant.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * zip文件压缩
 */
public class ZipCompress {

	/**
	 * 写入到的压缩包文件名称
	 */
	private final File zipFile;

	private final List<File> files = new ArrayList<File>();

	/**
	 * 是否忽略不存在的文件。如果不忽略，文件不存在时抛出异常
	 */
	private boolean ignoreNotExistFile = true;

	/**
	 * 是否保存文件全路径。 <br/>
	 * 保存文件全路径：将该文件所在的目录一并写入到压缩包中。如：C:\\temp\\file.txt 写入到压缩包中就在C: temp
	 * 两层目录下面。一般用在两个电脑中相同目录的文件用压缩包进行传输的情况。 <br/>
	 * 不保存文件全路径：即直接将该文件写到压缩包中<br/>
	 * 这个属性会被 {@link #fileNameInterceptor}覆盖
	 */
	private boolean saveFileFullPath = false;

	/**
	 * 文件名称拦截器
	 */
	private ZipCompressFileNameInterceptor fileNameInterceptor;

	/**
	 * 编码
	 */
	private String encoding = ZipConstant.DEFAULT_ENCODING;

	// ==================================================constructor==================================================

	public ZipCompress(String zipFileName) {
		this(new File(zipFileName));
	}

	public ZipCompress(File zipFile) {
		this.zipFile = Objects.requireNonNull(zipFile);
	}

	// ==================================================addFileName==================================================

	/**
	 * 添加被压缩的文件
	 * 
	 * @param fileName 文件名称
	 * @return this
	 */
	public ZipCompress addFileName(String fileName) {
		return addFile(new File(fileName));
	}

	/**
	 * 添加被压缩的文件
	 * 
	 * @param files 文件名称数组
	 * @return this
	 */
	public ZipCompress addFileNames(String... files) {
		if (ArrayUtils.isNotEmpty(files)) {
			for (String fileName : files) {
				addFileName(fileName);
			}
		}
		return this;
	}

	/**
	 * 添加被压缩的文件
	 * 
	 * @param files 文件名称集合
	 * @return this
	 */
	public ZipCompress addFileNames(Collection<String> files) {
		if (CollectionUtils.isNotEmpty(files)) {
			for (String fileName : files) {
				addFileName(fileName);
			}
		}
		return this;
	}

	// ==================================================removeFileName==================================================

	/**
	 * 移除一个被压缩的文件
	 * 
	 * @param fileName 需要被移除的文件名称
	 * @return this
	 */
	public ZipCompress removeFileName(String fileName) {
		this.files.removeIf(x -> x.getName().equals(fileName));
		return this;
	}

	// ==================================================addFile==================================================

	/**
	 * 添加被压缩的文件
	 * 
	 * @param file 被压缩的文件
	 * @return this
	 */
	public ZipCompress addFile(File file) {
		this.files.add(file);
		return this;
	}

	/**
	 * 添加被压缩的文件
	 * 
	 * @param files 被压缩的文件集合
	 * @return this
	 */
	public ZipCompress addFiles(Collection<? extends File> files) {
		this.files.addAll(files);
		return this;
	}

	/**
	 * 添加被压缩的文件
	 * 
	 * @param files 被压缩的文件集合
	 * @return this
	 */
	public ZipCompress addFiles(File... files) {
		if (ArrayUtils.isNotEmpty(files)) {
			for (File file : files) {
				addFile(file);
			}
		}
		return this;
	}

	// ==================================================removeFile==================================================

	/**
	 * 移除一个被压缩的文件
	 * 
	 * @param file 被压缩的文件
	 * @return this
	 */
	public ZipCompress removeFile(File file) {
		this.files.remove(file);
		return this;
	}

	/**
	 * 移除所有被压缩的文件
	 * 
	 * @return this
	 */
	public ZipCompress removeAll() {
		this.files.clear();
		return this;
	}

	// ==================================================containsFile==================================================

	/**
	 * 是否包含某个文件
	 * 
	 * @param file 判断是否包含的文件
	 * @return <code>true</code>被压缩的文件中包含该文件
	 */
	public boolean containsFile(File file) {
		return this.files.contains(file);
	}

	/**
	 * 是否包含某个文件名称
	 * 
	 * @param fileName 判断是否包含的文件名称
	 * @return <code>true</code>被压缩的文件中包含该文件名称
	 */
	public boolean containsFileName(String fileName) {
		return this.files.stream().map(File::getName).filter(x -> x.equals(fileName)).count() > 0;
	}

	// ==================================================get/set==================================================

	public File getZipFile() {
		return zipFile;
	}

	public void setIgnoreNotExistFile(boolean ignoreNotExistFile) {
		this.ignoreNotExistFile = ignoreNotExistFile;
	}

	public boolean isIgnoreNotExistFile() {
		return ignoreNotExistFile;
	}

	public void setSaveFileFullPath(boolean saveFileFullPath) {
		this.saveFileFullPath = saveFileFullPath;
	}

	public boolean isSaveFileFullPath() {
		return saveFileFullPath;
	}

	public ZipCompressFileNameInterceptor getFileNameInterceptor() {
		return fileNameInterceptor;
	}

	public void setFileNameInterceptor(ZipCompressFileNameInterceptor fileNameInterceptor) {
		this.fileNameInterceptor = fileNameInterceptor;
	}

	// ==================================================execute==================================================

	/**
	 * 执行压缩操作
	 * 
	 * @return 压缩包文件
	 * @see #zipFile
	 */
	public File execute() throws ZipCompressException, IOException {
		List<File> files = new ArrayList<File>(this.files);
		if (ignoreNotExistFile) {
			// 移除所有不存在的文件
			files.removeIf(x -> !x.exists());
		} else {
			for (File file : files) {
				if (!file.exists()) {
					throw new FileNotFoundException(file.getAbsolutePath());
				}
			}
		}
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(this.zipFile));
		if (StringUtils.isNotBlank(encoding)) {
			out.setEncoding(encoding);
		}
		for (File file : files) {
			String fileName = file.getAbsolutePath();
			String compressFileName = getCompressFileName(fileName);
			out.putNextEntry(new ZipEntry(compressFileName));
			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] data = new byte[1024];
			while (true) {
				int len = fileInputStream.read(data);
				if (-1 == len) {
					break;
				}
				out.write(data, 0, len);
			}
			fileInputStream.close();
		}
		out.close();
		return zipFile;
	}

	// ==================================================support==================================================

	/**
	 * 获取写入压缩包的文件名称
	 * 
	 * @author PengFei
	 * @date 2021年2月3日上午9:21:30
	 * @param fileName 文件名称
	 * @return 写入到压缩包的文件名称
	 */
	protected String getCompressFileName(String fileName) {
		ZipCompressFileNameInterceptor fileNameInterceptor = this.fileNameInterceptor;
		if (null == fileNameInterceptor) {
			if (saveFileFullPath) {
				fileNameInterceptor = ZipConstant.saveFullNameCompressFileNameInterceptor;
			} else {
				fileNameInterceptor = ZipConstant.defaultCompressFileNameInterceptor;
			}
		}
		return fileNameInterceptor.process(fileName);
	}

}
