package vn.tech.website.store.util.payment.other.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoder {
    public static void main(String[] args) {

        final String inputString = "27|OSP000197|Thanh toán tiền cọc|20000|VND|Vũ Văn Hùng|hungvv@tech.vn|03663106999|xã Tiến Đức - huyện Hưng Hà - tỉnh Thái Bình|https://store.osp.vn/frontend/account/payment_wallet_return_url.xhtml|https://store.osp.vn/frontend/account/payment_wallet_return_url.xhtml|https://store.osp.vn/frontend/index.xhtml|vi|123456";

        System.out.println("MD5 hex for '" + inputString + "' :");
        System.out.println(getMD5Hex(inputString));
    }

    public static String getMD5Hex(final String inputString) {
         return DigestUtils.md5Hex(inputString);
    }
}
