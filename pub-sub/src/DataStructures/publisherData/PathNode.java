package DataStructures.publisherData;

/**
 * Representation for a single node in the trie representing paths
 */
public class PathNode {
	public final int publisher_id;
	public final String lock_code;
	
	public PathNode(int publisher_id, String lock_code) {
		this.publisher_id = publisher_id;
		this.lock_code = lock_code;
	}

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!PathNode.class.isAssignableFrom(obj.getClass())) return false;
	PathNode other = (PathNode) obj;
	return other.publisher_id == this.publisher_id && other.lock_code.equals(this.lock_code);
    }

    @Override
    public int hashCode() {
	return publisher_id + 37 * lock_code.hashCode();
    }
}
