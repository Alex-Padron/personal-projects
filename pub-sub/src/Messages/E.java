package Messages;

public class E {
    // Messages between master and master client
    public static final int REGISTER = 0;
    public static final int QUERY = 1;
    public static final int NO_PUBLISHER = 2;
    public static final int PUBLISHER_INFO = 3;
    public static final int ACCEPTED_REGISTER = 4;
    public static final int INVALID_REQUEST = 5;
    public static final int REMOVE = 6;
    public static final int ACCEPTED_REMOVE = 7;

    // Messages between publisher and subscriber
    public static final int GET_VALUE = 8;
    public static final int QUERY_PATH = 9;
    public static final int PUBLISHING_PATH = 10;
    public static final int NOT_PUBLISHING = 11;
    public static final int VALUE_RESPONSE = 12;
}
