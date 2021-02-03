package test.ant.zip;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import ant.zip.ZipCompress;
import ant.zip.ZipCompressException;

public class ZipCompressTest {

	@Test
	public void test1() throws ZipCompressException, IOException {
		ZipCompress zipCompress = new ZipCompress("M:\\1.zip");
		zipCompress.addFileName("D:\\D未知文件.zip");
		zipCompress.addFileName(
				"E:\\32-log\\module_resource_manage\\employeeCertificate\\file\\2EE56F122B254B13A33DCBAEEC99E379.docx");
		zipCompress.setSaveFileFullPath(true);
//		zipCompress.setIgnoreNotExistFile(false);
		zipCompress.addFileName("C:\\1.txt");
		File zipFile = zipCompress.execute();
		System.out.println(zipFile);
	}

}
