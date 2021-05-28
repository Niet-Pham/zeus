package vn.tech.website.store.util;

public class Constant {

    public static final String TEXT_MESSAGE = "Text";
    public static final String ERROR_MESSAGE = "Error";
    public static final String ERROR_MESSAGE_ID = "errorMsgDialog";
    public static final String ERROR_MESSAGE_ID_1 = "errorMsgDialog1";
    public static final String ERROR_FE_GROWL_ID = ":notification";
    public static final String ERROR_GROWL_ID = "growl";
    public static final String ERROR_GROWL_DETAIL_ID = "growlDetail";
    public static final String EMPTY_MESSAGE = "Error";

    // page size
    public static final int PAGE_INDEX = 0;
    public static final int PAGE_SIZE_ROW = 4;
    public static final int PAGE_SIZE_REGULATION_LIST = 6;
    public static final int PAGE_SIZE_ASSET_LIST = 8;
    public static final int PAGE_SIZE_MAX = 10;
    public static final int PAGE_SIZE_SEARCH = 10;

    //Max file size
    public static final int MAX_FILE_SIZE = 50000000;

    // expire session
    public static final int SESSION_EXPIRE_IN = 30 * 60 * 1000; // 30 minutes

    //number wrong show captcha
    public static final int NUMBER_WRONG_PASSWORD_TO_SHOW_CAPTCHA = 3;

    //Menu trang chu
    public static final String NO_IMAGE_URL = "/images/no-image.jpeg";
    public static final String ASSET_NO_IMAGE_URL = "/frontend-layout/images/asset_image_default.jpg";
    public static final String ID_DON_YEU_CAU_DAU_GIA = "qldycdg";
    public static final String ID_THONG_TIN_LH = "ttlh";
    public static final String ID_CAU_HINH_TIN_TUC = "chtt";
    public static final String ID_CAP_NHAT_THONG_TIN_CA_NHAN = "cnttcn";
    public static final String ID_PROPERTY_SALE_HISTORY = "lsmbts";
    public static final String ID_QUAN_LY_TAI_KHOAN_NGUOI_DUNG = "qltknd";
    public static final String ID_PAYMENT_WALLET_RETURN_URL = "pwr";
    public static final String ID_MY_ACCOUNT="mac";

    //Screen frontend
    public static final String ID_INDEX_FONTEND = "hpfe";
    public static final String ID_ASSET_FRONTEND = "afe";
    public static final String ID_REGULATION_FRONTEND = "rfe";
    public static final String ID_REGULATION_DETAIL_FRONTEND = "rdfe";
    public static final String ID_ASSET_DETAIL_FRONTEND = "adfe";
    public static final String ID_MY_AUCTION = "ID_MY_AUCTION";
    public static final String ID_MY_PAYMENT = "ID_MY_PAYMENT";
    public static final String ID_COUNTDOWN_LIBRARY = "countdown-id";

    //Private key captcha
    public static final String PRIVATE_CAPTCHA_KEY = "6Ld5X84UAAAAAMnu0-8-2X9bfLEn_oTSy8JIfBFy";

    //cookie
    public static final String COOKIE_ACCOUNT = "_JCA";
    public static final String COOKIE_PASSWORD = "_JCP";

    //Url template export direct payment
    public static final String TEMPLETE_EXPORT_FILE_DIRECT_PAYMENT = "/WEB-INF/export/template_direct_payment.xlsx";
    public static final String REPORT_EXPORT_FILE_DIRECT_PAYMENT = "/WEB-INF/report/";

    public static final String TEMPLETE_REPORT_ADD_DATA_EXPORT_ORDERS_FILE = "/WEB-INF/template/orders_detail.xlsx";
    public static final String REPORT_ADD_DATA_EXPORT_ORDERS_FILE = "/WEB-INF/report/orders_detail.xlsx";

    //Index row start
    public static final int INDEX_ROW_START_EXPORT = 10;

    //Column width
    public static final int COLUMN_WIDTH = 7168;
    public static final int COLUMN_REGISTER_NAME_WIDTH = 8700;
    //Column width STT
    public static final int COLUMN_WIDTH_STT = 2048;

    public static final String REPORT_EXPORT_FILE_VOTE = "/WEB-INF/report/";

