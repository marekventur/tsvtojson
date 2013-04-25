package de.marekventur.tsvtojson.processor.inputmodel;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

public class Timestamp {
	private final long timestamp;
	private final LocalDateTime dateTime;

	public Timestamp(long timestamp) {
		this.timestamp = timestamp;
		dateTime = new LocalDateTime(timestamp, DateTimeZone.UTC);
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public LocalDateTime getLocalDateTime() {
		return dateTime;
	}
	
	public String getFormatedDateTime() {
		return dateTime.toString(ISODateTimeFormat.dateHourMinuteSecond());
	}
}
