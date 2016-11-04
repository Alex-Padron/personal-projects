package Master.Paths;

import java.net.InetSocketAddress;

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
	return other.addr == this.addr;
    }
}
