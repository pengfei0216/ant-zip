package test.ant.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.yelong.commons.io.FileUtilsE;
import org.yelong.commons.io.FilenameUtilsE;

public class ZipUtils {

	public static void main(String[] args) throws IOException {
		//不指定目的目录会写到当前项目中
//		unZipFile("M:\\1.zip", "");
		File file = new File("1 - 副本 (2).xlsx");
		System.out.println(file.exists());
		System.out.println(file.getAbsolutePath());
	}
	
	public static void zipFile(String zipFileName, List<String> fileNameList, boolean fileNameOnly) throws IOException {
		if (StringUtils.isBlank(zipFileName)) {
			throw new IOException("压缩目录不能为空");
		}
		if (CollectionUtils.isEmpty(fileNameList)) {
			throw new IOException("被压缩的文件不能为空");
		}
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		out.setEncoding(System.getProperty("sun.jnu.encoding"));
		for (String fileName : fileNameList) {
			String compressFileName;
			if (fileNameOnly) {
				compressFileName = FilenameUtils.getName(fileName);
			} else {
				compressFileName = fileName;
			}
			out.putNextEntry(new ZipEntry(compressFileName));
			FileInputStream fileInputStream = new FileInputStream(fileName);
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
	}

	@SuppressWarnings("unchecked")
	public static void unZipFile(String zipFileName, String unZipPath) throws IOException {
		if (StringUtils.isBlank(zipFileName)) {
			throw new IOException("压缩文件名称不能为空");
		}
		ZipFile zipFile = new ZipFile(zipFileName, "GBK");
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
