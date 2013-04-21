package de.marekventur.tsvtojson.processors.scrobbles;

import org.joda.time.LocalDateTime;

public interface JsonFileNamingStrategy {
	public String getName(LocalDateTime dateTime);
}
