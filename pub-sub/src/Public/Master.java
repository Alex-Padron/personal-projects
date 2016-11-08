package Public;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Optional;
import java.util.Set;

import DataStructures.publisherData.PublisherPaths;
import Messages.MasterRequest;
import Messages.MasterResponse;
import Messages.Serializable;
import Messages.Bodies.PathBody;
import Messages.Bodies.RemovePathBody;
import Messages.Bodies.NewPathBody;
import Server.MultiClientServer;

public class Master extends MultiClientServer<MasterRequest, MasterResponse> {
    private PublisherPaths data;

    public Master(int port) throws IOException {
	this.server_socket = new ServerSocket();
	this.server_socket.setReuseAddress(true);
	this.server_socket.bind(new InetSocketAddress(port));
	this.data = new PublisherPaths();
	this.server_name = "MASTER";
    }

    @Override
    protected Optional<MasterRequest> parse_client_string(String s) {
	Optional<MasterRequest> req = Serializable.parse(s, MasterRequest.class);
	if ((!req.isPresent()) || (!req.get().validate()))
	    return Optional.empty();
	return req;
    }

    @Override
    protected MasterResponse handle_request(MasterRequest req) {
        switch (req.type) {
	case REGISTER_PUBLISHER: {
	    NewPathBody body = Serializable.parse_exn(req.body, NewPathBody.class);
	    this.data.add(body.path, body.hostname, body.port, body.lock_code);
	    return accept_update_response();
	}
	case REMOVE_PUBLISHER: {
	    RemovePathBody body = Serializable.parse_exn(req.body, RemovePathBody.class);
	    this.data.remove(body.path, body.lock_code);
	    return accept_update_response();
	}
	case GET_PATHS_UNDER: {
	    PathBody body = Serializable.parse_exn(req.body, PathBody.class);
	    return paths_response(this.data.get_paths_under(body.path));
	}
	case GET_PUBLISHER_OF_PATH: {
	    PathBody body = Serializable.parse_exn(req.body, PathBody.class);
	    Optional<InetSocketAddress> query_response = data.get(body.path);
	    if (query_response.isPresent()) {
		return filled_query_response(query_response.get());
	    }
	    return empty_query_response();
	}
	default:
	    return invalid_request();
	}
    }

    @Override
    protected MasterResponse invalid_request() {
	return new MasterResponse(MasterResponse.T.INVALID_REQUEST);
    }

    private MasterResponse accept_update_response() {
	return new MasterResponse(MasterResponse.T.ACCEPT_UPDATE);
    }

    private MasterResponse filled_query_response(InetSocketAddress addr) {
	return new MasterResponse(addr.getHostName(), addr.getPort());
    }

    private MasterResponse empty_query_response() {
	return new MasterResponse(MasterResponse.T.NO_PUBLISHER_FOR_PATH);
    }

    private MasterResponse paths_response(Set<String> paths) {
	return new MasterResponse(paths);
    }
}
