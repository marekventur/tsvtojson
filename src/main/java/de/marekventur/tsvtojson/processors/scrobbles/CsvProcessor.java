package de.marekventur.tsvtojson.processors.scrobbles;

import java.io.IOException;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import au.com.bytecode.opencsv.CSVWriter;

import de.marekventur.tsvtojson.processors.CsvColumnsWithHeaderNames;
import de.marekventur.tsvtojson.processors.CsvInputCsvOutputSuggestingUserStreamProcessor;
import de.marekventur.tsvtojson.processors.CsvInputCsvOutputUserStreamProcessor;
import de.marekventur.tsvtojson.processors.UserStreamProcessor;

/**
 * Adds header, denormalizes bootstrapped tracks
 */
public class CsvProcessor extends CsvInputCsvOutputSuggestingUserStreamProcessor<ScrobbleInputColumns, CsvProcessor.OutputColumns> implements UserStreamProcessor {
	
	public enum OutputColumns implements CsvColumnsWithHeaderNames {
		DATETIME("datetime"),
		UNIXTIME("unixtime"),
		TRACK_NAME("track"),
		TRACK_MBID("track mbid"),
		ARTIST_NAME("artist"),
		ARTIST_MBID("artist mbid"),
		ALBUM_NAME("album"),
		ALBUM_MBID("album mbid"),	
		ALBUM_ARTIST_NAME("album artist"),	
		ALBUM_ARTIST_MBID("album artist mbid"),
		UNCORRECTED_TRACK_NAME("track (uncorrected)"),
		UNCORRECTED_TRACK_MBID("track mbid (uncorrected)"),
		UNCORRECTED_ARTIST_NAME("artist (uncorrected)"),
		UNCORRECTED_ARTIST_MBID("artist mbid (uncorrected)"),
		APPLICATION("application name"),
		BOOTSTRAPPED("imported");		
		
		private final String headerName;
		
		private OutputColumns(String headerName) {
			this.headerName = headerName;
		}

		public String getHeaderName() {
			return headerName;
		}
	}
	
	@Override
	protected OutputColumns[] getOutputColumns() {
		return OutputColumns.values();
	}


	@Override
	protected ScrobbleInputColumns[] getInputColumns() {
		return ScrobbleInputColumns.values();
	}


	@Override
	protected void processRowWithSuggestion(Map<ScrobbleInputColumns, String> row, String[] suggestion,
			CSVWriter writer) throws IOException {
		
		long timestamp = Long.valueOf(row.get(ScrobbleInputColumns.UNIXTIME)) * 1000;
		LocalDateTime dateTime = new LocalDateTime(timestamp, DateTimeZone.UTC);
		suggestion[OutputColumns.DATETIME.ordinal()] = dateTime.toString(ISODateTimeFormat.dateHourMinuteSecond());
		
		if (row.get(ScrobbleInputColumns.TRACK_NAME) == null) {
			suggestion[OutputColumns.TRACK_NAME.ordinal()] = row.get(ScrobbleInputColumns.UNCORRECTED_TRACK_NAME);
			suggestion[OutputColumns.TRACK_MBID.ordinal()] = row.get(ScrobbleInputColumns.UNCORRECTED_TRACK_MBID);
			suggestion[OutputColumns.ARTIST_NAME.ordinal()] = row.get(ScrobbleInputColumns.UNCORRECTED_ARTIST_NAME);
			suggestion[OutputColumns.ARTIST_MBID.ordinal()] = row.get(ScrobbleInputColumns.UNCORRECTED_ARTIST_MBID);
		}
		
		int bootstrapCount = Integer.valueOf(row.get(ScrobbleInputColumns.BOOTSTRAP_COUNT));
		if (bootstrapCount == 0) {
			suggestion[OutputColumns.BOOTSTRAPPED.ordinal()] = "false";
			writer.writeNext(suggestion);
		} else {
			suggestion[OutputColumns.BOOTSTRAPPED.ordinal()] = "true";
			for (int i = 0; i < bootstrapCount; i++) {
				writer.writeNext(suggestion);
			}
		}
	}

}

