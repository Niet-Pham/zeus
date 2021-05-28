package vn.tech.website.store.controller.admin.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.FacesUtil;
import vn.tech.website.store.util.FileUtil;

import javax.inject.Named;

@Getter
@Setter
@Named
@Scope(value = "session")
public class UploadSingleImageController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(UploadSingleImageController.class);

    private String imagePath;
    private boolean showDeleteButton;

    public void resetAll(String image) {
        showDeleteButton = !StringUtils.isBlank(image);
        imagePath = image;
    }

    public void onUploadImage(FileUploadEvent e) {
        try {
            if (e.getFile().getSize() > Constant.MAX_FILE_SIZE) {
                setErrorForm("Dung lượng file quá lớn. Dung lượng tối đa " + Constant.MAX_FILE_SIZE / 1000000 + "Mb");
                return;
            }
            imagePath = FileUtil.saveImageFile(e.getFile());
            showDeleteButton = true;
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }

    public void onRemoveImage(String image) {
        // Nếu chưa có trong database
        imagePath = null;
        showDeleteButton = false;
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
