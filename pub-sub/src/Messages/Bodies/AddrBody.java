package Messages.Bodies;

import Messages.Serializable;

public class AddrBody extends Serializable {
    public final String hostname;
    public final int port;

    public AddrBody(String hostname, int port) {
	this.hostname = hostname;
	this.port = port;
    }
}
