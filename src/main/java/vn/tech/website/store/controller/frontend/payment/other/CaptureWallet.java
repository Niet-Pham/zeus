package vn.tech.website.store.controller.frontend.payment.other;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import vn.tech.website.store.model.payment.other.CaptureWalletRequest;
import vn.tech.website.store.model.payment.other.CaptureWalletResponse;
import vn.tech.website.store.model.payment.other.CaptureWalletResponseCheckOrder;
import vn.tech.website.store.util.payment.PaymentVariableUtil;
import vn.tech.website.store.util.payment.other.sharedmodels.AbstractProcess;
import vn.tech.website.store.util.payment.other.utils.Encoder;

import java.io.IOException;

public class CaptureWallet extends AbstractProcess<CaptureWalletRequest, CaptureWalletResponse> {

    // get all valiable to save on database
    public static CaptureWalletRequest captureWalletRequestCoppy;
    public static CaptureWalletResponse captureWalletResponseCoppy;
    public static CaptureWalletResponseCheckOrder captureWalletResponseCheckOrderCoppy;

    // this method use for create order send to api
    public static CaptureWalletResponse processCreateOrder(CaptureWalletRequest captureWalletRequestIndex) throws IOException {
        CaptureWallet cProcessor = new CaptureWallet();
        CaptureWalletRequest captureWalletRequest = cProcessor.createPaymentCreationRequestCreateOrder(captureWalletRequestIndex);
        captureWalletRequestCoppy = new CaptureWalletRequest();
        captureWalletRequestCoppy = captureWalletRequest;
        CaptureWalletResponse captureWalletResponse = cProcessor.executeCreateOrder(captureWalletRequest);
        captureWalletResponseCoppy = new CaptureWalletResponse();
        captureWalletResponseCoppy = captureWalletResponse;
        return captureWalletResponse;
    }

    public CaptureWalletResponse executeCreateOrder(CaptureWalletRequest request) throws IOException {
        HttpResponse response = execute.sendToWallet(PaymentVariableUtil.getApiWalletEndpoint(), request);
        String json = EntityUtils.toString(response.getEntity());
        return getGson().fromJson(json, CaptureWalletResponse.class);
    }

    public CaptureWalletRequest createPaymentCreationRequestCreateOrder(CaptureWalletRequest captureWalletRequest) {
        try {
            String checksum = Encoder.getMD5Hex(captureWalletRequest.toString());
            return new CaptureWalletRequest(
                    captureWalletRequest.getFunction(),
                    captureWalletRequest.getMerchant_site_code(),
                    captureWalletRequest.getOrder_code(),
                    captureWalletRequest.getOrder_description(),
                    captureWalletRequest.getAmount(),
                    captureWalletRequest.getCurrency(),
                    captureWalletRequest.getBuyer_fullname(),
                    captureWalletRequest.getBuyer_email(),
                    captureWalletRequest.getBuyer_mobile(),
                    captureWalletRequest.getBuyer_address(),
                    captureWalletRequest.getReturn_url(),
                    captureWalletRequest.getCancel_url(),
                    captureWalletRequest.getNotify_url(),
                    captureWalletRequest.getLanguage(),
                    checksum,
                    captureWalletRequest.getApplication_id());
        } catch (Exception e) {
            //LogUtils.error("[CaptureWalletRequest] "+ e);
            return null;
        }
    }

    // this method use for check status of order by token code get from create order
    public static CaptureWalletResponseCheckOrder processCheckOrder(CaptureWalletRequest captureWalletRequestIndex) throws IOException {
        CaptureWallet cProcessorCheckOrder = new CaptureWallet();
        CaptureWalletRequest captureWalletRequest = cProcessorCheckOrder.createPaymentCreationRequestCheckOrder(captureWalletRequestIndex);
        captureWalletRequestCoppy = new CaptureWalletRequest();
        captureWalletRequestCoppy = captureWalletRequest;
        CaptureWalletResponseCheckOrder captureWalletResponseCheckOrder = cProcessorCheckOrder.executeCheckOrder(captureWalletRequest);
        captureWalletResponseCheckOrderCoppy = new CaptureWalletResponseCheckOrder();
        captureWalletResponseCheckOrderCoppy = captureWalletResponseCheckOrder;
        return captureWalletResponseCheckOrder;
    }

    public CaptureWalletResponseCheckOrder executeCheckOrder(CaptureWalletRequest request) throws IOException {
        HttpResponse response = execute.sendToWalletCheckOrder(PaymentVariableUtil.getApiWalletEndpoint(), request);
        String json = EntityUtils.toString(response.getEntity());
        return getGson().fromJson(json, CaptureWalletResponseCheckOrder.class);
    }

    public CaptureWalletRequest createPaymentCreationRequestCheckOrder(CaptureWalletRequest captureWalletRequest) {
        try {
            String checksum = Encoder.getMD5Hex(captureWalletRequest.toStringCheckOrder());
            return new CaptureWalletRequest(
                    captureWalletRequest.getFunction(),
                    captureWalletRequest.getMerchant_site_code(),
                    checksum,
                    captureWalletRequest.getApplication_id(),
                    captureWalletRequest.getToken_code());
        } catch (Exception e) {
            //LogUtils.error("[CaptureWalletRequest] "+ e);
            return null;
        }
    }
}
