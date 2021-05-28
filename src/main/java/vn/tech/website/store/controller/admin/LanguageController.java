package vn.tech.website.store.controller.admin;

import vn.tech.website.store.util.Constant;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@SessionScoped
public class LanguageController implements Serializable {

    private static final long serialVersionUID = 8575412082953212040L;
    private Locale currentLocale;
    private Map<String, String> errorMap;
    private Map<String, String> textMap;

    @PostConstruct
    public void init() {
        // set default en language
        //currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        currentLocale = new Locale("vi");

        textMap = loadData(Constant.TEXT_MESSAGE, currentLocale);
        errorMap = loadData(Constant.ERROR_MESSAGE, currentLocale);
    }

    public Map<String, String> loadData(String baseName, Locale locale) {
        Map<String, String> messageMap = new HashMap<>();
        // get resource bundle
        ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, locale);
        Enumeration<String> enumeration = (Enumeration<String>) resourceBundle
                .getKeys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = resourceBundle.getString(key);
            messageMap.put(key, value);
        }
        return messageMap;
    }

    // getter-setter
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public Map<String, String> getTextMap() {
        return textMap;
    }

    public void setTextMap(Map<String, String> textMap) {
        this.textMap = textMap;
    }

}
