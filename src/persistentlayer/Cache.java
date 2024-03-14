package persistentlayer;

public interface Cache<K, V> {
	void set(K key, V value);

	V get(K key);

	default void put(K key, V value) {
		set(key, value);
	}

	void remove(K key);
}
