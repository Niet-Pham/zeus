package vn.tech.website.store.controller.frontend;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.common.FacesNoticeController;
import vn.tech.website.store.repository.AccountRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Log4j2
@Named
@Getter
@Setter
@Scope(value = "session")
public class CommonFEController implements Serializable {
    @Inject
    private AuthorizationFEController authorizationFEController;
    @Inject
    private FacesNoticeController facesNoticeController;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AccountRepository accountRepository;

}
