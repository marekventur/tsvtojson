package de.marekventur.tsvtojson.processors.scrobbles;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;
import de.marekventur.tsvtojson.MockDataCollection;

public class JsonProcessorTest {

	private final File testScrobbleData = new File("src/test/data/scrobblesOneUser.tsv");
	
	@Test
	public void test() throws IOException {
		String output = convertTestFile(testScrobbleData, "json/scrobbles/scrobbles-2007-07-01.json");
		
		assertEquals("1", output);
	}
	
	
	private String convertTestFile(File file, String path) throws IOException {
		InputStream input = new FileInputStream(testScrobbleData);
		MockDataCollection output = new MockDataCollection();
		
		new JsonProcessor(new JsonFileNamingStrategy() {
			public String getName(LocalDateTime dateTime) {
				return "scrobbles-" + dateTime.toString(ISODateTimeFormat.yearMonthDay()) + ".json";
			}
			
		}).process(input, output);
		
		output.close();
		
		return output.getData(path);
	}

}
