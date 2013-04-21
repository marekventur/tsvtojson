package de.marekventur.tsvtojson.processors.scrobbles;

import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

public class MonthlyJsonFileNamingStrategy implements JsonFileNamingStrategy {

	public String getName(LocalDateTime dateTime) {
		return "scrobbles-" + dateTime.toString(ISODateTimeFormat.yearMonth());
	}

}
