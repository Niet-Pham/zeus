package vn.tech.website.store.dto.common;

import java.util.Map;

public class UploadMultipleFileNameDto {
    private Map<String, String> listToShow; // fileId,name
    private Map<String, String> listToAdd;
    private Map<String, String> listToDelete;

    public Map<String, String> getListToShow() {
        return listToShow;
    }

    public void setListToShow(Map<String, String> listToShow) {
        this.listToShow = listToShow;
    }

    public Map<String, String> getListToAdd() {
        return listToAdd;
    }

    public void setListToAdd(Map<String, String> listToAdd) {
        this.listToAdd = listToAdd;
    }

    public Map<String, String> getListToDelete() {
        return listToDelete;
    }

    public void setListToDelete(Map<String, String> listToDelete) {
        this.listToDelete = listToDelete;
    }
}
