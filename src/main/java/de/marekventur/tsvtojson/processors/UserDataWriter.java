package de.marekventur.tsvtojson.processors;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import de.marekventur.tsvtojson.DataCollection;

public abstract class UserDataWriter<R, W> implements Closeable {
	
	private W writer = null;
	private String lastPath = null;
	private final DataCollection dataCollection;
	
	protected abstract String getPath(R row);
	protected abstract W openWriter(OutputStream outputStream) throws IOException;
	protected abstract void closeWriter(W writer) throws IOException;
	protected abstract void processRow(R row, W writer);
	
	protected UserDataWriter(DataCollection dataCollection) {
		this.dataCollection = dataCollection;
	}
	
	public final void processRow(R row) throws IOException {
		String thisPath = getPath(row);
		if (thisPath.equals(lastPath)) {
			lastPath = thisPath;
			if (writer != null) {
				closeWriter(writer);
			}
			writer = openWriter(dataCollection.getOutputStream(thisPath));
		}
		
		processRow(row, writer);
	}
	
	public void close() throws IOException {
		if (lastPath != null) {
			closeWriter(writer);
		}
	}
}
