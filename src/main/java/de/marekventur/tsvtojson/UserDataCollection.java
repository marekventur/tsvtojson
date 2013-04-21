package de.marekventur.tsvtojson;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * This represents a collection of formated files, folders and so on where all
 * data is directly streamed to a zip file.
 */
public class UserDataCollection implements DataCollection, Closeable {

	/**
	 * Just a marker class to indicate that this file should get deleted after
	 * usage
	 */
	private class TemporaryFile extends File {
		private static final long serialVersionUID = 1820574204097041083L;

		public TemporaryFile(File file) {
			super(file.getAbsolutePath());
		}
	};

	private File outputFile = null;
	private final static File STATIC_FOLDER = new File("src/main/data/static/");
	private final Map<String, File> temporaryFileMap = new HashMap<String, File>();

	public UserDataCollection() {
		addStaticContent(STATIC_FOLDER, "");
	}

	public void close() throws IOException {
		outputFile = File.createTempFile("userDataCollection", ".zip");

		ZipOutputStream zipOutputStream = new ZipOutputStream(
				new FileOutputStream(outputFile));

		for (Entry<String, File> entry : temporaryFileMap.entrySet()) {
			String path = entry.getKey();
			File file = entry.getValue();

			zipOutputStream.putNextEntry(new ZipEntry(path));
			FileInputStream in = new FileInputStream(file);
			IOUtils.copy(in, zipOutputStream);
			in.close();
			if (file instanceof TemporaryFile) {
				if (!file.delete()) {
					throw new IOException("Couldn't delete temporary file "
							+ file.getAbsolutePath());
				}
			}
		}

		zipOutputStream.close();
	}

	/**
	 * Returns a buffered output stream for a data collection element. More than
	 * one output stream on the same object can be used at the same time, data
	 * is buffered to a temporary file until UserDataCollection "close" is
	 * called. <br>
	 * <b>Important:</b> The caller of this function is responsible for closing
	 * the OutputStream
	 */
	public OutputStream getOutputStream(String path) throws IOException {

		if (temporaryFileMap.containsKey(path)) {
			throw new RuntimeException("Path {} is already in use.");
		}

		TemporaryFile tempBufferFile = new TemporaryFile(File.createTempFile(
				"tempStreamBuffer", ".tmp"));
		temporaryFileMap.put(path, tempBufferFile);

		return new BufferedOutputStream(new FileOutputStream(tempBufferFile));
	}

	/**
	 * Walks the "static" folder and recursively adds every file to the
	 * temporaryFileMap
	 */
	private void addStaticContent(File file, String relativePath) {
		if (file.isFile()) {
			temporaryFileMap.put(relativePath, file);
		}

		if (file.isDirectory()) {
			for (String filename : file.list()) {
				addStaticContent(new File(file, filename), relativePath + "/"
						+ filename);
			}
		}
	}

	public File getOutputFile() {
		if (outputFile == null) {
			throw new RuntimeException(
					"UserDataCollection has to be closed before the output file can be accessed.");
		}
		return outputFile;
	}

}
