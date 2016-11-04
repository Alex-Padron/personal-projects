package Master.Trie;

import java.util.Optional;

import Master.Paths.Path;

/**
 * Implementation of a trie with keys storing type T
 */
public class Trie<T> {
    public TrieNode<T> head;

    public Trie() {
	this.head = new TrieNode<T>();
    }

    /*
     * @param path: path name to insert under
     * @param value: to insert
     */
    public void insert(Path path, T value) {
	String[] components = path.get_components();
	TrieNode<T> current = this.head;
	for (int i = 0; i < components.length - 1; i++) {
	    String component = components[i];
	    current = insert_if_not_present(current, component);
	}
	insert_data_node(current, components[components.length - 1], value);
    }

    /*
     * @param path: to query
     * @return value if there is one for [path], otherwise empty
     */
    public Optional<T> get(Path path) {
	TrieNode<T> target = navigate_to(path);
	if (target == null || (!target.is_terminal)) return Optional.empty();
	return Optional.of(target.value);
    }

    /*
     * Removes a value from the trie and clears all uneeded nodes
     * after removing.
     * @param path: to remove value from
     */
    public void remove(Path path) {
	TrieNode<T> to_remove = navigate_to(path);
	if (to_remove == null) return;
	to_remove.value = null;
	to_remove.is_terminal = false;
	if (to_remove.children.size() > 0) return;
	filter_upwards(to_remove);
    }


    // clears out uneeded nodes from current upwards to the root
    private void filter_upwards(TrieNode<T> current) {
	TrieNode<T> parent = current.parent;
	while (current.children.size() == 0 &&
	       parent != null &&
	       (!current.is_terminal)) {
	    parent.children.values().remove(current);
	    current = parent;
	    parent = parent.parent;
	}
    }

    // get a reference to the node at the given path, or null
    // if there isn't one
    private TrieNode<T> navigate_to(Path path) {
	TrieNode<T> current = this.head;
	String[] components = path.get_components();
	for (int i = 0; i < components.length; i++) {
	    String component = components[i];
	    if (!current.children.containsKey(component)) return null;
	    current = current.children.get(component);
	}
	return current;
    }

    private TrieNode<T> insert_if_not_present (TrieNode<T> target, String name) {
	if (target.children.containsKey(name)) {
	    return target.children.get(name);
	}
        TrieNode<T> child = new TrieNode<>();
	target.children.put(name, child);
	child.parent = target;
	return child;
    }

    private void insert_data_node (TrieNode<T> target, String name, T value) {
	TrieNode<T> child = insert_if_not_present(target, name);
	child.is_terminal = true;
	child.value = value;
    }
}
