package de.marekventur.tsvtojson;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Scanner;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.io.Files;

public class UserDataCollectionTest {
	
	@Test
	public void testItCopiesStaticData() throws IOException {
		UserDataCollection udc = new UserDataCollection();
		udc.close();
		
		ZipFile zipFile = new ZipFile(udc.getOutputFile());
		
		InputStream inputStream = zipFile.getInputStream(zipFile.getEntry("/README.txt"));
		Scanner s = new Scanner(inputStream);
		assertEquals("lkjasflkjas", s.next());
		s.close();
		
		inputStream = zipFile.getInputStream(zipFile.getEntry("/viewer-includes/main.js"));
		s = new Scanner(inputStream);
		assertEquals("asdfasdfsafd", s.next());
		s.close();
		
		udc.getOutputFile().deleteOnExit();
	}
	
	@Test
	public void testItAllowsDataToBeWritten() throws IOException {
		UserDataCollection udc = new UserDataCollection();
		
		Writer writer1 = new OutputStreamWriter(udc.getOutputStream("scrobbles.tsv"));
		writer1.write("foo");
		
		Writer writer2 = new OutputStreamWriter(udc.getOutputStream("json/scrobbles.json"));
		writer2.write("bar");
		
		writer1.write("foo");
		writer2.write("bar");
		
		writer1.close();
		writer2.close();
		
		udc.close();
		
		ZipFile zipFile = new ZipFile(udc.getOutputFile());
		
		InputStream inputStream = zipFile.getInputStream(zipFile.getEntry("scrobbles.tsv"));
		Scanner s = new Scanner(inputStream);
		assertEquals("foofoo", s.next());
		s.close();
		
		inputStream = zipFile.getInputStream(zipFile.getEntry("json/scrobbles.json"));
		s = new Scanner(inputStream);
		assertEquals("barbar", s.next());
		s.close();

		udc.getOutputFile().deleteOnExit();
	}
	

}
