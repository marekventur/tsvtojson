package de.marekventur.tsvtojson.processors;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.EnumSet;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public abstract class CsvInputCsvOutputUserStreamProcessor<I extends CsvColumns, O extends CsvColumnsWithHeaderNames> extends CsvInputUserStreamProcessor<I> implements UserStreamProcessor {

	private CSVWriter writer;
	
	protected abstract O[] getOutputColumns();
	
	protected abstract void processRow(Map<I, String> row, CSVWriter writer) throws IOException;
	
	@Override
	protected void processRow(Map<I, String> row, OutputStream output)
			throws IOException {
		processRow(row, writer);
	}
	
	@Override
	public void process(InputStream input, OutputStream output)
			throws IOException {
		writer = new CSVWriter(new OutputStreamWriter(new BufferedOutputStream(output)), '\t', '"');
		
		try {
			String[] header = new String[getOutputColumns().length];
			for (O column: getOutputColumns()) {
				header[column.ordinal()] = column.getHeaderName();
			}
			writer.writeNext(header);
				
			super.process(input, output);
		} finally {
			writer.close();
		}
	}

}
