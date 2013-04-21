package de.marekventur.tsvtojson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MockDataCollection implements DataCollection {

	private final Map<String, ByteArrayOutputStream> streams = new HashMap<String, ByteArrayOutputStream>();
	private Map<String, String> strings;
	
	
	public void close() throws IOException {
		strings = new HashMap<String, String>();
		for(Entry<String, ByteArrayOutputStream> entry: streams.entrySet()) {
			strings.put(entry.getKey(), entry.getValue().toString());
		}
	}

	public OutputStream getOutputStream(String path) throws IOException {
		if (streams.containsKey(path)) {
			throw new RuntimeException("Don't open the same path twice! " + path);
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		streams.put(path, byteArrayOutputStream);
		return byteArrayOutputStream;
	}
	
	public String getData(String path) {
		return strings.get(path);
	}

}
