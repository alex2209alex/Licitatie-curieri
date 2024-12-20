package ro.fmi.unibuc.licitatie_curieri.common.utils;

public class ErrorMessageUtils {
    private ErrorMessageUtils() {
    }

    public static final String EMAIL_ALREADY_USED_FOR_USER_TYPE = "Email %s is already used for user type %s";
    public static final String USER_WITH_EMAIL_AND_USER_TYPE_AWAITING_VERIFICATION = "User with email %s and user type %s is awaiting verification";
    public static final String EMAIL_INVALID = "Email is invalid";
    public static final String PHONE_NUMBER_INVALID = "Phone number is invalid";
    public static final String PASSWORD_IS_DIFFERENT_FROM_PASSWORD_CONFIRMATION = "Password is different from password confirmation";
    public static final String PASSWORD_INVALID = "Password is invalid. It should contain at least one capital letter, one lower case letter, one digit and one special character (~!@#$%^&*()_+). It should not contain white spaces";
    public static final String ERROR_HASHING_ALGORITHM = "Error with hashing algorithm";
    public static final String USER_VERIFICATION_FAILED = "User verification failed";
    public static final String VERIFICATION_FAILED_USER_DELETED = "User verification failed. User was deleted";
    public static final String USER_IS_UNVERIFIED = "User is unverified";
    public static final String ONLY_CLIENT_CAN_GET_ADDRESSES = "Only client can get addresses";
    public static final String ONLY_CLIENT_CAN_CREATE_ADDRESS = "Only client user can create address";
    public static final String USER_ADDRESS_NOT_FOUND = "User address %s not found";
    public static final String ONLY_CLIENT_CAN_GET_RESTAURANTS = "Only client can get restaurants";
    public static final String RESTAURANT_ALREADY_EXISTS = "%s already exists";
    public static final String RESTAURANT_NOT_FOUND = "Restaurant with id: %s not found";
    public static final String ADDRESS_NOT_FOUND = "Address with id: %s not found";
    public static final String AUTHORIZATION_FAILED = "Authorization failed";
}
