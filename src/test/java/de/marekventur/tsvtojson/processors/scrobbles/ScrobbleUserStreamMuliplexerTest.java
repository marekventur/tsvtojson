package de.marekventur.tsvtojson.processors.scrobbles;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import de.marekventur.tsvtojson.MockDataCollection;
import de.marekventur.tsvtojson.processors.SingleOutputUserStreamProcessor;
import de.marekventur.tsvtojson.processors.UserStreamProcessor;

public class ScrobbleUserStreamMuliplexerTest {

	private UserStreamProcessor getTestUserStreamProcessor(final String path) {
		return new SingleOutputUserStreamProcessor() {
			@Override
			protected void process(InputStream input, OutputStream output)
					throws IOException {
				IOUtils.copy(input, output);
			}
			
			@Override
			protected String getOutputFilePathName() {
				return path;
			}
		};
	}
	
	@Test
	public void test() throws IOException {
		MockDataCollection dataCollection = new MockDataCollection();
		UserStreamProcessor csvProcessor  = getTestUserStreamProcessor("test.tsv");
		UserStreamProcessor jsonProcessor = getTestUserStreamProcessor("test.json");
		
		ScrobbleUserStreamMuliplexer multiplexer = new ScrobbleUserStreamMuliplexer(csvProcessor, jsonProcessor);
	
		InputStream input = new ByteArrayInputStream("foobar".getBytes());
		
		multiplexer.process(input, dataCollection);
		
		input.close();
		dataCollection.close();
		
		assertEquals("foobar", dataCollection.getData("test.tsv"));
		assertEquals("foobar", dataCollection.getData("test.json"));
		
	}

}
