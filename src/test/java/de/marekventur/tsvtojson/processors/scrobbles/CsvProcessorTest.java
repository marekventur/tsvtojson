package de.marekventur.tsvtojson.processors.scrobbles;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.marekventur.tsvtojson.MockDataCollection;

import au.com.bytecode.opencsv.CSVReader;

public class CsvProcessorTest {

	private final File testScrobbleData = new File("src/test/data/scrobbles.tsv");
	
	@Test
	public void test() throws IOException {
		List<String[]> parsedResultOfProcessor = convertTestFile(testScrobbleData);
		
		String[] expectedHeader = new String[] {"datetime", "unixtime", "track", "track mbid", "artist", "artist mbid", 
				"album", "album mbid", "album artist", "album artist mbid", "track (uncorrected)", 
				"track mbid (uncorrected)", "artist (uncorrected)", "artist mbid (uncorrected)", 
				"application name", "imported"};
		assertArrayEquals(expectedHeader, parsedResultOfProcessor.get(0));
		
		String[] row1 = new String[] {"2007-06-30T11:25:03", "1183202703", "Rain", "1943ce0b-feb9-4b95-97e1-e73fd3a2855a", "Madonna", "79239441-bfd5-4981-a70c-55c3f15c1287", 
				"Best Ballads", "1384c926-0a21-45c2-9cdf-15bb023bd1cf", "Madonna", "79239441-bfd5-4981-a70c-55c3f15c1287", "Rain", "1943ce0b-feb9-4b95-97e1-e73fd3a2855a", "Madonna", "79239441-bfd5-4981-a70c-55c3f15c1287", 
				"", "false"};
		assertArrayEquals(row1, parsedResultOfProcessor.get(1));
		
		
	}
	
	@Test
	public void testApplication() throws IOException {
		List<String[]> parsedResultOfProcessor = convertTestFile(testScrobbleData);
		
		assertEquals("xBox", parsedResultOfProcessor.get(2)[14]);
	}
	
	@Test
	public void testCorrection() throws IOException {
		List<String[]> parsedResultOfProcessor = convertTestFile(testScrobbleData);
		
		String[] row = new String[] {"2007-07-02T07:07:53", "1183360073", "Mercy Mercy", "1128e415-a1e8-4e9d-8d1f-94f484c8c8d1", "Ace of Base", "d4a1404d-e00c-4bac-b3ba-e3557f6468d6", 
				"Always Have, Always Will", "0a80be1a-794a-46a9-ae2e-12878deae886", "Ace of Base", "d4a1404d-e00c-4bac-b3ba-e3557f6468d6", "Mercy, Mercy", "", "Ace of Base", "d4a1404d-e00c-4bac-b3ba-e3557f6468d6", 
				"", "false"};
		assertArrayEquals(row, parsedResultOfProcessor.get(27));
	}
	
	@Test
	public void testBootstrap() throws IOException {
		List<String[]> parsedResultOfProcessor = convertTestFile(testScrobbleData);
		
		assertEquals("Rain", parsedResultOfProcessor.get(1)[2]);
		assertEquals("Just Hold Me", parsedResultOfProcessor.get(2)[2]);
		assertEquals("Just Hold Me", parsedResultOfProcessor.get(3)[2]);
		assertEquals("false", parsedResultOfProcessor.get(3)[15]); // This is a double scrobble 
		assertEquals("Clocks", parsedResultOfProcessor.get(4)[2]);
		assertEquals("true", parsedResultOfProcessor.get(4)[15]); // This is a bootstrap 
		assertEquals("New Years Day", parsedResultOfProcessor.get(5)[2]);
		assertEquals("true", parsedResultOfProcessor.get(5)[15]);
		assertEquals("New Years Day", parsedResultOfProcessor.get(6)[2]);
		assertEquals("true", parsedResultOfProcessor.get(6)[15]); 
		assertEquals("Run", parsedResultOfProcessor.get(7)[2]);
	}
	
	private List<String[]> convertTestFile(File file) throws IOException {
		InputStream input = new FileInputStream(testScrobbleData);
		MockDataCollection output = new MockDataCollection();
		
		new CsvProcessor().process(input, output);
		
		output.close();
		
		return stringToCsvList(output.getData("scrobbles.tsv"));
	}
	
	private List<String[]> stringToCsvList(String input) throws IOException {
		CSVReader reader = new CSVReader(new StringReader(input), '\t', '"');
		
		List<String[]> result = new ArrayList<String[]>();
		
		String[] row;
		while ((row = reader.readNext()) != null) {
			result.add(row);
	    }
		
		reader.close();
		
		return result;
	}

}
