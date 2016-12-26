package rkr.binatestation.pathrakaran.utils;

/**
 * Created by RKR on 17/10/2016.
 * Constants.
 */

public class Constants {
    /**
     * Shared Preference Key Constants
     */
    public static final String KEY_SP_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_SP_USER_ID = "user_id";
    public static final String KEY_SP_USER_NAME = "user_name";
    public static final String KEY_SP_USER_PHONE = "user_phone";
    public static final String KEY_SP_USER_EMAIL = "user_email";
    public static final String KEY_SP_USER_IMAGE = "user_image";
    public static final String KEY_SP_USER_TYPE = "user_type";
    public static final String KEY_SP_USER_ADDRESS = "user_address";
    public static final String KEY_SP_USER_POSTCODE = "user_postcode";
    public static final String KEY_SP_USER_LATITUDE = "user_latitude";
    public static final String KEY_SP_USER_LONGITUDE = "user_longitude";

    /**
     * JSON Keys
     */
    public static final String KEY_JSON_USER_ID = "userid";
    public static final String KEY_JSON_NAME = "name";
    public static final String KEY_JSON_MOBILE = "mobile";
    public static final String KEY_JSON_EMAIL = "email";
    public static final String KEY_JSON_STATUS = "status";
    public static final String KEY_JSON_DATA = "data";
    public static final String KEY_JSON_MESSAGE = "message";
    public static final String KEY_JSON_IMAGE = "image";
    public static final String KEY_JSON_USER_TYPE = "user_type";
    public static final String KEY_JSON_LATITUDE = "latitude";
    public static final String KEY_JSON_LONGITUDE = "longitude";
    public static final String KEY_JSON_ADDRESS = "address";
    public static final String KEY_JSON_POSTCODE = "postcode";
    /**
     * POST Param Key
     */
    public static final String KEY_POST_USER_ID = "userid";
    public static final String KEY_POST_USER = "user";
    public static final String KEY_POST_PASSWORD = "password";
    public static final String KEY_POST_NAME = "name";
    public static final String KEY_POST_MOBILE = "mobile";
    public static final String KEY_POST_USER_TYPE = "user_type";
    public static final String KEY_POST_LOGIN_TYPE = "login_type";
    public static final String KEY_POST_IMAGE = "image";
    public static final String KEY_POST_ADDRESS = "address";
    public static final String KEY_POST_POSTCODE = "postcode";
    public static final String KEY_POST_EMAIL = "email";
    public static final String KEY_POST_LATITUDE = "latitude";
    public static final String KEY_POST_LONGITUDE = "longitude";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    public static final int REQUEST_READ_CONTACTS = 100;
    public static final int REQUEST_LOCATION_PERMISSION = 101;
    public static final int REQUEST_EXTERNAL_STORAGE = 102;

    /**
     * API URL
     */
    public static final String USER_LOGIN = "user/login";
    public static final String USER_REGISTER = "user/register";
    public static final String USER_PROFILE = "user/profile";
    public static final String USER_PROFILE_UPDATE = "user/profile/update";

    /**
     * Static Constants
     */
    public static final String USER_TYPE_AGENT = "AG";
    public static final String USER_TYPE_SUPPLIER = "SP";
    public static final String USER_TYPE_SUBSCRIBER = "SC";
}
