package vn.tech.website.store.controller.frontend.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.FileUtil;

import javax.inject.Named;

@Getter
@Setter
@Named
@Scope(value = "session")
public class UploadSingleFileFEController extends BaseFEController {
    private static Logger log = LoggerFactory.getLogger(UploadSingleFileFEController.class);

    private String filePath;
    private String fileName;

    public void resetAll(String filePath) {
        this.filePath = filePath;
        if (StringUtils.isNotBlank(filePath)) {
            this.fileName = FileUtil.getFilenameFromFilePath(filePath);
        }
    }

    public void onUploadFile(FileUploadEvent e) {
        try {
            if (!FileUtil.isAcceptFileType(e.getFile())) {
                setErrorForm("Loại file không được phép. Những file được phép: " + FileUtil.getAcceptFileString().replaceAll(",", ", ").toUpperCase());
                return;
            }
            if (e.getFile().getSize() > Constant.MAX_FILE_SIZE) {
                setErrorForm("Dung lượng file quá lớn. Dung lượng tối đa " + Constant.MAX_FILE_SIZE / 1000000 + "Mb");
                return;
            }
            filePath = FileUtil.saveFile(e.getFile());
            fileName = FileUtil.getFilenameFromFilePath(filePath);
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }

    public void onRemoveFile() {
        // Nếu chưa có trong database
        this.filePath = "";
    }

    public String getAcceptFileString() {
        return FileUtil.getAcceptFileString();
    }

    @Override
    protected String getMenuId() {
        return null;
    }
}
