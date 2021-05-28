package vn.tech.website.store.util;

public class DbConstant {
    public static final Integer ACCOUNT_INACTIVE_STATUS = 0;
    public static final Integer ACCOUNT_ACTIVE_STATUS = 1;
    public static final Integer ACCOUNT_LOCK_STATUS = 2;
    public static final Integer ACCOUNT_DELETE_STATUS = 3;
    public static final boolean TYPE_NEWS = false;
    public static final boolean TYPE_DECISION_TEXT = true;

    //---------------------

    //Status int
    public static final int ACTIVE = 1;
    public static final int DEACTIVE = 2;

    //maxlengthPassword
    public static final int ACCOUNT_MAXLENGTH_PASSWORD_ADMIN = 12;
    public static final int ACCOUNT_MINLENGTH_PASSWORD_USER = 6;
    //isOrg
    public static final boolean PESONAL = false;
    public static final boolean ORGANIZATION = true;
    public static final int notOrg = 0;
    public static final int isOrg = 1;
    //username
    public static final int ACCOUNT_MINLENGTH_USERNAME = 6;

    //sex
    public static final int UNKNOW = 0;
    public static final int MAN = 1;
    public static final int WOMAN = 2;

    //role
    public static final int ROLE_ID_NOT_LOGIN = -1;
    public static final int ROLE_ID_ADMIN = 0;
    public static final int ROLE_ID_MANAGER = 1;
    public static final int ROLE_ID_EMPLOYEE = 2;
    public static final int ROLE_ID_USER = 3;

    public static final int ROLE_STATUS_ACTIVE = 1;
    public static final int ROLE_STATUS_INACTIVE = 0;


    public static final int PAGE_SIZE = 5;
    // asset_management
    public static final boolean ASSET_MANAGEMENT_ENDING_BAD = false;
    public static final boolean ASSET_MANAGEMENT_ENDING_GOOD = true;

    // asset
    public static final int ASSET_STATUS_WAITING = 1;
    public static final int ASSET_STATUS_PLAYING = 2;
    public static final int ASSET_STATUS_ENDED = 3;
    public static final int ASSET_STATUS_NOT_SUCCESS = 4;
    public static final int ASSET_STATUS_CANCELED = 5;
    // system_config
    public static final long SYSTEM_CONFIG_ID_DEPOSITION = 1;
    public static final long SYSTEM_CONFIG_ID_WAITING_1TH = 2;
    public static final long SYSTEM_CONFIG_ID_WAITING_2TH = 3;
    public static final long SYSTEM_CONFIG_ID_BREAK_TIME = 4;
    public static final long SYSTEM_CONFIG_ID_SHOW_CAPTCHA = 5;
    public static final long SYSTEM_CONFIG_ID_NUMBER_WRONG_SHOW_CAPTCHA = 6;
    public static final long SYSTEM_CONFIG_ID_NUMBER_LOGIN_FAILED = 7;


    // payment
    public static final int PAYMENT_STATUS_UNPAID = 0;
    public static final int PAYMENT_STATUS_PAID = 1;
    public static final int PAYMENT_STATUS_REFUND = 2;
    public static final boolean PAYMENT_SEND_BILL_STATUS_UNPAID = false;
    public static final boolean PAYMENT_SEND_BILL_STATUS_PAID = true;
    public static final int PAYMENT_FORMALITY_ONLINE = 1;
    public static final int PAYMENT_FORMALITY_OFFLINE = 2;

    // cấu hình thông tin liên hệ
    public static final long MA_TITLE = 2;
    public static final long MA_ADDRESS = 3;
    public static final long MA_EMAIL = 4;
    public static final long MA_PHONE = 5;
    public static final long MA_MAPS = 6;


    public static final boolean IS_ORG = true;
    public static final boolean NOT_ORG = false;

    // Time to change password
    public static final Integer ACCOUNT_TIME_TO_CHANGE_PASSWORD = 90;

