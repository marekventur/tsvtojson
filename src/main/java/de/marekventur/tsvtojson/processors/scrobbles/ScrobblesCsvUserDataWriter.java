package de.marekventur.tsvtojson.processors.scrobbles;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.EnumMap;

import javax.sql.DataSource;

import au.com.bytecode.opencsv.CSVWriter;
import de.marekventur.tsvtojson.DataCollection;
import de.marekventur.tsvtojson.processor.inputmodel.ScrobbleOrBootstrap;
import de.marekventur.tsvtojson.processors.CsvColumnsWithHeaderNames;
import de.marekventur.tsvtojson.processors.CsvUserDataWriter;
import de.marekventur.tsvtojson.processors.UserDataWriter;

public class ScrobblesCsvUserDataWriter extends CsvUserDataWriter<ScrobbleOrBootstrap, ScrobblesCsvUserDataWriter.OutputColumns> {

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
	
	protected ScrobblesCsvUserDataWriter(DataCollection dataCollection) {
		super(dataCollection);
	}
		
	@Override
	protected String getPath(ScrobbleOrBootstrap row) {
		return row.isBootstrap() ? "tsv/bootstraps.tsv" : "tsv/scrobbles.tsv";
	}


	@Override
	protected void processRow(ScrobbleOrBootstrap row, CSVWriter writer) {
		String[] resultRow = new String[getOutputColumns().length];
		
			
		writer.writeNext(resultRow);
	}

	@Override
	protected OutputColumns[] getOutputColumns() {
		return OutputColumns.values();
	}

	@Override
	protected EnumMap<OutputColumns, String> convertRowToCsv(
			ScrobbleOrBootstrap input) {
		
		EnumMap<OutputColumns, String> result = new EnumMap<OutputColumns, String>(OutputColumns.class);
		
		result.put(OutputColumns.UNIXTIME, String.valueOf(input.getTimestamp().getTimestamp()));
		result.put(OutputColumns.DATETIME, input.getTimestamp().getFormatedDateTime());
		
		
		return result;
	}

}
