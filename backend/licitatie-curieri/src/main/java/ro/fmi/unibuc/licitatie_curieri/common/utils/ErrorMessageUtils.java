package ro.fmi.unibuc.licitatie_curieri.common.utils;

public class ErrorMessageUtils {
    private ErrorMessageUtils() {
    }

    public static final String EMAIL_ALREADY_USED_FOR_USER_TYPE = "Email %s is already used for user type %s";
    public static final String USER_WITH_EMAIL_AND_USER_TYPE_AWAITING_VERIFICATION = "User with email %s and user type %s is awaiting verification";
    public static final String PASSWORD_IS_DIFFERENT_FROM_PASSWORD_CONFIRMATION = "Password is different from password confirmation";
    public static final String PASSWORD_INVALID = "Password is invalid. It should contain at least one capital letter, one lower case letter, one digit and one special character (~!@#$%^&*()_+)";
    public static final String ERROR_HASHING_ALGORITHM = "Error with hashing algorithm";
}
