package vn.tech.website.store.controller.frontend;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vn.tech.website.store.controller.admin.CommonController;
import vn.tech.website.store.controller.admin.LanguageController;
import vn.tech.website.store.controller.frontend.common.FacesNoticeController;
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
public abstract class BaseFEController implements Serializable {
    @Inject
    @Named
    LanguageController languageController;
    @Inject
    AuthorizationFEController authorizationFEController;
    @Inject
    CommonController commonController;
    @Inject
    private FacesNoticeController facesNoticeController;
    @Autowired
    private AccountRepository accountRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract String getMenuId();

    public void init() {
        if (CollectionUtils.isEmpty(authorizationFEController.getMyMenus())) {
            authorizationFEController.resetAll();
        }
        // Check block account
        if (authorizationFEController.hasLogged()) {
            Account account = accountRepository.getAccountByAccountId(authorizationFEController.getAccountDto().getAccountId());
            if ((account == null || Arrays.asList(DbConstant.ACCOUNT_DELETE_STATUS, DbConstant.ACCOUNT_LOCK_STATUS).contains(account.getStatus()))) {
                authorizationFEController.resetAll();
                FacesUtil.redirect("/frontend/index2.xhtml");
                return;
            }
        }

//        if (!authorizationFEController.hasRole(getMenuId())) {
//            FacesUtil.redirect("/frontend/index.xhtml");
//            return;
//        }
    }

    public FacesNoticeController facesNotice() {
        return facesNoticeController;
    }

    public void redirectToSuccessPage() {
        FacesUtil.redirect("/common/success.xhtml");
    }

    public void setErrorGlobal(String error) {
        FacesUtil.addErrorMessage(error);
    }

    public void setErrorForm(String error) {
        facesNoticeController.addErrorMessage(error);
    }

    public void closeModal(String modalId) {
        facesNoticeController.closeModal(modalId);
    }

    public void setSuccessGlobal(String error) {
        FacesUtil.addSuccessMessage(error);
    }

    public void setSuccessForm(String error) {
        facesNoticeController.addSuccessMessage(error);
    }

    public void resetGoogleRecaptchaForm() {
        facesNoticeController.resetGoogleRecaptchaRegister();
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

    public StreamedContent getFileDownload(String fileName) {
        StreamedContent streamedContent = FileUtil.getDownloadFileFromDatabase(fileName);
        if (streamedContent == null) {
            FacesUtil.addErrorMessage(Constant.ERROR_MESSAGE_ID, "File không tồn tại");
        }
        return streamedContent;
    }

}


