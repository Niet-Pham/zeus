package vn.tech.website.store.dto.common;

import java.util.List;

public class UploadSingleFileDto {
    private String imageToShow;
    private String imageToAdd;
    private List<String> listToDelete;

    public String getImageToShow() {
        return imageToShow;
    }

    public void setImageToShow(String imageToShow) {
        this.imageToShow = imageToShow;
    }

    public String getImageToAdd() {
        return imageToAdd;
    }

    public void setImageToAdd(String imageToAdd) {
        this.imageToAdd = imageToAdd;
    }

    public List<String> getListToDelete() {
        return listToDelete;
    }

    public void setListToDelete(List<String> listToDelete) {
        this.listToDelete = listToDelete;
    }
}
