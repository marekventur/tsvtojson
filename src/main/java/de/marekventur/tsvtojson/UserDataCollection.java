package de.marekventur.tsvtojson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * This represents a collection of formated files, folders and so on where 
 * all data is directly streamed to a zip file.
 */
public class UserDataCollection {
	private final File outputFile;
	private final ZipOutputStream zipOutputStream;
	private final static File STATIC_FOLDER = new File ("src/main/data/static/");

	public UserDataCollection() throws IOException {
		outputFile = File.createTempFile("exported", ".zip");
		
		zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile));
		
		writeStaticContent(STATIC_FOLDER, "");
	}
	
	public void close() throws IOException {
		zipOutputStream.close();
	}
	
	public ZipOutputStream getOutputStream(String path) throws IOException {
		zipOutputStream.putNextEntry(new ZipEntry(path));
		return zipOutputStream;
	}
	
	private void writeStaticContent(File file, String relativePath) throws IOException {
		if(file.isFile()){
			FileInputStream in = new FileInputStream(file);
			IOUtils.copy(in, getOutputStream(relativePath));
			in.close();
		}
	 
		if(file.isDirectory()){
			for(String filename : file.list()){
				writeStaticContent(new File(file, filename), relativePath + "/" + filename);
			}
		}
	}

	public File getOutputFile() {
		return outputFile;
	}

	
	
}
