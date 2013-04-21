package de.marekventur.tsvtojson.processors;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import au.com.bytecode.opencsv.CSVWriter;

public abstract class CsvInputCsvOutputUserStreamProcessor<I extends CsvColumns, O extends CsvColumnsWithHeaderNames>
		extends CsvInputUserStreamProcessor<I> implements UserStreamProcessor {

	private boolean first = true;

	protected abstract O[] getOutputColumns();

	protected abstract void processRow(Map<I, String> row, CSVWriter writer)
			throws IOException;

	@Override
	protected void processRow(Map<I, String> row, OutputStream output)
			throws IOException {
		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(output), '\t', '"');
		
		if (first) {
			first = false;
			String[] header = new String[getOutputColumns().length];
			for (O column : getOutputColumns()) {
				header[column.ordinal()] = column.getHeaderName();
			}
			csvWriter.writeNext(header);
		}
		processRow(row, csvWriter);
		
		csvWriter.close();
	}

}
