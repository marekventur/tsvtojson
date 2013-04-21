package de.marekventur.tsvtojson.processors;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.marekventur.tsvtojson.DataCollection;
import de.marekventur.tsvtojson.UserDataCollection;

public interface UserStreamProcessor {
	public void process(InputStream input, DataCollection output) throws IOException;
}
