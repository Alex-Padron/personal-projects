import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import DataStructures.Path;
import Messages.MasterRequest;
import Messages.MasterResponse;
import Messages.Serializable;
import Messages.Bodies.AddrBody;
import Messages.Bodies.PathSetBody;
import Public.Master;

public class TestMaster {
    private DataOutputStream to_server;
    private BufferedReader from_server;

    public void master_req_put(String path_name, String hostname, int port) throws Exception {
    	assert(master_req_put(path_name, hostname, port, ""));
    }

    public boolean master_req_put(String path_name, String hostname, int port, String lock_code) throws Exception {
    Path p = new Path(path_name);
	MasterRequest req = new MasterRequest(p, hostname, port, lock_code);
	MasterResponse res = master_req(req);
	if (res.type.equals(MasterResponse.T.ACCEPT_UPDATE)) return true;
	if (res.type.equals(MasterResponse.T.REJECT_UPDATE)) return false;
	assert(false);
	return false;
    }

    public void master_req_remove(String path_name) throws Exception {
    	assert(master_req_remove(path_name, ""));
    }

    public boolean master_req_remove(String path_name, String lock_code) throws Exception {
	MasterRequest req =
	    new MasterRequest(new Path(path_name), lock_code);
	MasterResponse res = master_req(req);
	if (res.type.equals(MasterResponse.T.ACCEPT_UPDATE)) return true;
	if (res.type.equals(MasterResponse.T.REJECT_UPDATE)) return false;
	assert(false);
	return false;
    }

