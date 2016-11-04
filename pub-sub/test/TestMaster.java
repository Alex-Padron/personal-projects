import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.junit.Test;

import com.google.gson.Gson;

import Master.Master;
import Master.Paths.Path;
import Messages.MasterRequest;
import Messages.MasterResponse;

public class TestMaster {
    private DataOutputStream to_server;
    private BufferedReader from_server;
    private Gson parser;

    public void master_req_put(String path_name, String hostname, int port) throws Exception {
    Path p = new Path(path_name);
	MasterRequest req = new MasterRequest(p, hostname, port);
	MasterResponse des = new MasterResponse(MasterResponse.T.ACCEPT_UPDATE);
	master_req(req, des);
    }

    public void master_req_remove(String path_name) throws Exception {
    Path p = new Path(path_name);
	MasterRequest req =
	    new MasterRequest(MasterRequest.T.REMOVE_PUBLISHER, p);
	MasterResponse des = new MasterResponse(MasterResponse.T.ACCEPT_UPDATE);
	master_req(req, des);
    }

    public void master_req_get(String path_name, String hostname, int port) throws Exception {
    Path p = new Path(path_name);
	MasterRequest req =
	    new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, p);
	MasterResponse des = new MasterResponse(hostname, port);
	master_req(req, des);
    }

    public void master_req_get_i(String path_name) throws Exception {
    Path p = new Path(path_name);
	MasterRequest req =
	    new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, p);
	MasterResponse des =
	    new MasterResponse(MasterResponse.T.NO_PUBLISHER_FOR_PATH);
	master_req(req, des);
    }

    public void master_req(MasterRequest msg, MasterResponse desired) throws IOException {
	to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
	String s = from_server.readLine();
	MasterResponse res = parser.fromJson(s, MasterResponse.class);
	assert(desired.equals(res));
    }

    public void master_bad_string(String s) throws IOException {
    	to_server.writeBytes(s);
    	s = from_server.readLine();
    	MasterResponse res = parser.fromJson(s, MasterResponse.class);
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
	parser = new Gson();

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
}
