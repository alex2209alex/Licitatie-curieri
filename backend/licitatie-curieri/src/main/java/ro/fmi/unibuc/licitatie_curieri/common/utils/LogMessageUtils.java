package ro.fmi.unibuc.licitatie_curieri.common.utils;

public class LogMessageUtils {
    private LogMessageUtils() {}

    public static final String CREATE_USER = "createUser operation was invoked with email %s and userType %s";
    public static final String VERIFY_USER = "verifyUser operation was invoked with email %s";

    public static final String GET_ADDRESSES = "getAddresses operation was invoked";
    public static final String CREATE_ADDRESS = "createAddress operation was invoked for User %s";

    public static final String GET_RESTAURANTS = "getRestaurants operation was invoked for Address %s";
    public static final String CREATE_RESTAURANT = "createRestaurant operation was invoked with name %s, address %s, latitude %f and longitude %f";

}
