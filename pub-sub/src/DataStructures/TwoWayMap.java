package DataStructures;

import java.util.HashMap;

/*
 * KV map that allows querying from key to value and vice
 * versa in O(1). Both keys and values must be distinct
 */
public class TwoWayMap<K,V> {
    HashMap<K,V> k_to_v;
    HashMap<V,K> v_to_k;

    public TwoWayMap() {
	this.k_to_v = new HashMap<>();
	this.v_to_k = new HashMap<>();
    }

    public void insert(K key, V value) {
	k_to_v.put(key, value);
	v_to_k.put(value, key);
    }

    public K get_k(V value) {
	return v_to_k.get(value);
    }

    public V get_v(K key) {
	return k_to_v.get(key);
    }

    public void remove_k(K key) {
	V value = k_to_v.get(key);
	k_to_v.remove(key);
	v_to_k.remove(value);
    }

    public void remove_v(V value) {
	K key = v_to_k.get(value);
	k_to_v.remove(key);
	v_to_k.remove(value);
    }

    public boolean contains_k(K key) {
	return k_to_v.containsKey(key);
    }

    public boolean contains_v(V value) {
	return v_to_k.containsKey(value);
    }
}
