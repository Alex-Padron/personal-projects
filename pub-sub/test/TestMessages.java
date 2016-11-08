import java.lang.reflect.Type;

import org.junit.Test;

import com.google.gson.reflect.TypeToken;

import Messages.Bodies.ValueBody;
import Messages.PublisherResponse.T;
import Messages.PublisherResponse;
import Messages.Serializable;

public class TestMessages {

	@Test
	public void test() throws Exception {
		System.out.println("Testing Messages and Serializable...");
		PublisherResponse<Integer> req = new PublisherResponse<>(5);
		Type resp_type = new TypeToken<PublisherResponse<T>>(){}.getType();
		Type value_type = new TypeToken<ValueBody<Integer>>(){}.getType();
		String s = req.json();
		PublisherResponse<Integer> res = Serializable.parse_exn(s, resp_type);
		ValueBody<Integer> vb = Serializable.parse_exn(res.body, value_type);
		assert(vb.value == 5);
		assert(res.type.equals(PublisherResponse.T.VALUE_RESPONSE));
		System.out.println("...passed");
	}

}
