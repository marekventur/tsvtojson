package de.marekventur.tsvtojson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

public class UserDataCollectionPool implements
		Iterable<UserDataCollectionPool.UserDataCollectionEntry> {

	public class UserDataCollectionEntry {
		private final int userId;
		private final UserDataCollection userDataCollection;

		public UserDataCollectionEntry(int userId,
				UserDataCollection userDataCollection) {
			this.userId = userId;
			this.userDataCollection = userDataCollection;
		}

		public int getUserId() {
			return userId;
		}

		public UserDataCollection getUserDataCollection() {
			return userDataCollection;
		}
	}

	private final Map<Integer, UserDataCollection> pool = new HashMap<Integer, UserDataCollection>();

	public UserDataCollection get(int userId) throws IOException {
		UserDataCollection userDataCollection = pool.get(userId);
		if (userDataCollection == null) {
			userDataCollection = new UserDataCollection();
			pool.put(userId, userDataCollection);
		}
		return userDataCollection;
	}

	public Iterator<UserDataCollectionEntry> iterator() {
		Function<Entry<Integer, UserDataCollection>, UserDataCollectionEntry> iteratorConverter = new Function<Entry<Integer, UserDataCollection>, UserDataCollectionEntry>() {
		    public UserDataCollectionEntry apply(Entry<Integer, UserDataCollection> input) {
		        return new UserDataCollectionEntry(input.getKey(), input.getValue());
		    }
		};
		return Iterators.transform(pool.entrySet().iterator(), iteratorConverter);
	}
}
