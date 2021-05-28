package vn.tech.website.store.model;

import java.util.ArrayList;
import java.util.List;

public class CommonPagination<T> {

    private List<T> list = new ArrayList<T>();

    public List<T> fetchCurrentList(int from, int to) {
        return this.list.subList(from, to);
    }

    public int getListSize() {
        return this.list.size();
    }
}
