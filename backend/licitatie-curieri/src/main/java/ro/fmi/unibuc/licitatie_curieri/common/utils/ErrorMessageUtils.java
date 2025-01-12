package ro.fmi.unibuc.licitatie_curieri.common.utils;

public class ErrorMessageUtils {
    private ErrorMessageUtils() {
    }

    public static final String ONLY_CLIENT_CAN_GET_ADDRESSES = "Only client can get Addresses";
    public static final String ONLY_CLIENT_CAN_CREATE_ADDRESSES = "Only client can create Addresses";

    public static final String ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_MENU_ITEMS = "Only client and restaurant admin can get Menu Items";
    public static final String RESTAURANT_NOT_FOUND = "Restaurant with Id %s not found";
    public static final String ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_MENU_ITEM = "Only client and restaurant admin can get Menu Item";
    public static final String MENU_ITEM_NOT_FOUND = "Menu Item with Id %s not found";
    public static final String ONLY_RESTAURANT_ADMIN_CAN_CREATE_MENU_ITEMS= "Only restaurant admin can create Menu Items";
    public static final String ONLY_RESTAURANT_ADMIN_CAN_UPDATE_MENU_ITEMS = "Only restaurant admin can update Menu Items";
    public static final String ONLY_RESTAURANT_ADMIN_CAN_REMOVE_MENU_ITEMS = "Only restaurant admin can remove Menu Items";

    public static final String ONLY_CLIENT_CAN_CREATE_ORDERS = "Only client can create Orders";
    public static final String ORDER_TOO_FAR = "Order too far";
    public static final String NOT_ALL_MENU_ITEMS_BELONG_TO_SAME_RESTAURANT = "Not all Menu Items belong to the same Restaurant";

    public static final String ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_RESTAURANTS = "Only client and restaurant admin can get Restaurants";
    public static final String MISSING_ADDRESS_ID = "Client requires addressId to view Restaurants";
    public static final String USER_ADDRESS_WITH_ID_NOT_FOUND = "User Address with Id %s not found";
    public static final String ONLY_RESTAURANT_ADMIN_CAN_GET_RESTAURANT = "Only client and restaurant admin can get Restaurant";
    public static final String ONLY_RESTAURANT_ADMIN_CAN_CREATE_RESTAURANTS = "Only restaurant admin can create Restaurants";
    public static final String ONLY_RESTAURANT_ADMIN_CAN_UPDATE_RESTAURANTS = "Only restaurant admin can update Restaurants";
    public static final String ONLY_RESTAURANT_ADMIN_CAN_REMOVE_RESTAURANTS = "Only restaurant admin can remove Restaurants";

    public static final String EMAIL_ALREADY_USED_FOR_USER_TYPE = "Email %s is already used for user type %s";
    public static final String USER_WITH_EMAIL_AND_USER_TYPE_AWAITING_VERIFICATION = "User with email %s and user type %s is awaiting verification";
    public static final String EMAIL_INVALID = "Email is invalid";
    public static final String PHONE_NUMBER_INVALID = "Phone number is invalid";
    public static final String PASSWORD_IS_DIFFERENT_FROM_PASSWORD_CONFIRMATION = "Password is different from password confirmation";
    public static final String PASSWORD_INVALID = "Password is invalid. It should contain at least one capital letter, one lower case letter, one digit and one special character (~!@#$%^&*()_+). It should not contain white spaces";
    public static final String ERROR_HASHING_ALGORITHM = "Error with hashing algorithm";
    public static final String USER_VERIFICATION_FAILED = "User verification failed";
    public static final String VERIFICATION_FAILED_USER_DELETED = "User verification failed. User was deleted";
    public static final String AUTHORIZATION_FAILED = "Authorization failed";
    public static final String USER_NOT_FOUND = "User with email: %s not found";
    public static final String VERIFICATION_FAILED_TWO_FA = "2FA verification failed. User not authenticated";

    public static final String USER_IS_UNVERIFIED = "User is unverified";
    public static final String AUTHENTICATION_TOKEN_IS_INVALID = "Authentication token is invalid";

    public static final String ONLY_CLIENT_CAN_GET_CLIENT_ORDERS = "Only client can get client Orders";
    public static final String ONLY_COURIER_CAN_GET_NEARBY_ORDERS = "Only courier can get nearby Orders";

    public static final String ONLY_CLIENT_CAN_CANCEL_ORDERS = "Only client can cancel Orders";
    public static final String ORDER_NOT_FOUND = "Order with id %s not found";
    public static final String ORDER_DOES_NOT_BELONG_TO_CLIENT = "Order does not belong to the client";
    public static final String ONLY_COURIER_CAN_MAKE_OFFERS = "Only courier can make offers";
    public static final String ORDER_CANNOT_BE_CANCELED = "Order cannot be canceled";
    public static final String ERROR_WITH_ORDER_THREAD = "Error with order thread";
}
