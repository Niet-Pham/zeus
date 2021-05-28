package vn.tech.website.store.controller.admin;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vn.tech.website.store.controller.admin.auth.AuthFunctionController;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.entity.EFunction;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.util.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Getter
@Setter
public abstract class BaseController implements Serializable {
    @Inject
    @Named
    LanguageController languageController;
    @Inject
    AuthorizationController authorizationController;
    @Inject
    CommonController commonController;
    @Autowired
    AccountRepository accountRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected abstract EScope getMenuId();


    public void init() {
        // Check login
        if (!authorizationController.hasLogged()) {
            FacesUtil.redirect("/admin/login.xhtml");
            return;
        }
        // Check block account
        Account account = accountRepository.getAccountByAccountId(authorizationController.getAccountDto().getAccountId());
        if (account == null || Arrays.asList(DbConstant.ACCOUNT_DELETE_STATUS, DbConstant.ACCOUNT_LOCK_STATUS).contains(account.getStatus())) {
            authorizationController.resetAll();
            FacesUtil.redirect("/admin/login.xhtml");
            return;
        }
        // Check permission
//        if (!authorizationController.hasRole(getMenuId())) {
//            // redirect to access denied page
//            FacesUtil.redirect("/admin/errors/access.xhtml");
//        }
    }

    public void redirectToSuccessPage() {
        FacesUtil.redirect("/common/success.xhtml");
    }

    public void setErrorGlobal(String error) {
        FacesUtil.addErrorMessage(error);
    }

    public void setErrorForm(String error) {
        FacesUtil.addErrorMessage(Constant.ERROR_MESSAGE_ID, error);
    }

    public void setSuccessGlobal(String msg) {
        FacesUtil.addSuccessMessage(msg);
    }

    public void setSuccessForm(String msg) {
        FacesUtil.addSuccessMessage(Constant.ERROR_MESSAGE_ID, msg);
    }

    public void setSuccessForm(String title, String msg) {
        FacesUtil.addSuccessMessage(Constant.ERROR_MESSAGE_ID, title, msg);
        FacesUtil.updateView(Constant.ERROR_GROWL_DETAIL_ID);
    }

    public boolean isPdfFile(String fileName) {
        return FileUtil.isPdfFileExt(fileName);
    }

    public String getTextMessage(String key) {
        return languageController.getTextMap().get(key);
    }

    public String getErrorMessage(String key) {
        return languageController.getErrorMap().get(key);
    }

    public StreamedContent getFileDownload(String filePath) {
        StreamedContent streamedContent = FileUtil.getDownloadFileFromDatabase(filePath);
        if (streamedContent == null) {
            FacesUtil.addErrorMessage(Constant.ERROR_MESSAGE_ID, "File không tồn tại");
        }
        return streamedContent;
    }

    public StreamedContent getFileDownload(String filePath, String fileName) {
        StreamedContent streamedContent = FileUtil.getDownloadFileFromDatabase(filePath, fileName);
        if (streamedContent == null) {
            FacesUtil.addErrorMessage(Constant.ERROR_MESSAGE_ID, "File không tồn tại");
        }
        return streamedContent;
    }

    public String convertDatetimeToHour(Date date) {
        return DateUtil.formatToPattern(date, "HH");
    }

    public String convertDatetimeToMinute(Date date) {
        return DateUtil.formatToPattern(date, "mm");
    }

    public void redirectToError() {
        FacesUtil.redirect("/admin/errors/error.xhtml");
    }

}
