package DataStructures;

import java.net.InetSocketAddress;

/*
 * Data for a publisher in the master. Includes the address and a refcount
 * to know when to remove the publisher if it is no longer needed
 */
public class PublisherNode {
    public int refcount;
    public final InetSocketAddress addr;

    public PublisherNode(InetSocketAddress addr) {
	this.addr = addr;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!PublisherNode.class.isAssignableFrom(obj.getClass())) return false;
	PublisherNode other = (PublisherNode) obj;
	return other.addr.equals(this.addr);
    }

    @Override
    public int hashCode() {
	return refcount + addr.hashCode();
    }
}
