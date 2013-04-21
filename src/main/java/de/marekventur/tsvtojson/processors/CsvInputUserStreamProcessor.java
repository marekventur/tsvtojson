package de.marekventur.tsvtojson.processors;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import de.marekventur.tsvtojson.DataCollection;

import au.com.bytecode.opencsv.CSVReader;

public abstract class CsvInputUserStreamProcessor<I extends CsvColumns> extends SingleOutputUserStreamProcessor {

	protected static final String NULL_INPUT_VALUE = "NULL";
	
	protected abstract I[] getInputColumns();
	protected abstract void processRow(Map<I, String> row, OutputStream output) throws IOException;
	
	public void process(InputStream input, OutputStream output) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(input), '\t', '"');
		OutputStream outputStream = output;
		
		try {
			String[] row;
			while ((row = reader.readNext()) != null) {
				Map<I, String> rowMap = new HashMap<I, String>();
				for (I column: getInputColumns()) {
					String cell = row[column.ordinal()];
					if (NULL_INPUT_VALUE.equals(cell)) {
						cell = null;
					}
					rowMap.put(column, cell);
				}
				processRow(rowMap, outputStream);
		    }
		} finally {
			reader.close();
		}
	}
	
	public void close() throws IOException { }
}
