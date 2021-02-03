package test.ant.zip;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ant.zip.ZipUnCompress;
import ant.zip.ZipUnCompressException;

public class ZipUnCompressTest {

	@Test
	public void test() throws ZipUnCompressException, IOException {
		ZipUnCompress zipUnCompress = new ZipUnCompress("M:\\1.zip", "N:\\");
		zipUnCompress.execute();
	}

}
