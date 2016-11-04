package Master.Paths;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Paths that can be used to publish and subscribe. Similar to directory
 * paths, they are represented as arrays of string components
 */
public class Path {
    private final String components[];

    /*
     * @param path_name: to be converted into a path
     * @throws: exception if the path name does not match
     * the pattern (in the validate method)
     */
    public Path(String path_name) throws Exception {
	validate(path_name);
	components = path_name.split("/");
    }

    public String[] get_components() {
	return copy_over_array(components.length);
    }

    public String[] get_prefix() {
	return copy_over_array(components.length - 1);
    }

    public String get_suffix() {
	return components[components.length - 1];
    }

    public int length() {
	return components.length;
    }

    @Override
    public String toString() {
	String s = "";
	for (int i = 0; i < components.length; i++) {
	    s += components[i];
	    if (i < components.length - 1) {
		s += "/";
	    }
	}
	return s;
    }

    public Path append(Path other) throws Exception {
	String new_path_name = this.toString() + "/" + other.toString();
	return new Path(new_path_name);
    }

    public Path append(String tail) throws Exception {
	String new_path_name = this.toString() + "/" + tail;
	return new Path(new_path_name);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!Path.class.isAssignableFrom(obj.getClass())) return false;
	Path other = (Path) obj;
	if (other.components.length != this.components.length) return false;
	for (int i = 0; i < components.length; i++) {
	    if (!other.components[i].equals(this.components[i])) return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	for (int i = 0; i < components.length; i++) {
	    hash+= components[i].hashCode();
	}
	return hash;
    }

    private String[] copy_over_array(int length) {
	String[] r = new String[length];
	for (int i = 0; i < length; i++) {
	    r[i] = components[i];
	}
	return r;
    }

    private void validate(String s) throws Exception {
	String query = "^([a-zA-Z0-9]+[/])*([a-zA-Z0-9]+)$";
	Pattern p = Pattern.compile(query);
	Matcher m = p.matcher(s);
	if (!m.find())
	    throw new Exception("Invalid path string");
    }
}
