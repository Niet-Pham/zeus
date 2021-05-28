package vn.tech.website.store.controller.frontend.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.entity.Pagination;
import vn.tech.website.store.util.FacesUtil;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Named
@Scope(value = "session")
@Getter
@Setter
public class PaginationController<T> extends Pagination<T> {

    private HttpServletRequest request;
    private int pageTemp;
    private final int PAGE_PREFIX = 2;

    public PaginationController() {
        super();
    }

    public PaginationController(LazyDataModel<T> lazyDataModel) {
        super(lazyDataModel);
    }

    public void onChangePageSize() {
        setRowCount(getLazyDataModel().getRowCount());
        setPageTotal((int) Math.ceil((double) getRowCount() / getPageSize()));
        String url = FacesUtil.getServletRequest().getRequestURI().replace(FacesUtil.getContextPath(), "");
        String param = getAllParameter();
        FacesUtil.redirect(url + (StringUtils.isNotBlank(param) ? "?" + param : ""));
    }

    public void getParameter() {
        getPageIndex();
        super.getParameter();
    }

    public void loadData() {
        pageTemp = super.getPageIndex();
        getParameter();
        super.loadData();
    }

    public int getPageIndex() {
        HttpServletRequest req = FacesUtil.getServletRequest();
        try {
            int p = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
            if (p <= 0 || p > getPageTotal()) {
                p = 1;
            }
            setPageIndex(p);
        } catch (Exception ignored) {
            resetAll();
        }
        return super.getPageIndex();
    }

    public String getAllParameter() {
        // get all parameter names
        Set<String> paramNames = request.getParameterMap().keySet();
        List<String> output = new ArrayList<>();

        // iterating over parameter names and get its value
        for (String name : paramNames) {
            if (name.equals("page") || name.matches("javax.*|.*j_id.*")) {
                continue;
            }

            String value = request.getParameter(name);
            if ("pageParam".equals(name) &&  value != null) {
                output.add(value);
            } else {
                output.add(name + "=" + value);
            }
        }

        // output
        return String.join("&", output);
    }

    public String buildParameter(int page) {
        String param = getAllParameter();
        if (StringUtils.isBlank(param)) {
            return "?page=" + page;
        }
        return "?" + param + "&page=" + page;
    }
}