    // Status create receipt manager payment
    public static final int NOTIFY_PAYMENT_STATUS_NON_CREATE = 0;
    public static final int NOTIFY_PAYMENT_STATUS_CREATED = 1;

    //limit value to show FE
    public static final int LIMIT_SHOW_FE = 6;

    //status brand
    public static final int BRAND_STATUS_INACTIVE = 0;
    public static final int BRAND_STATUS_ACTIVE = 1;

    //status category
    public static final int CATEGORY_TYPE_NEWS = 0;
    public static final int CATEGORY_TYPE_PRODUCT = 1;

    public static final int CATEGORY_STATUS_INACTIVE = 0;
    public static final int CATEGORY_STATUS_ACTIVE = 1;

    //product option
    public static final int OPTION_STATUS_INACTIVE = 0;
    public static final int OPTION_STATUS_ACTIVE = 1;
    public static final int OPTION_TYPE_SIZE = 0; public static final String OPTION_TYPE_SIZE_STRING = "Size";
    public static final int OPTION_TYPE_COLOR = 1; public static final String OPTION_TYPE_COLOR_STRING = "Màu";
    public static final int OPTION_TYPE_RELEASE = 2; public static final String OPTION_TYPE_RELEASE_STRING = "Năm sản xuất";
    public static final int OPTION_TYPE_OTHER = 3; public static final String OPTION_TYPE_OTHER_STRING = "Khác";

    //type product
    public static final int PRODUCT_STATUS_INACTIVE = 0;
    public static final int PRODUCT_STATUS_ACTIVE = 1;

    public static final int PRODUCT_TYPE_PARENT = 0;
    public static final int PRODUCT_TYPE_CHILD = 1;
    public static final int PRODUCT_TYPE_NONE = 2;

    public static final String PRODUCT_TYPE_PARENT_STRING = "Sản phẩm cha";
    public static final String PRODUCT_TYPE_CHILD_STRING = "Sản phẩm con";
    public static final String PRODUCT_TYPE_NONE_STRING = "Sản phẩm duy nhất";

    //order
    public static final int ORDER_STATUS_NOT_APPROVED = 0; public static final String ORDER_STATUS_NOT_APPROVED_STRING = "Chưa phê duyệt";
    public static final int ORDER_STATUS_APPROVED = 1; public static final String ORDER_STATUS_APPROVED_STRING = "Đã phê duyệt";
    public static final int ORDER_STATUS_CANCEL = 2; public static final String ORDER_STATUS_CANCEL_STRING = "Hủy đơn hàng";
    public static final int ORDER_STATUS_PAID = 3; public static final String ORDER_STATUS_PAID_STRING = "Đã thanh toán";
    public static final int ORDER_TYPE_ORDER = 0;
    public static final int ORDER_TYPE_BILL = 1;

    //news
    public static final int NEWS_STATUS_INACTIVE = 0;
    public static final int NEWS_STATUS_ACTIVE = 1;

    // shipping
    public static final int SHIPPING_STATUS_INACTIVE = 0;
    public static final int SHIPPING_STATUS_ACTIVE = 1;

    //payment
    public static final int PAYMENTS_STATUS_UNPAID = 0;
    public static final int PAYMENTS_STATUS_PAID = 1;
    public static final int PAYMENTS_TYPE_OFFLINE = 0;
    public static final int PAYMENTS_TYPE_ONLINE = 1;

    //send notification
    public static final int SNOTIFICATION_STATUS_INACTIVE = 0;
    public static final int SNOTIFICATION_STATUS_ACTIVE = 1;
    public static final int NOTIFICATION_TYPE_ORDER = 0;
    public static final int NOTIFICATION_TYPE_PRODUCT = 1;
    public static final int NOTIFICATION_TYPE_NEWS = 2;

    // notification
    public static final boolean NOTIFICATION_STATUS_NOT_SEEN = false;
    public static final boolean NOTIFICATION_STATUS_SEEN = true;
    public static final boolean NOTIFICATION_STATUS_BELL_NOT_SEEN = false;
    public static final boolean NOTIFICATION_STATUS_BELL_SEEN = true;
}
