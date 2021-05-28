package vn.tech.website.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import vn.tech.website.store.util.PropertiesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Pagination<T> {
    private int rowIndex;
    private int rowCount;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int pageTotal;
    private String pageOption = "10,20,50,100";
    private List<T> data;
    private String dataString;
    private LazyDataModel<T> lazyDataModel;

    private final int PAGE_PREFIX = 2;

    public Pagination() {
        lazyDataModel = new LazyDataModel<T>() {
        };
        String pg = PropertiesUtil.getProperty("pagination.page.sizes");
        if (StringUtils.isNotBlank(pg)) {
            pageOption = pg;
        }
        resetAll();
    }

    public Pagination(LazyDataModel<T> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public void resetAll() {
        rowIndex = 0;
        rowCount = 0;
        pageIndex = 1;
    }

    public void getParameter() {
        rowIndex = (pageIndex - 1) * pageSize;
    }

    public void loadData() {
        data = lazyDataModel.load(rowIndex, pageSize, null, null, null);
        rowCount = lazyDataModel.getRowCount();
        pageTotal = (int) Math.ceil((double) rowCount / pageSize);
    }

    public String getDataString() {
        if (data == null || data.isEmpty() || data.get(0) == null) {
            return "[]";
        }
        dataString = data.get(0).toString();
        return dataString;
    }

    public List<Integer> pagePrev() {
        List<Integer> prev = new ArrayList<>();
        int temp = pageIndex;
        if (pageIndex <= 1) {
            return prev;
        }
        do {
            prev.add(--temp);
        } while (temp > 1 && prev.size() < PAGE_PREFIX);
        Collections.reverse(prev);
        return prev;
    }

    public List<Integer> pageNext() {
        List<Integer> next = new ArrayList<>();
        int temp = pageIndex;
        if (pageIndex >= pageTotal) {
            return next;
        }
        do {
            next.add(++temp);
        } while (temp < pageTotal && next.size() < PAGE_PREFIX);
        return next;
    }

    public List<String> getPageOptionList() {
        return Arrays.asList(pageOption.split(","));
    }

    public boolean isShowPageNext() {
        return getPageIndex() < pageTotal;
    }

    public boolean isShowPagePrev() {
        return getPageIndex() > 1;
    }

    public boolean isShow3DotNext() {
        return getPageIndex() + PAGE_PREFIX < pageTotal;
    }

    public boolean isShow3DotPrev() {
        return getPageIndex() - PAGE_PREFIX > 1;
    }
}
