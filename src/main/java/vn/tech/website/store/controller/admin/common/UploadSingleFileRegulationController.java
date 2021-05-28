package vn.tech.website.store.controller.admin.common;

import org.apache.commons.lang3.StringUtils;
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
public class UploadSingleFileRegulationController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(UploadSingleFileRegulationController.class);

    private String filePath;
    private String fileName;

    public void resetAll(String filePath) {
        this.filePath = filePath;
        if (StringUtils.isNotBlank(filePath)) {
            this.fileName = FileUtil.getFilenameFromFilePath(filePath);
        } else {
            this.fileName = null;
        }
    }

    public void onUploadFilePDF(FileUploadEvent e) {
        try {
            if (!FileUtil.isAcceptFilePDFType(e.getFile())) {
                setErrorForm("Loại file không hợp lệ. Bạn vui lòng tải file quy chế có định dạng " + FileUtil.getAccpetFilePDFString().replace(",", ", "));
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

    public void onUploadFilePDFAndDocx(FileUploadEvent e) {
        try {
            if (!FileUtil.isAcceptFilePDFAndDocxType(e.getFile())) {
                setErrorForm("Loại file không hợp lệ. Bạn vui lòng tải file hướng dẫn thủ tục có định dạng " + FileUtil.getAccpetFilePDFAndDOCXString().replace(",", ", "));
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
        this.filePath = null;
        this.fileName = null;
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
