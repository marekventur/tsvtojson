package de.marekventur.tsvtojson.processor.inputmodel;

public class Artist {
	private final String name;
	private final String mbid;
	
	public Artist(String name, String mbid) {
		super();
		this.name = name;
		this.mbid = mbid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMbid() {
		return mbid;
	}
	
}
