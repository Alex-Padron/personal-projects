package Messages;

import java.lang.reflect.Type;
import java.util.Optional;

import com.google.gson.Gson;

/**
 * Handles parsing to and from json strings. Uses a global
 * json parser, and provides methods to convert any class
 * or type to and from json.
 */
public abstract class Serializable {
    private static final Gson p = new Gson();

    /**
     * @return the json string for this object
     */
    public String json() {
	return p.toJson(this, this.getClass());
    }

    /**
     * @param s: to parse
     * @param c: class to parse into
     * @return the parsed object, of the type of the class that was passed in,
     * or none if the string did not parse correctly.
     */
    public static <T> Optional<T> parse(String s, Class<T> c) {
	try {
	    return Optional.of(parse_exn(s, c));
	} catch (Exception e) {
	    return Optional.empty();
	}
    }

    /**
     * @param s: to parse
     * @param t: class to parse into
     * @return the parsed object, of the type of the class that was passed in,
     * or none if the string did not parse correctly.
     */
    public static <T> Optional<T> parse(String s, Type t) {
	try {
	    return Optional.of(parse_exn(s, t));
	} catch (Exception e) {
	    return Optional.empty();
	}
    }

    /**
     * Parsing methods that throw the exception rather than returning an
     * optional
     */
    public static <T> T parse_exn(String s, Class<T> c) {
	return p.fromJson(s, c);
    }

    public static <T> T parse_exn(String s, Type t) {
	return p.fromJson(s, t);
    }
}
