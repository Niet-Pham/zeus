package vn.tech.website.store.controller.admin.common;

import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.FileUtil;

import javax.inject.Named;

@Named
@Scope(value = "session")
public class UploadSingleFileController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(UploadSingleFileController.class);

    private String filePath;
    private String fileName;

    public void resetAll(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void onUploadFile(FileUploadEvent e) {
        try {
            if (!FileUtil.isAcceptFileType(e.getFile())) {
                setErrorForm("Loại file không được phép. Những file được phép " + FileUtil.getAcceptFileString().replaceAll(",", ", ").toUpperCase());
                return;
            }
            if (e.getFile().getSize() > Constant.MAX_FILE_SIZE) {
                setErrorForm("Dung lượng file quá lớn. Dung lượng tối đa " + Constant.MAX_FILE_SIZE / 1000000 + "Mb");
                return;
            }
            filePath = FileUtil.saveFile(e.getFile());
            fileName = e.getFile().getFileName();
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }

    public void onRemoveFile() {
        // Nếu chưa có trong database
        this.filePath = "";
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
