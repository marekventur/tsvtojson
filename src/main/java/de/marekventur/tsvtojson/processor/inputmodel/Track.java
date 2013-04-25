package de.marekventur.tsvtojson.processor.inputmodel;

public class Track {
	private final String name;
	private final String mbid;
	private final Artist artist;
	
	public Track(String name, String mbid, Artist artist) {
		super();
		this.name = name;
		this.mbid = mbid;
		this.artist = artist;
	}

	public String getName() {
		return name;
	}
	
	public String getMbid() {
		return mbid;
	}

	public Artist getArtist() {
		return artist;
	}

}
