package ant.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.yelong.commons.io.FileUtilsE;
import org.yelong.commons.io.FilenameUtilsE;

/**
 * zip文件解压
 */
public class ZipUnCompress {

	private final File zipFile;

	/**
	 * 解压后文件存放的目录。<br/>
	 * 可以为空，为空时如果文件压缩时没有携带全路径则会解压到当前项目路径，可能受部署环境影响，如果携带全路径则解压到各个文件的全路径目录下。<br/>
	 * 不为空且解压的文件中存在携带全路径的文件可能会导致解压缩失败。<br/>
	 * 推荐如果压缩的文件中携带了文件全路径则该值为 null 。否则最好不能为 null
	 */
	private final String unZipPath;

	/**
	 * 编码
	 */
	private String encoding = ZipConstant.DEFAULT_ENCODING;

	// ==================================================constructor==================================================

	public ZipUnCompress(File zipFile) {
		this(zipFile, null);
	}

	public ZipUnCompress(String zipFileName) {
		this(zipFileName, null);
	}

	public ZipUnCompress(File zipFile, String unZipPath) {
		this.zipFile = Objects.requireNonNull(zipFile);
		this.unZipPath = unZipPath;
	}

	public ZipUnCompress(String zipFileName, String unZipPath) {
		this.zipFile = Objects.requireNonNull(new File(zipFileName));
		this.unZipPath = unZipPath;
	}

	// ==================================================get/set==================================================

	public File getZipFile() {
		return zipFile;
	}

	public String getUnZipPath() {
		return unZipPath;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	// ==================================================execute==================================================

	/**
	 * 执行解压缩包操作
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws ZipUnCompressException, IOException {
		ZipFile zipFile;
		if (StringUtils.isBlank(encoding)) {
			zipFile = new ZipFile(this.zipFile);
		} else {
			zipFile = new ZipFile(this.zipFile, encoding);
		}
		Enumeration<ZipEntry> entries = zipFile.getEntries();
		byte[] buf = new byte[1024];
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String fileName;
			if (StringUtils.isBlank(unZipPath)) {
				fileName = entry.getName();
			} else {
				fileName = FilenameUtilsE.getFilePath(unZipPath, entry.getName());
			}
			if (entry.isDirectory()) {
				File file = new File(fileName);
				if (!file.exists()) {
					file.mkdirs();
				}
			} else {
				File file = FileUtilsE.createNewFileOverride(fileName);
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
				InputStream inputStream = new BufferedInputStream(zipFile.getInputStream(entry));
				while (true) {
					int readLen = inputStream.read(buf);
					if (-1 == readLen) {
						break;
					}
					outputStream.write(buf, 0, readLen);
				}
				inputStream.close();
				outputStream.close();
			}
		}
	}

}
