package de.marekventur.tsvtojson.processors.scrobbles;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.stream.JsonWriter;

import au.com.bytecode.opencsv.CSVReader;

import de.marekventur.tsvtojson.DataCollection;
import de.marekventur.tsvtojson.processors.UserStreamProcessor;
import de.marekventur.tsvtojson.processors.scrobbles.CsvProcessor.OutputColumns;

public class JsonProcessor implements UserStreamProcessor {

	private final JsonFileNamingStrategy fileNamingStrategy;
	private final static String NULL_INPUT_VALUE = "NULL";
	
	public JsonProcessor(JsonFileNamingStrategy fileNamingStrategy) {
		this.fileNamingStrategy = fileNamingStrategy;
	}

	public void process(InputStream input, DataCollection output)
			throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(input), '\t', '"');
		JsonWriter jsonWriter = null;
		
		try {
			List<String> filenames = new ArrayList<String>();
			
			String[] row;
			
			String lastFilenameUsed = null;
			
			while ((row = reader.readNext()) != null) {
				Map<ScrobbleInputColumns, String> rowMap = new HashMap<ScrobbleInputColumns, String>();
				for (ScrobbleInputColumns column: ScrobbleInputColumns.values()) {
					String cell = row[column.ordinal()];
					if (NULL_INPUT_VALUE.equals(cell)) {
						cell = null;
					}
					rowMap.put(column, cell);
				}	
				
				String filename;
				LocalDateTime localDateTime;
				if ("0".equals(rowMap.get(ScrobbleInputColumns.BOOTSTRAP_COUNT))) {
					localDateTime = convertUnixtimeToLocalDateTime(rowMap.get(ScrobbleInputColumns.UNIXTIME));
					filename = "json/scrobbles/" + fileNamingStrategy.getName(localDateTime);
				} else {
					localDateTime = null;
					filename = "json/scrobbles/bootstrap.json";
				}
				System.out.println(filename);
				if (!filename.equals(lastFilenameUsed)) {
					if (jsonWriter != null) {
						jsonWriter.endArray();
						jsonWriter.close();
					}
					jsonWriter = new JsonWriter(new OutputStreamWriter(output.getOutputStream(filename)));
					jsonWriter.beginArray();
					
					lastFilenameUsed = filename;
					filenames.add(filename);
				}
				
				processRow(rowMap, localDateTime, jsonWriter);
		    }
			
			if (jsonWriter != null) {
				jsonWriter.endArray();
				jsonWriter.close();
			}
			
			// TODO: Write filenames to helper JSON
			
		} finally {
			reader.close();
			if (jsonWriter != null) {
				jsonWriter.close();
			}
		}

	}
	
	private LocalDateTime convertUnixtimeToLocalDateTime(String input) {
		long timestamp = Long.valueOf(input) * 1000;
		return new LocalDateTime(timestamp, DateTimeZone.UTC);		
	}
	
	private void processRow(Map<ScrobbleInputColumns, String> rowMap, LocalDateTime dateTime, JsonWriter writer) throws IOException {
		writer.beginObject();
		
		int bootstrapCount = Integer.valueOf(rowMap.get(ScrobbleInputColumns.BOOTSTRAP_COUNT));
		if (bootstrapCount == 0) {
			
			writer.name("time").beginObject();
			writer.name("iso").value(dateTime.toString(ISODateTimeFormat.dateHourMinuteSecond()));
			writer.name("unixtime").value(Integer.valueOf(rowMap.get(ScrobbleInputColumns.UNIXTIME)));
			writer.endObject();
			
			writer.name("application").value(rowMap.get(ScrobbleInputColumns.APPLICATION));
			writer.name("bootstrapped").value(false);
		} else {
			writer.name("bootstrapped").value(false);
			writer.name("bootstrapCount").value(bootstrapCount);
		}
		
		
		writer.name("track").beginObject();
		if (rowMap.get(ScrobbleInputColumns.ARTIST_NAME) == null) {
			writer.name("corrected").value(false);
			
			writer.name("name").value(rowMap.get(ScrobbleInputColumns.UNCORRECTED_TRACK_NAME));
			writer.name("mbid").value(rowMap.get(ScrobbleInputColumns.UNCORRECTED_TRACK_MBID));
			writer.name("artist").beginObject();
			writer.name("name").value(rowMap.get(ScrobbleInputColumns.UNCORRECTED_ARTIST_NAME));
			writer.name("mbid").value(rowMap.get(ScrobbleInputColumns.UNCORRECTED_ARTIST_MBID));
			writer.endObject();
			
		} else {
			writer.name("corrected").value(true);
			
			writer.name("name").value(rowMap.get(ScrobbleInputColumns.TRACK_NAME));
			writer.name("mbid").value(rowMap.get(ScrobbleInputColumns.TRACK_MBID));
			writer.name("artist").beginObject();
			writer.name("name").value(rowMap.get(ScrobbleInputColumns.ARTIST_NAME));
			writer.name("mbid").value(rowMap.get(ScrobbleInputColumns.ARTIST_MBID));
			writer.endObject();
			
			writer.name("uncorrectedTrack").beginObject();
			writer.name("name").value(rowMap.get(ScrobbleInputColumns.UNCORRECTED_TRACK_NAME));
			writer.name("mbid").value(rowMap.get(ScrobbleInputColumns.UNCORRECTED_TRACK_MBID));
			writer.name("artist").beginObject();
			writer.name("name").value(rowMap.get(ScrobbleInputColumns.UNCORRECTED_ARTIST_NAME));
			writer.name("mbid").value(rowMap.get(ScrobbleInputColumns.UNCORRECTED_ARTIST_MBID));
			writer.endObject();
			writer.endObject();
		}
		writer.endObject();
		
		writer.name("album").beginObject();
		writer.name("name").value(rowMap.get(ScrobbleInputColumns.ALBUM_NAME));
		writer.name("mbid").value(rowMap.get(ScrobbleInputColumns.ALBUM_MBID));
		writer.name("artist").beginObject();
		writer.name("name").value(rowMap.get(ScrobbleInputColumns.ALBUM_ARTIST_NAME));
		writer.name("mbid").value(rowMap.get(ScrobbleInputColumns.ALBUM_ARTIST_MBID));
		writer.endObject();
		writer.endObject();
		
		
		writer.endObject();
	}

}
