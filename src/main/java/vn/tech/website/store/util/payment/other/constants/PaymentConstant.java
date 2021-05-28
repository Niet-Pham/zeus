package vn.tech.website.store.util.payment.other.constants;

public class PaymentConstant {
    // error code momo
    public static final int MOMO_SUCCESS = 0;
    public static final int MOMO_INITIALIZATION_TRANSACTION = -1;
    public static final int MOMO_LACK_OF_PARTNER_INFOR = 1;
    public static final int MOMO_ORDERID_WRONG_FORMAT = 2;
    public static final int MOMO_PAYMENT_AMOUNT_BOT_VALID = 4;
    public static final int MOMO_SIGNATURE_NOT_VALID = 5;
    public static final int MOMO_ORDER_ALREADY_EXISTS = 6;
    public static final int MOMO_TRANSACTION_PENDING = 7;
    public static final int MOMO_REQUEST_ALREADY_EXISTS = 12;
    public static final int MOMO_PARTNER_NOT_ACTIVED = 14;
    public static final int MOMO_SYSTEM_MAINTAINCE = 29;
    public static final int MOMO_TRANSACTION_PAID = 32;
    public static final int MOMO_TRANSACTION_CANNOT_REFUNDED = 33;
    public static final int MOMO_REFUND_TRANSACTION_PROCESSED = 34;
    public static final int MOMO_TRANSACTION_EXPIRED = 36;
    public static final int MOMO_ACCOUNT_EXPIRES_FOR_DAY = 37;
    public static final int MOMO_CUSTOMER_ACCOUNT_NOT_ENOUGH_MONEY = 38;
    public static final int MOMO_REQUEST_MALFORMED = 42;
    public static final int MOMO_SERVICE_NOT_SUPPORT_REQUEST = 44;
    public static final int MOMO_CUSTOMER_CANCEL_TRANSACTION = 49;
    public static final int MOMO_TRANSACTION_NOT_EXISTS = 58;
    public static final int MOMO_REQUEST_NOT_VALID = 59;
    public static final int MOMO_BANK_PAYMENT_FAILED = 63;
    public static final int MOMO_LOSS_FIELD_REQUEST_TYPE = 76;
    public static final int MOMO_CUSTOMER_AUTHENTICATION_FAILED = 80;
    public static final int MOMO_UNKOWN_ERROR = 99;
    public static final int MOMO_CUSTOMER_NOT_LINKED_BANK_ACCOUNT = 9043;

    // error code payment wallet api
    public static final String API_WALLET_ERROR_CODE_SUCCESS = "0000";
    public static final String API_WALLET_ERROR_CODE_UNKNOWN_ERROR = "0001";
    public static final String API_WALLET_ERROR_CODE_FUNCTION_NAME_IS_INVALID = "0002";
    public static final String API_WALLET_ERROR_CODE_MERCHANT_SITE_CODE_IS_NOT_VALID = "0003";
    public static final String API_WALLET_ERROR_CODE_VERSION_IS_NOT_VALID = "0004";
    public static final String API_WALLET_ERROR_CODE_ORDER_CODE_IS_NOT_VALID = "0005";
    public static final String API_WALLET_ERROR_CODE_ORDER_DESCRIPTION_IS_VALID = "0006";
    public static final String API_WALLET_ERROR_CODE_INVALID_AMOUNT_FORMAT = "0007";
    public static final String API_WALLET_ERROR_CODE_INVALID_CURRENCY = "0008";
    public static final String API_WALLET_ERROR_CODE_BUYER_FULLNAME_IS_NOT_VALID = "0009";
    public static final String API_WALLET_ERROR_CODE_BUYER_EMAIL_IS_INVALID = "0010";
    public static final String API_WALLET_ERROR_CODE_BUYER_MOBILE_IS_NOT_VALID = "0011";
    public static final String API_WALLET_ERROR_CODE_BUYER_ADDRESS_IS_INVALID = "0012";
    public static final String API_WALLET_ERROR_CODE_RETURN_URL_IS_INVALID = "0013";
    public static final String API_WALLET_ERROR_CODE_CANCEL_URL_IS_INVALID = "0014";
    public static final String API_WALLET_ERROR_CODE_NOTIFY_URL_IS_INVALID = "0015";
    public static final String API_WALLET_ERROR_CODE_TIME_LIMIT_IS_INVALID = "0016";
    public static final String API_WALLET_ERROR_CODE_INVALID_CHECKSUM_CODE = "0017";
    public static final String API_WALLET_ERROR_CODE_TOKEN_CODE_IS_INVALID = "0018";
    public static final String API_WALLET_ERROR_CODE_APPLICATION_CODE_HAS_NOT_YET_BEEN_APPLIED_TO_THE_UNIT = "0019";
    public static final String API_WALLET_ERROR_CODE_APPLICATION_CODE_CANNOT_BE_CREATED_FOR_THIS_UNIT = "0101";

    // status check order api
    public static final int API_WALLET_STATUS_NONE_REGISTER = 0;
    public static final int API_WALLET_STATUS_NEWLY_CREATED = 1;
    public static final int API_WALLET_STATUS_PATMENT = 2;
    public static final int API_WALLET_STATUS_ALREADY_PAID = 3;
    public static final int API_WALLET_STATUS_CANCELED = 4;
    public static final int API_WALLET_STATUS_BEING_REVIEWED = 5;
    public static final int API_WALLET_STATUS_REFUNDING = 6;
    public static final int API_WALLET_STATUS_REDUNDED = 7;
    public static final int API_WALLET_STATUS_WITHDRAWING_MONEY = 8;
    public static final int API_WALLET_STATUS_WITHDRAWED_MONEY = 9;
    public static final int API_WALLET_STATUS_AWAITING_INSALLMENT_PAYMENT = 10;
}
