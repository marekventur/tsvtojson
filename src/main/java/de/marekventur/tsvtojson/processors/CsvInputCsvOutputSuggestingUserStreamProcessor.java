package de.marekventur.tsvtojson.processors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVWriter;

public abstract class CsvInputCsvOutputSuggestingUserStreamProcessor<I extends CsvColumns, O extends CsvColumnsWithHeaderNames> extends CsvInputCsvOutputUserStreamProcessor<I, O> {

	protected static final String NULL_OUTPUT_VALUE = "";
	
	protected abstract void processRowWithSuggestion(Map<I, String> row, String[] suggestion, CSVWriter writer) throws IOException;
		
	
	private Map<O, I> suggestedColumMap = null;
	
	/**
	 * Returns an column suggestion lookup list or (in case it doesn't exist yet) lazily 
	 * creates it.
	 */
	private Map<O, I> getSuggestionIndexTable() {
		if (suggestedColumMap == null) {
			suggestedColumMap = new HashMap<O, I>();
			
			for (O outputColumn: getOutputColumns()) {				
				for (I inputColumn: getInputColumns()) {
					if (inputColumn.name().equals(outputColumn.name())) {
						suggestedColumMap.put(outputColumn, inputColumn);
						break;
					}
				}
			}
		}
		return suggestedColumMap;
	}
	
	@Override
	protected void processRow(Map<I, String> row, CSVWriter writer) throws IOException {
		String[] suggestion = new String[getOutputColumns().length];
		
		for (O outputColumn: getOutputColumns()) {
			String suggestedCell = row.get(getSuggestionIndexTable().get(outputColumn));
			
			if (suggestedCell == null) {
				suggestedCell = NULL_OUTPUT_VALUE;
			}
			
			suggestion[outputColumn.ordinal()] = suggestedCell;
		}
		
		processRowWithSuggestion(row, suggestion, writer);
	}

	

}
