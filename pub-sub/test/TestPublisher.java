import org.junit.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Master.Master;
import Master.MasterClient;
import Messages.PublisherRequest;
import Messages.PublisherResponse;
import Publisher.Publisher;
import java.lang.reflect.Type;

public class TestPublisher {
    DataOutputStream to_server;
    BufferedReader from_server;
    Type msg_type;
    Gson parser;

    public void pub_get_value(String path_name, int value) throws IOException {
	PublisherRequest msg =
	    new PublisherRequest(PublisherRequest.T.GET_PATH_VALUE, path_name);
	PublisherResponse<Integer> des = new PublisherResponse<>(value);
	publisher_req(msg, des);
    }

    public void pub_get_value_i(String path_name) throws IOException {
	PublisherRequest msg =
	    new PublisherRequest(PublisherRequest.T.GET_PATH_VALUE, path_name);
	PublisherResponse<Integer> des =
	    new PublisherResponse<>(PublisherResponse.T.NOT_PUBLISHING_PATH);
	publisher_req(msg, des);
    }

    public void pub_is_publishing(String path_name) throws IOException {
	PublisherRequest msg =
	    new PublisherRequest(PublisherRequest.T.QUERY_PUBLISHING_PATH, path_name);
	PublisherResponse<Integer> des =
	    new PublisherResponse<>(PublisherResponse.T.AM_PUBLISHING_PATH);
	publisher_req(msg, des);
    }

    public void pub_not_publishing(String path_name) throws IOException {
	PublisherRequest msg =
	    new PublisherRequest(PublisherRequest.T.QUERY_PUBLISHING_PATH, path_name);
	PublisherResponse<Integer> des =
	    new PublisherResponse<>(PublisherResponse.T.NOT_PUBLISHING_PATH);
	publisher_req(msg, des);
    }

    public void publisher_req(PublisherRequest msg,
			      PublisherResponse<Integer> desired) throws IOException {
	to_server.writeBytes(parser.toJson(msg, PublisherRequest.class) + "\n");
	String s = from_server.readLine();
	PublisherResponse<Integer> res = parser.fromJson(s, msg_type);
	assert(desired.equals(res));
    }

    public void pub_bad_string(String s) throws IOException {
    	to_server.writeBytes(s);
    	s = from_server.readLine();
    	PublisherResponse<Integer> res = parser.fromJson(s, msg_type);
    	assert(res.type.equals(PublisherResponse.T.INVALID_REQUEST));
    }
    @Test
    public void test() throws IOException {
	System.out.println("Testing Publisher...");
	int port = 8100;
	String master_hostname = "localhost";
	int master_port = 8101;
	parser = new Gson();
	msg_type = new TypeToken<PublisherResponse<Integer>>(){}.getType();

	Master ms = new Master(master_port);
	Thread t1 = new Thread(ms);
	t1.start();

	Map<String, Integer> paths = new HashMap<>();
	paths.put("path1", 1);
	paths.put("path2", 2);

	Publisher<Integer> p =
	    new Publisher<>(port,master_hostname, master_port, paths);
	Thread t2 = new Thread(p);
	t2.start();

	Socket socket = new Socket("localhost", port);
	to_server =
	    new DataOutputStream(socket.getOutputStream());
	from_server =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));

	System.out.println("Without Master...");

	pub_get_value("path1", 1);

	p.put_path("path3", 3);

	pub_get_value("path3", 3);
	pub_get_value_i("path4");
	pub_get_value("path2", 2);

	p.remove_path("path2");

	pub_get_value_i("path2");
	pub_is_publishing("path1");
	pub_not_publishing("path2");

	System.out.println("With Master...");

	MasterClient mc = new MasterClient("localhost", master_port);
	p.send_paths_to_master();
	assert(mc.get_path_addr("path1").get().getPort() == port);
	assert(!mc.get_path_addr("path2").isPresent());
	assert(mc.get_path_addr("path3").get().getPort() == port);

	p.put_path("path2", 1);
	p.send_paths_to_master();

	assert(mc.get_path_addr("path2").get().getPort() == port);

	p.remove_path("path3");
	p.send_paths_to_master();

	assert(!mc.get_path_addr("path3").isPresent());
	
	// some malicious strings
	
	pub_bad_string("sadadadadad\n");
	pub_bad_string("{\"type\":\"GET_PATH_VALUE\",\"path\":\"\"}\n");
	socket.close();
	System.out.println("...passed");
    }
}
