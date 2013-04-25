package de.marekventur.tsvtojson.processor.inputmodel;

public class Album {
	private final String name;
	private final String mbid;
	private final Artist artist;
	
	public Album(String name, String mbid, Artist artist) {
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
