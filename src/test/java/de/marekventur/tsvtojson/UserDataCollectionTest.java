package de.marekventur.tsvtojson;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.io.Files;

public class UserDataCollectionTest {

	private File uncompressFolder(File in) throws ZipException {
		File folder = Files.createTempDir();
		new ZipFile(in).extractAll(folder.getAbsolutePath());
		return folder;
	}
	
	@Test
	public void testItCopiesStaticData() throws IOException, ZipException {
		UserDataCollection udc = new UserDataCollection();
		udc.close();
		
		File uncompressedFolder = uncompressFolder(udc.getOutputFile());
	
		assertTrue(new File(uncompressedFolder, "index.html").exists());
		assertTrue(new File(uncompressedFolder, "README.txt").exists());
		assertTrue(new File(uncompressedFolder, "viewer-includes/main.js").exists());
		
		assertTrue (FileUtils.readFileToString(new File(uncompressedFolder, "index.html")).length() > 20);
		
		Files.deleteRecursively(uncompressedFolder);
		udc.getOutputFile().deleteOnExit();
	}
	
	@Test
	public void testItAllowsDataToBeWritten() throws IOException, ZipException {
		UserDataCollection udc = new UserDataCollection();
		
		Writer writer1 = new OutputStreamWriter(udc.getOutputStream("scrobbles.tsv"));
		writer1.write("foo");
		writer1.flush();
		
		Writer writer2 = new OutputStreamWriter(udc.getOutputStream("json/scrobbles.json"));
		writer2.write("bar");
		writer2.flush();
		
		udc.close();
		
		File uncompressedFolder = uncompressFolder(udc.getOutputFile());
	

		assertEquals("foo", (FileUtils.readFileToString(new File(uncompressedFolder, "scrobbles.tsv"))));
		assertEquals("bar", (FileUtils.readFileToString(new File(uncompressedFolder, "json/scrobbles.json"))));
		
		Files.deleteRecursively(uncompressedFolder);
		udc.getOutputFile().deleteOnExit();
	}

}
