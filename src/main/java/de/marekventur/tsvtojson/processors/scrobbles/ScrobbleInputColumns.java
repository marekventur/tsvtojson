package de.marekventur.tsvtojson.processors.scrobbles;

import de.marekventur.tsvtojson.processors.CsvColumns;

public enum ScrobbleInputColumns implements CsvColumns {
	USER_ID,
	UNIXTIME,
	UNCORRECTED_TRACK_NAME,
	UNCORRECTED_TRACK_MBID,
	UNCORRECTED_ARTIST_NAME,	
	UNCORRECTED_ARTIST_MBID,
	TRACK_NAME,
	TRACK_MBID,
	ARTIST_NAME,
	ARTIST_MBID,
	ALBUM_NAME,
	ALBUM_MBID,	
	ALBUM_ARTIST_NAME,	
	ALBUM_ARTIST_MBID,
	BOOTSTRAP_COUNT,
	APPLICATION;	
}