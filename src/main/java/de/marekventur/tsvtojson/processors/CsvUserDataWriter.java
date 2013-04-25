package de.marekventur.tsvtojson.processors;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.EnumMap;

import de.marekventur.tsvtojson.DataCollection;

import au.com.bytecode.opencsv.CSVWriter;

public abstract class CsvUserDataWriter<I, C extends Enum<C> & CsvColumnsWithHeaderNames> extends UserDataWriter<I, CSVWriter> {

	protected CsvUserDataWriter(DataCollection dataCollection) {
		super(dataCollection);
	}
	
	abstract protected C[] getOutputColumns();

	@Override
	protected CSVWriter openWriter(OutputStream outputStream)
			throws IOException {
		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream));
		
		String[] header = new String[getOutputColumns().length];
		for (C column : getOutputColumns()) {
			header[column.ordinal()] = column.getHeaderName();
		}
		csvWriter.writeNext(header);
		
		return csvWriter;
	}

	@Override
	protected final void closeWriter(CSVWriter writer) throws IOException {
		writer.close();		
	}
	
	@Override
	protected void processRow(I row, CSVWriter writer) {
		EnumMap<C, String> resultMap = convertRowToCsv(row);
		
		String[] result = new String[getOutputColumns().length];
		for (C column: getOutputColumns()) {
			result[column.ordinal()] = resultMap.get(column);
		}
		
		writer.writeNext(result);
		
	}

	protected abstract EnumMap<C, String> convertRowToCsv(I input);

}
