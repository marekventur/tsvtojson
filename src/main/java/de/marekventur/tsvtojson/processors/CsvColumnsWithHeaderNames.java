package de.marekventur.tsvtojson.processors;

public interface CsvColumnsWithHeaderNames extends CsvColumns {
	public abstract String getHeaderName();
}
