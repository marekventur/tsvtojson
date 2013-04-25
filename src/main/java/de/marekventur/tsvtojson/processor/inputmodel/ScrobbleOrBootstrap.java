package de.marekventur.tsvtojson.processor.inputmodel;

public class ScrobbleOrBootstrap {
	private final Timestamp timestamp;
	private final int bootstrapCount;
	private final String applicationName;
	private final Track track;
	private final Track uncorrectedTrack;
	private final Album album;
	
	public ScrobbleOrBootstrap(Timestamp timestamp, int bootstrapCount,
			String applicationName, Track track, Track uncorrectedTrack,
			Album album) {
		this.timestamp = timestamp;
		this.bootstrapCount = bootstrapCount;
		this.applicationName = applicationName;
		this.track = track;
		this.uncorrectedTrack = uncorrectedTrack;
		this.album = album;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public int getBootstrapCount() {
		return bootstrapCount;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public Track getTrack() {
		return track;
	}

	public Track getUncorrectedTrack() {
		return uncorrectedTrack;
	}

	public Album getAlbum() {
		return album;
	}
	
	public boolean isBootstrap() {
		return bootstrapCount > 0;
	}
	
	public boolean hasCorrection() {
		return uncorrectedTrack != null;
	}
}
