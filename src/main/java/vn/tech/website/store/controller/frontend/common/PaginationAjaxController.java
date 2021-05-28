package vn.tech.website.store.controller.frontend.common;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import vn.tech.website.store.entity.Pagination;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class PaginationAjaxController<T> extends Pagination<T> {
    private HttpServletRequest requestTemp;

    private final int PAGE_PREFIX = 2;

    public PaginationAjaxController() {
        super();
    }

    public PaginationAjaxController(LazyDataModel<T> lazyDataModel) {
        super(lazyDataModel);
    }

    public void onChangePageSize() {
        onChangePage(1);
    }

    public void onChangePage(int pageIndex) {
        onChangeFirstPage(pageIndex);
        loadData();
    }

    public void onChangeFirstPage(int pageIndex) {
        setPageIndex(pageIndex);
        super.getParameter();
    }

    public void onChangePagePrev() {
        if (getPageIndex() <= 1) {
            return;
        }
        onChangePage(getPageIndex() - 1);
    }

    public void onChangePageNext() {
        if (getPageIndex() >= getPageTotal()) {
            return;
        }
        onChangePage(getPageIndex() + 1);
    }
}
