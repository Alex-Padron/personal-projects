package DataStructures.publisherData;

/*
 * Data for a publisher in the master. Includes the address and a refcount
 * to know when to remove the publisher if it is no longer needed
 */
public class PublisherNode {
    public int refcount;
    public final String hostname;
    public final int port;

    public PublisherNode(String hostname, int port) {
	this.hostname = hostname;
	this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!PublisherNode.class.isAssignableFrom(obj.getClass())) return false;
	PublisherNode other = (PublisherNode) obj;
	return other.hostname.equals(hostname) && other.port == port;
    }

    @Override
    public int hashCode() {
	return refcount + port + hostname.hashCode();
    }
}
