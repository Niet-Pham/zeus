package vn.tech.website.store.dto.common;

import java.util.Set;

public class UploadMultipleFileDto {
    private Set<String> listToShow;
    private Set<String> listToAdd;
    private Set<String> listToDelete;

    public Set<String> getListToShow() {
        return listToShow;
    }

    public void setListToShow(Set<String> listToShow) {
        this.listToShow = listToShow;
    }

    public Set<String> getListToAdd() {
        return listToAdd;
    }

    public void setListToAdd(Set<String> listToAdd) {
        this.listToAdd = listToAdd;
    }

    public Set<String> getListToDelete() {
        return listToDelete;
    }

    public void setListToDelete(Set<String> listToDelete) {
        this.listToDelete = listToDelete;
    }
}
