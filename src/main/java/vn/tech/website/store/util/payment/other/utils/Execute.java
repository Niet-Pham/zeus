package vn.tech.website.store.util.payment.other.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import vn.tech.website.store.model.payment.other.CaptureWalletRequest;
import vn.tech.website.store.util.payment.other.constants.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Execute {

    public HttpResponse sendToWallet(String endpoint, CaptureWalletRequest request) {
        try {
            HttpPost post = new HttpPost(endpoint);

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair(Parameter.FUNNCTION, request.getFunction()));
            urlParameters.add(new BasicNameValuePair(Parameter.MERCHANT_SITE_CODE, request.getMerchant_site_code()));
            urlParameters.add(new BasicNameValuePair(Parameter.ORDER_CODE, request.getOrder_code()));
            urlParameters.add(new BasicNameValuePair(Parameter.ORDER_DESCRIPTION, request.getOrder_description()));
            urlParameters.add(new BasicNameValuePair(Parameter.AMOUNT, request.getAmount().toString()));
            urlParameters.add(new BasicNameValuePair(Parameter.CURRENCY, request.getCurrency()));
            urlParameters.add(new BasicNameValuePair(Parameter.BUYER_FULLNAME, request.getBuyer_fullname()));
            urlParameters.add(new BasicNameValuePair(Parameter.BUYER_EMAIL, request.getBuyer_email()));
            urlParameters.add(new BasicNameValuePair(Parameter.BUYER_MOBILE, request.getBuyer_mobile()));
            urlParameters.add(new BasicNameValuePair(Parameter.BUYER_ADDRESS, request.getBuyer_address()));
            urlParameters.add(new BasicNameValuePair(Parameter.RETURN_URL, request.getReturn_url()));
            urlParameters.add(new BasicNameValuePair(Parameter.CANCEL_URL, request.getCancel_url()));
            urlParameters.add(new BasicNameValuePair(Parameter.NOTIFY_URL, request.getNotify_url()));
            urlParameters.add(new BasicNameValuePair(Parameter.LANGUAGE, request.getLanguage()));
            urlParameters.add(new BasicNameValuePair(Parameter.CHECKSUM, request.getChecksum()));
            urlParameters.add(new BasicNameValuePair(Parameter.APPLICATION_ID, request.getApplication_id().toString()));

            post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
            HttpClient httpclient = HttpClients.createDefault();
            return httpclient.execute(post);

        } catch (Exception e) {
            LogUtils.error("[ExecuteSendToWallet] " + e);
            return null;
        }
    }

    public HttpResponse sendToWalletCheckOrder(String endpoint, CaptureWalletRequest request) {
        try {
            HttpPost post = new HttpPost(endpoint);

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair(Parameter.FUNNCTION, request.getFunction()));
            urlParameters.add(new BasicNameValuePair(Parameter.MERCHANT_SITE_CODE, request.getMerchant_site_code()));
            urlParameters.add(new BasicNameValuePair(Parameter.TOKEN_CODE, request.getToken_code()));
            urlParameters.add(new BasicNameValuePair(Parameter.CHECKSUM, request.getChecksum()));
            urlParameters.add(new BasicNameValuePair(Parameter.APPLICATION_ID, request.getApplication_id().toString()));

            post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
            HttpClient httpclient = HttpClients.createDefault();
            return httpclient.execute(post);

        } catch (Exception e) {
            LogUtils.error("[ExecuteSendToWallet] " + e);
            return null;
        }
    }
}
