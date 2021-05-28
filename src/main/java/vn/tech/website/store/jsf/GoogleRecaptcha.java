package vn.tech.website.store.jsf;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import vn.tech.website.store.util.Constant;

import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

public class GoogleRecaptcha {
    public static final String url = "https://www.google.com/recaptcha/api/siteverify";
    private final static String USER_AGENT = "Mozilla/5.0";

    public static boolean verify() {
        try {
            String gRecaptchaResponse = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("g-recaptcha-response");
            return GoogleRecaptcha.verify(gRecaptchaResponse);
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean verify(String gRecaptchaResponse) throws IOException {
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
            return false;
        }
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            // add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String postParams = "secret=" + Constant.PRIVATE_CAPTCHA_KEY + "&response="
                    + gRecaptchaResponse;
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //parse JSON response and return 'success' value
            ObjectMapper mapper = new ObjectMapper();
            GoogleRecaptchaResponse googleRecaptchaResponse = mapper.readValue(response.toString(), GoogleRecaptchaResponse.class);
            return googleRecaptchaResponse.getSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Getter
    @Setter
    public static class GoogleRecaptchaResponse {
        private Boolean success;
        private Date challenge_ts;
        private String hostname;
    }
}
