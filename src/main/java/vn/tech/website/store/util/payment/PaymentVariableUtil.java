package vn.tech.website.store.util.payment;

import vn.tech.website.store.util.PropertiesUtil;

public class PaymentVariableUtil {
    /*MOMO*/
    public static String evn() {
        return PropertiesUtil.getPropertyEnvironment("evn");
    }

    public static String momoOderInfor() {
        return PropertiesUtil.getPropertyEnvironment("momo.order.infor");
    }

    public static String momoReturnUrl() {
        return PropertiesUtil.getPropertyEnvironment("momo.return.url");
    }

    public static String momoNotifyUrl() {
        return PropertiesUtil.getPropertyEnvironment("momo.notify.url");
    }

    public static String momoExtraData() {
        return PropertiesUtil.getPropertyEnvironment("momo.extra.data");
    }

    public static String momoBankCode() {
        return PropertiesUtil.getPropertyEnvironment("momo.bank.code");
    }

    //environment
    public static String momoPartnerCode(String partnerCode) {
        return PropertiesUtil.getPropertyEnvironment(partnerCode);
    }

    public static String momoAccessKey(String accessKey) {
        return PropertiesUtil.getPropertyEnvironment(accessKey);
    }

    public static String momoSecretKey(String secretKey) {
        return PropertiesUtil.getPropertyEnvironment(secretKey);
    }

    /*OTHER WALLET*/
    public static String getApiWalletEndpoint() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_ENDPOINT");
    }

    public static String getApiWalletMerchantPassCode() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_MERCHANT_PASSCODE");
    }

    public static String getApiWalletFunction() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_FUNCTION");
    }

    public static String getApiWalletFunctionCheck() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_FUNCTION_CHECK");
    }

    public static String getApiWalletMerchantSiteCode() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_MERCHANT_SITE_CODE");
    }

    public static String getApiWalletCurrency() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_CURRENCY");
    }

    public static String getApiWalletReturnUrl() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_RETURN_URL");
    }

    public static String getApiWalletCancelUrl() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_CANCEL_URL");
    }

    public static String getApiWalletNotifyUrl() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_NOTIFY_URL");
    }

    public static String getApiWalletLanguage() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_LANGUAGE");
    }

    public static String getApiWalletApplicationId() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_APPLICATION_ID");
    }

    // message status response api
    public static String getApiWalletStatusNewlyCreated() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_NEWLY_CREATED");
    }

    public static String getApiWalletStatusPayment() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_PATMENT");
    }

    public static String getApiWalletStatusAlreadyPaid() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_ALREADY_PAID");
    }

    public static String getApiWalletStatusCanceled() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_CANCELED");
    }

    public static String getApiWalletStatusBeingReviewed() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_BEING_REVIEWED");
    }

    public static String getApiWalletStatusRefunding() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_REFUNDING");
    }

    public static String getApiWalletStatusRefunded() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_REDUNDED");
    }

    public static String getApiWalletStatusWithdrawingMoney() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_WITHDRAWING_MONEY");
    }

    public static String getApiWalletStatusWithdrawedMoney() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_WITHDRAWED_MONEY");
    }

    public static String getApiWalletStatusAwaitingInstallmentPayment() {
        return PropertiesUtil.getPropertyEnvironment("API_WALLET_AWAITING_INSALLMENT_PAYMENT");
    }
}
