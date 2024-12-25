package ro.fmi.unibuc.licitatie_curieri.common.utils;

import java.util.Locale;

public class LogMessageUtils {
    private LogMessageUtils() {}

    public static final String CREATE_USER = "createUser operation was invoked with email %s and userType %s";
    public static final String VERIFY_USER = "verifyUser operation was invoked with email %s";
    public static final String AUTHENTICATE_USER = "authenticateUser operation was invoked with email %s and password %s";
    public static final String TWO_FACTOR_AUTH_USER = "Verify 2FE operation was invoked with email %s and code %s";

    public static final String GET_ADDRESSES = "getAddresses operation was invoked";
    public static final String CREATE_ADDRESS = "createAddress operation was invoked for User %s";

    public static final String GET_RESTAURANTS = "getRestaurants operation was invoked for Address %s";
    public static final String CREATE_RESTAURANT = "createRestaurant operation was invoked with name %s, address %s, latitude %f and longitude %f";
    public static final String DELETE_RESTAURANT = "Restaurant with id: %d was deleted";
    public static final String UPDATE_RESTAURANT_BY_NAME = "Restaurant with id: %d was updated with name: %s";

    public static final String CREATE_MENU = "createMenu operation was invoked with name %s, price: %f, ingredientsList %s, photo %s and discount %f";
    public static final String DELETE_MENU = "Menu with id: %d was deleted";
}
