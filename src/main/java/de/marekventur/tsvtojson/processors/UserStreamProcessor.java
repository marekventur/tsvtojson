package de.marekventur.tsvtojson.processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.marekventur.tsvtojson.UserDataCollection;

public interface UserStreamProcessor {
	public void process(InputStream input, OutputStream output) throws IOException;
}
