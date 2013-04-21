package de.marekventur.tsvtojson.processors.scrobbles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.TeeInputStream;

import de.marekventur.tsvtojson.DataCollection;
import de.marekventur.tsvtojson.processors.UserStreamProcessor;

public class ScrobbleUserStreamMuliplexer implements UserStreamProcessor {

	private final UserStreamProcessor csvProcessor;
	private final UserStreamProcessor jsonProcessor;

	public ScrobbleUserStreamMuliplexer(UserStreamProcessor csvProcessor,
			UserStreamProcessor jsonProcessor) {
		this.csvProcessor = csvProcessor;
		this.jsonProcessor = jsonProcessor;
	}

	public void process(InputStream input, DataCollection output) throws IOException {
		
		ByteArrayOutputStream memoryCopyStreamReader = new ByteArrayOutputStream();
		
		InputStream tsvInputStream = new TeeInputStream(input, memoryCopyStreamReader);
		InputStream jsonInputStream = null;
		
		try {
			csvProcessor.process(tsvInputStream, output);
			
			jsonInputStream = new ByteArrayInputStream(memoryCopyStreamReader.toByteArray());
			jsonProcessor.process(jsonInputStream, output);	
		} finally {
			tsvInputStream.close();
			if (jsonInputStream != null) {
				jsonInputStream.close();
			}
				
		}
		
		
		
	}

}