    public void master_req_get(String path_name, String hostname, int port) throws Exception {
    Path p = new Path(path_name);
	MasterRequest req =
	    new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, p);
	MasterResponse res = master_req(req);
	AddrBody a = Serializable.parse(res.body, AddrBody.class).get();
	assert(a.hostname.equals(hostname) && a.port == port);
    }

    public void master_req_get_i(String path_name) throws Exception {
    Path p = new Path(path_name);
	MasterRequest req =
	    new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, p);
	MasterResponse res = master_req(req);
	assert(res.type.equals(MasterResponse.T.NO_PUBLISHER_FOR_PATH));
    }

    public MasterResponse master_req(MasterRequest msg) throws IOException {
	to_server.writeBytes(msg.json() + "\n");
	String s = from_server.readLine();
	MasterResponse res = Serializable.parse_exn(s, MasterResponse.class);
	return res;
    }

    public Optional<Set<String>> master_paths_under(String path_name) throws Exception {
    	MasterRequest req = new MasterRequest(MasterRequest.T.GET_PATHS_UNDER, new Path(path_name));
    	to_server.writeBytes(req.json() + "\n");
    	String s = from_server.readLine();
    	MasterResponse res = Serializable.parse_exn(s, MasterResponse.class);
    	if (!res.validate()) return Optional.empty();
    	PathSetBody b = Serializable.parse_exn(res.body, PathSetBody.class);
    	return Optional.of(b.paths);
    }

    public void master_bad_string(String s) throws IOException {
    	to_server.writeBytes(s);
    	s = from_server.readLine();
    	MasterResponse res = Serializable.parse_exn(s, MasterResponse.class);
    	assert(res.type.equals(MasterResponse.T.INVALID_REQUEST));
    }

    @Test
    public void test() throws Exception {
	System.out.println("Testing Master...");
	int port = 8080;
	Master m = new Master(port);
	Thread t = new Thread(m);
	t.start();
	Socket socket = new Socket("localhost", port);
	to_server = new DataOutputStream(socket.getOutputStream());
	from_server =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));

	master_req_put("path1", "localhost", 1111);
	master_req_get_i("path2");
	master_req_put("path2", "localhost2", 2222);
	master_req_get("path2", "localhost2", 2222);
	master_req_put("path1", "localhost2", 2222);
	master_req_get("path1", "localhost2", 2222);
	master_req_remove("path1");
	master_req_get_i("path1");

	// Some malicious strings to try and kill the master server

	// doesn't parse
	master_bad_string("sdadadadadasdafsdfsgfgsfgdsfg\n");
	// register without required fields
	master_bad_string("{\"type\":\"REGISTER_PUBLISHER\",\"path\":\"path1\",\"hostname\":{},\"port\":{}}\n");
	master_bad_string("{\"type\":\"REGISTER_PUBLISHER\",\"path\":\"path1\",\"hostname\":{\"value\":\"localhost\"},\"port\":{}}\n");
	// remove with an extra field
	master_bad_string("{\"type\":\"REMOVE_PUBLISHER\",\"path\":\"path1\",\"hostname\":{\"value\":\"localhost\"},\"port\":{}}\n");

	socket.close();
	System.out.println("...passed");
    }

    @Test
    public void test2() throws Exception {
    	System.out.println("Testing Master Nested Paths...");
    	int port = 8079;
    	Master m = new Master(port);
    	Thread t = new Thread(m);
    	t.start();
    	Socket socket = new Socket("localhost", port);
    	to_server = new DataOutputStream(socket.getOutputStream());
    	from_server =
    	    new BufferedReader(new InputStreamReader(socket.getInputStream()));

    	master_req_put("foo", "l", 1111);
    	master_req_put("foo/bar", "l", 2222);
    	master_req_put("foo/foo", "l", 3333);
    	master_req_put("foo/baz", "l", 4444);
    	master_req_get("foo", "l", 1111);
    	master_req_get("foo/bar", "l", 2222);
    	master_req_get("foo/foo", "l", 3333);
    	master_req_get("foo/baz", "l", 4444);
    	Set<String> s = master_paths_under("foo").get();
    	assert(s.size() == 3);
    	assert(s.contains("bar"));
    	assert(s.contains("foo"));
    	assert(s.contains("baz"));

    	assert(master_paths_under("foo/bar").get().size() == 0);

    	socket.close();
    	System.out.println("...passed");
    }

    @Test
    public void test3() throws Exception {
    	System.out.println("Testing Master Closing...");
    	int port = 8078;
    	Master m = new Master(port);
    	Thread t = new Thread(m);
    	t.start();
    	Socket socket1 = new Socket("localhost", port);
    	DataOutputStream to_server1 =
    			new DataOutputStream(socket1.getOutputStream());
    	BufferedReader from_server1 =
    	    new BufferedReader(new InputStreamReader(socket1.getInputStream()));
    	to_server1.writeBytes("asdasdasd\n");
    	from_server1.readLine();
    	Socket socket2 = new Socket("localhost", port);
    	DataOutputStream to_server2 =
    			new DataOutputStream(socket2.getOutputStream());
    	BufferedReader from_server2 =
        	    new BufferedReader(new InputStreamReader(socket2.getInputStream()));
    	to_server2.writeBytes("saasfsdgfdsg\n");
    	from_server2.readLine();
    	assert(socket1.isBound());
    	assert(socket2.isBound());
    	m.close();
    	assert(from_server1.read() == -1);
    	assert(from_server2.read() == -1);
    	socket1.close();
    	socket2.close();
    }

    @Test
    public void test4() throws Exception {
    	System.out.println("Testing Master Locking Paths...");
    	int port = 8077;
    	Master m = new Master(port);
    	Thread t = new Thread(m);
    	t.start();

    	Socket socket = new Socket("localhost", port);
    	to_server = new DataOutputStream(socket.getOutputStream());
    	from_server =
    	    new BufferedReader(new InputStreamReader(socket.getInputStream()));

    	assert(master_req_put("a/b", "localhost", 1111, "locked"));
    	assert(!master_req_put("a/b", "localhost", 2222, "sdadad"));
    	master_req_get("a/b", "localhost", 1111);
    	assert(master_req_put("a/b", "localhost", 3333, "locked"));
    	master_req_get("a/b", "localhost", 3333);
    	assert(!master_req_remove("a/b", "asdasd"));
    	master_req_get("a/b", "localhost", 3333);
    	assert(master_req_remove("a/b", "locked"));
    	master_req_get_i("a/b");
    	assert(master_req_put("a/b", "localhost", 4444, "lock1"));
    	assert(master_req_put("a/c", "localhost", 5555, "lock2"));
    	assert(!master_req_put("a/b", "localhost", 4444, "lock2"));
    	assert(!master_req_put("a/c", "localhost", 5555, "lock1"));
    	master_req_get("a/b", "localhost", 4444);
    	master_req_get("a/c", "localhost", 5555);

    	socket.close();

    	System.out.println("...passed");
    }
}
