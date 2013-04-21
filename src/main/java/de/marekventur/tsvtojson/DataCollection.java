package de.marekventur.tsvtojson;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public interface DataCollection extends Closeable {
	public OutputStream getOutputStream(String path) throws IOException;
}