    // Url template export vote
    public static final String TEMPLATE_EXPORT_FILE_VOTE = "/WEB-INF/export/template_vote.xlsx";
    public static final String REPORT_EXPORT_FILE_BILL = "/WEB-INF/report/Bill.docx";
    public static final String REPORT_EXPORT_FILE_BILL_TRANSACTION_CONTROL = "/WEB-INF/report/BillTransactionControl.docx";
    public static final String TEMPLATE_REPORT_ASSET_LIST_EXPORT_FILE = "/WEB-INF/template/TemplateReportAssetList.xlsx";
    public static final String TEMPLATE_REPORT_AND_STATISTICAL = "/WEB-INF/template/ReportAndStatistical.xlsx";
    public static final String TEMPLATE_BILL = "/WEB-INF/template/Bill.xlsx";

    // Excel export report
    public static final String REPORT_ASSET_LIST_EXPORT_FILE = "/WEB-INF/report/PrintReportAssetList.xlsx";
    public static final String REPORT_REPORT_AND_STATISTICAL = "/WEB-INF/report/ReportAndStatistical.xlsx";

    public static final String TIME_ZONE_DEFAULT = "Asia/Ho_Chi_Minh";

    //acronym for create code
    public static final String ACRONYM_PRODUCT = "SP";
    public static final String ACRONYM_ORDER = "DH";
    public static final String ACRONYM_BILL = "HD";

    //Account
    public static final String ACCOUNT_PASSWORD_DEFAULT = "123456a@";

    //resources
    public static final String RESOURCES_FE = "../frontend";

    //param cate search product FE
    public static final int CATE_PHONE = 1;
    public static final int CATE_HEADPHONE = 2;
    public static final int CATE_LAPTOP = 3;
    public static final int CATE_WATCH = 4;
    public static final String CATE_PHONE_CODE = "phone";
    public static final String CATE_LAPTOP_CODE = "pc";
    public static final String CATE_WATCH_CODE = "watch";
    public static final String CATE_HEADPHONE_CODE = "headphone";

    public static final int CATE_NEWS_DISCOUNT = 9;
    public static final int CATE_NEWS_PRODUCT = 8;
    public static final int CATE_NEWS_TECH = 5;
    public static final int CATE_NEWS_HOT = 3;
    public static final int CATE_NEWS_HIGHLIGHT = 4;

    public static final int CATE_MALE = 1;
    public static final int CATE_FEMALE = 2;
    public static final int CATE_KID = 3;
    public static final String CATE_AO = "AO";
    public static final String CATE_QUAN = "QUAN";
    public static final String CATE_GIAY = "GIAY";
    public static final String CATE_PHU_KIEN = "PHUKIEN";
    public static final int CATE_FASHION = 5;
    public static final int CATE_FOOTWARE = 6;

    public static final Double SHIPPING = 30000D;

    //brand K2
    public static final int BRAND_APPLE_ID = 1;
    public static final int BRAND_XIAOMI_ID = 2;
    public static final int BRAND_ASUS_ID = 3;
    public static final int BRAND_HUAWEI_ID = 4;
    public static final int BRAND_LG_ID = 5;
    public static final int BRAND_SAMSUNG_ID = 6;
    public static final int BRAND_SONY_ID = 7;
    public static final int BRAND_VINFAST_ID = 8;

    //brand Niet
    public static final int BRAND_NIKE_ID = 1;
    public static final int BRAND_ADIDAS_ID = 2;
    public static final int BRAND_PRADA_ID = 3;
    public static final int BRAND_LACOSTE_ID = 4;
    public static final int BRAND_GUCCI_ID = 6;
    public static final int BRAND_YSL_ID = 7;
    public static final int BRAND_ZARA_ID = 8;
    public static final int BRAND_ZEUS_ID = 12;
    public static final int BRAND_BOO_ID = 11;
    public static final int BRAND_BAD_HABIT_ID = 10;
    public static final int BRAND_SWE_ID = 9;

    // Min length of password
    public static final int ACCOUNT_MINLENGTH_PASSWORD_USER = 6;

    public static final int MIN_PRICE = 0;
    public static final int MAX_PRICE = 10000000;
    public static final int STEP_PRICE = 100000;

    public static final int NOTIFICATION_PAGE_SIZE = 10;


    public static final String REPORT_EXPORT_FILE = "/WEB-INF/report/thongke.xlsx";

    public static final String URL_LOGO_K2 = "https://i2.paste.pics/fe148b655d53d6f87590d41d074d46c0.png";
    public static final String URL_LOGO_ZEUS = "https://i2.paste.pics/cace1603401daa1b64bc6ba5b44dd8eb.png";

    public static final String ZEUS_SHOP = "Zeus Shop";
    public static final String K2_SHOP = "K2 Tech Store";

    public static final String K2_FOUNDER = "Parker Vu";
    public static final String ZEUS_FOUNDER = "Niet Pham";
}
