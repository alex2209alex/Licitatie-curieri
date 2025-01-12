package ro.fmi.unibuc.licitatie_curieri.common.utils;

public class LogMessageUtils {
    private LogMessageUtils() {}

    public static final String GET_ADDRESSES = "getAddresses operation was invoked";
    public static final String CREATE_ADDRESS = "createAddress operation was invoked with latitude %s and longitude %s";

    public static final String GET_ALL_MENU_ITEMS_FOR_RESTAURANT = "getAllMenuItemsForRestaurant operation was invoked for Restaurant with Id %d";
    public static final String GET_MENU_ITEM = "getMenuItem operation was invoked for MenuItem with Id %s";
    public static final String CREATE_MENU_ITEM = "createMenuItem operation was invoked for Restaurant with Id %s";
    public static final String UPDATE_MENU_ITEM = "updateMenuItem operation was invoked for MenuItem with Id %s";
    public static final String REMOVE_MENU_ITEM = "removeMenuItem operation was invoked for MenuItem with Id %s";

    public static final String CREATE_ORDER = "createOrder operation was invoked";

    public static final String GET_RESTAURANTS = "getRestaurants operation was invoked";
    public static final String GET_RESTAURANT = "getRestaurant operation was invoked with Id %s";
    public static final String CREATE_RESTAURANT = "createRestaurant operation with name %s";
    public static final String UPDATE_RESTAURANT = "updateRestaurant operation was invoked for Restaurant with Id %s";
    public static final String REMOVE_RESTAURANT = "removeRestaurant operation was invoked for Restaurant with Id %s";

    public static final String CREATE_USER = "createUser operation was invoked with email %s and userType %s";
    public static final String VERIFY_USER = "verifyUser operation was invoked with email %s";
    public static final String AUTHENTICATE_USER = "authenticateUser operation was invoked with email %s and password %s";
    public static final String TWO_FACTOR_AUTH_USER = "Verify 2FE operation was invoked with email %s and code %s";

    public static final String GET_CLIENT_ORDER = "getClientOrder operation was invoked";
}
