package Messages.Bodies;

import Messages.Serializable;

public class ValueBody<T>  extends Serializable {
	public final T value;
	
	public ValueBody(T value) {
		this.value = value;
	}
}
