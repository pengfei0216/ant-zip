package test.ant.zip;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import ant.zip.ZipCompress;
import ant.zip.ZipCompressException;

public class ZipCompressTest {

	@Test
	public void test1() throws ZipCompressException, IOException {
		ZipCompress zipCompress = new ZipCompress("C:\\temp\\1.zip");
		zipCompress.addFileName("D:\\D未知文件.zip");
		zipCompress.addFileName(
				"E:\\32-log\\module_resource_manage\\certificate\\file\\9DBBD0B6A1D94313A62213154CC80F47.docx");
		zipCompress.addFileName(
				"E:\\32-log\\module_resource_manage\\certificate\\file\\65A6AA5BA7F747128FC6E714FA6E1A84.docx");
		zipCompress.setSaveFileFullPath(false);
//		zipCompress.setIgnoreNotExistFile(false);
		zipCompress.addFileName("C:\\1.txt");
		zipCompress.setFileNameInterceptor(x -> {
			if (x.startsWith("E:\\")) {
				return x.replace("E:\\", "data\\home\\jcgl\\");
			}
			return x;
		});
		File zipFile = zipCompress.execute();
		System.out.println(zipFile);
	}

}
