package vn.tech.website.store.controller.frontend.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.util.FacesUtil;

import javax.inject.Named;
import java.io.Serializable;

@Getter
@Setter
@Named
@Scope(value = "session")
public class FacesNoticeController implements Serializable {
    private String script;

    public FacesNoticeController() {
        resetAll();
    }

    public void resetAll() {
        this.script = "";
    }

    public void resetGoogleRecaptcha() {
        resetGoogleRecaptcha("");
    }

    public void resetGoogleRecaptcha(String formId) {
        activeFunctionWithParam("resetGoogleCaptcha", formId);
    }

    public void resetGoogleRecaptchaRegister() {
        this.script +=
                "jQuery('.g-recaptcha').each(function(i,e){" +
                        "   jQuery(e).html('');" +
                        "});";
        this.script +=
                "jQuery('.g-recaptcha').each(function(i,e){" +
                        " console.log(jQuery(e).attr('id'));" +
                        "   grecaptcha.render(jQuery(e).attr('id'), { sitekey : jQuery(e).attr('data-sitekey') });" +
                        "});";
    }

    public void closeModal(String id) {
        script += "closeModal('#" + id + "');";
    }

    public void openModal(String id) {
        script += "openModal('#" + id + "');";
    }

    public void click(String id) {
        script += "document.getElementById('" + id + "').click();";
    }

    public void activeFunction(String functionName) {
        script += functionName + "();";
    }

    public void activeFunctionWithParam(String functionName, String componentId) {
        script += functionName + "('" + componentId + "');";
    }

    public void resetSubmitButton() {
        this.script += " jQuery('#checkCode').show();";
    }

    public void reload() {
        script += "window.location.reload();";
    }

    public void reloadAfter(long seconds) {
        script += "setTimeout(function(){window.location.reload();}, " + seconds * 1000 + ");";
    }

    public void addSuccessMessage(String message) {
        script += "simpleNotify.notify({message: '" + message + "', level: 'good'});";
    }

    public void addErrorMessage(String message) {
        script += "simpleNotify.notify({message: '" + message + "', level: 'danger'});";
    }

    public void addErrorMessage(String controlId, String msg) {
        FacesUtil.addErrorMessage(controlId, msg);
    }

    public String output() {
        try {
            activeFunction("stopLoadingScreen");
            return script;
        } finally {
            resetAll();
        }
    }

    private String buildScriptMessage(String message, String level) {
        return "simpleNotify.notify({message: '" + message + "', level: '" + level +"'});";
    }

    private String buildTryCatch(String str) {
        String s = "try {";
        s += str;
        s += "} catch (e) { console.error(e); }";
        return s;
    }

    public void showVerifyRegister(){
        script += "showVerifyRegister();";
    }
}
