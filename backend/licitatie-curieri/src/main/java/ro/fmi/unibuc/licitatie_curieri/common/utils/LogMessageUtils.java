package ro.fmi.unibuc.licitatie_curieri.common.utils;

public class LogMessageUtils {
    private LogMessageUtils() {}

    public static final String GET_ADDRESSES = "getAddresses operation was invoked";
    public static final String CREATE_ADDRESS = "createAddress operation was invoked with latitude %s and longitude %s";

    public static final String CREATE_USER = "createUser operation was invoked with email %s and userType %s";
    public static final String VERIFY_USER = "verifyUser operation was invoked with email %s";
    public static final String AUTHENTICATE_USER = "authenticateUser operation was invoked with email %s and password %s";
    public static final String TWO_FACTOR_AUTH_USER = "Verify 2FE operation was invoked with email %s and code %s";

    public static final String GET_RESTAURANTS = "getRestaurants operation was invoked for Address %s";
    public static final String CREATE_RESTAURANT = "createRestaurant operation was invoked for Restaurant %s";
    public static final String UPDATE_RESTAURANT = "updateRestaurant operation was invoked for Restaurant %s";
    public static final String REMOVE_RESTAURANT = "removeRestaurant operation was invoked for Restaurant %s";

    public static final String CREATE_MENU = "createMenu operation was invoked with idRestaurant %d, name %s, price: %f, ingredientsList %s, photo %s and discount %f";
    public static final String DELETE_MENU = "Menu with id: %d was deleted";
    public static final String UPDATE_MENU = "updateMenu operation was invoked with name %s, price: %f, ingredientsList %s, photo %s and discount %f";
    public static final String GET_MENU_BY_ID = "getMenuByID operation was invoked for id %d";
    public static final String GET_ALL_MENUS_BY_RESTAURANT_ID = "getAllMenusByRestaurantID operation was invoked for id %d";
}
