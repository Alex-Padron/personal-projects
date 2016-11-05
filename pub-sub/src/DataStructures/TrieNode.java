package DataStructures;

import java.util.HashMap;
import java.util.Map;

/*
 * Node in a trie. Names of nodes are stored in the parents -
 * each node has a map from names to children.
 * [is_terminal] says whether this node has a value at it, so that it
 * is possible to determine whether internal nodes represent paths.
 */
public class TrieNode<T> {
    public Map<String, TrieNode<T>> children;
    public TrieNode<T> parent;
    public T value;
    public String name;
    public boolean is_terminal;

    public TrieNode(T value) {
	this.value = value;
	this.is_terminal = true;
	this.children = new HashMap<>();
    }

    public TrieNode() {
	this.value = null;
	this.is_terminal = false;
	this.children = new HashMap<>();
    }
}
