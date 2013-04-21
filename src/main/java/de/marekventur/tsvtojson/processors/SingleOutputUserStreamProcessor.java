package de.marekventur.tsvtojson.processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.marekventur.tsvtojson.DataCollection;

public abstract class SingleOutputUserStreamProcessor implements UserStreamProcessor {

	protected abstract String getOutputFilePathName();
	protected abstract void process(InputStream input, OutputStream output)
			throws IOException;

	public final void process(InputStream input, DataCollection output)
			throws IOException {
		process(input, output.getOutputStream(getOutputFilePathName()));
		
	}
	


}
