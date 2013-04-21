package de.marekventur.tsvtojson;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.marekventur.tsvtojson.UserDataCollectionPool.UserDataCollectionEntry;

public class UserDataCollectionPoolTest {

	UserDataCollectionPool pool;
	
	@Before 
	public void setup() {
		pool = new UserDataCollectionPool();
	}
	
	@After 
	public void tearDown() throws IOException {
		for (UserDataCollectionEntry entry: pool) {
			entry.getUserDataCollection().close();
			entry.getUserDataCollection().getOutputFile().deleteOnExit();
		}
	}
	
	@Test
	public void test() throws IOException {
		assertEquals(pool.get(1), pool.get(1));
		assertNotEquals(pool.get(1), pool.get(2));
	}

}
