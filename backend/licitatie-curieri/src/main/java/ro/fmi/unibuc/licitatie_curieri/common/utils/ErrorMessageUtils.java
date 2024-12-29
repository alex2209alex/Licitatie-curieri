package ro.fmi.unibuc.licitatie_curieri.common.utils;

public class ErrorMessageUtils {
    private ErrorMessageUtils() {
    }

    public static final String ONLY_CLIENT_CAN_GET_ADDRESSES = "Only client can get Addresses";
    public static final String ONLY_CLIENT_CAN_CREATE_ADDRESSES = "Only client can create Addresses";

    public static final String RESTAURANT_MENU_ALREADY_EXISTS = "Restaurant with id %d and menu with id %d already exists";
    public static final String MENU_NOT_FOUND = "Menu with id: %s not found";
    public static final String ONLY_CLIENT_AND_ADMIN_REST_CAN_GET_MENUS = "Only client and admin restaurant can get menus";
    public static final String ONLY_ADMIN_REST_CAN_CREATE_MENUS = "Only admin restaurant can create menus";
    public static final String ONLY_ADMIN_REST_CAN_DELETE_MENUS = "Only admin restaurant can delete menus";
    public static final String ONLY_ADMIN_REST_CAN_UPDATE_MENUS = "Only admin restaurant can update menus";

    public static final String ONLY_CLIENT_AND_RESTAURANT_ADMIN_CAN_GET_RESTAURANTS = "Only client and restaurant admin can get Restaurants";
    public static final String MISSING_ADDRESS_ID = "Client requires addressId to view Restaurants";
    public static final String USER_ADDRESS_WITH_ID_NOT_FOUND = "User Address with Id %s not found";
    public static final String ONLY_RESTAURANT_ADMIN_CAN_GET_RESTAURANT = "Only client and restaurant admin can get Restaurant";
    public static final String RESTAURANT_WITH_ID_NOT_FOUND = "Restaurant with id %s not found";
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
    public static final String AUTHENTICATION_IS_NULL = "Authentication is null";
}
