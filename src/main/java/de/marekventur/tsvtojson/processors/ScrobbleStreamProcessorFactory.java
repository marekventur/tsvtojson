package de.marekventur.tsvtojson.processors;

import java.util.ArrayList;
import java.util.List;

import de.marekventur.tsvtojson.Types;
import de.marekventur.tsvtojson.processors.scrobbles.CsvProcessor;

public class ScrobbleStreamProcessorFactory {
	public List<UserStreamProcessor> create(Types type) {
		List<UserStreamProcessor> result = new ArrayList<UserStreamProcessor>();
		
		switch(type) {
			case SCROBBLES: 
				result.add(new CsvProcessor());
				break;
			default:
			    throw new RuntimeException("type {} not defined in ScrobbleStreamProcessorFactory");
		}
		
		return result;
	}
}
