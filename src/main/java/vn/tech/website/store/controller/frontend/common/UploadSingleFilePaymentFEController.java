package vn.tech.website.store.controller.frontend.common;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.dto.common.UploadSingleFileFEDto;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.FileUtil;

import javax.inject.Named;

@Getter
@Setter
@Named
@Scope(value = "session")
public class UploadSingleFilePaymentFEController extends BaseFEController {
    private static Logger log = LoggerFactory.getLogger(UploadSingleFilePaymentFEController.class);

    private UploadSingleFileFEDto uploadSingleFileFEDto;

    public void resetAll(UploadSingleFileFEDto imageFile) {
        uploadSingleFileFEDto = new UploadSingleFileFEDto();
        if (imageFile == null) {
            imageFile = new UploadSingleFileFEDto();
        }
        BeanUtils.copyProperties(imageFile, uploadSingleFileFEDto);
    }

    public void onUploadFile(FileUploadEvent e) {
        try {
            if (!FileUtil.isAcceptImageType(e.getFile())) {
                setErrorForm("Loại file không đúng định dạng. Bạn vui lòng chọn file ảnh có định dạng: " + FileUtil.getAcceptFileImageString().replaceAll(",", ", ") + " có dung lượng nhỏ hơn 5 MB");
                return;
            }
            if (e.getFile().getSize() > Constant.MAX_FILE_SIZE) {
                setErrorForm("Dung lượng file quá lớn. Dung lượng tối đa " + Constant.MAX_FILE_SIZE / 500000 + "Mb");
                return;
            }

            uploadSingleFileFEDto.setFilePath(FileUtil.saveFile(e.getFile()));
            uploadSingleFileFEDto.setFileName(e.getFile().getFileName());
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }

    public void onUploadPdfFile(FileUploadEvent e) {
        try {
            if (!FileUtil.isAcceptFilePDFType(e.getFile())) {
                setErrorForm("Loại file không được phép. Bạn vui lòng chọn file: " + FileUtil.getAccpetFilePDFString().replaceAll(",", ", ") + " có dung lượng nhỏ hơn 10 MB");
                return;
            }
            if (e.getFile().getSize() > Constant.MAX_FILE_SIZE) {
                setErrorForm("Dung lượng file quá lớn. Dung lượng tối đa " + Constant.MAX_FILE_SIZE / 1000000 + "Mb");
                return;
            }

            uploadSingleFileFEDto.setFilePath(FileUtil.saveFile(e.getFile()));
            uploadSingleFileFEDto.setFileName(e.getFile().getFileName());
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }

    public void onRemoveFile() {
        // Nếu chưa có trong database
        uploadSingleFileFEDto = new UploadSingleFileFEDto();
    }

    public String getAcceptFileImageString() {
        return FileUtil.getAcceptFileImageString();
    }

    public String getAcceptFileString() {
        return FileUtil.getAcceptFileString();
    }

    public String getAccpetFilePDFString() {
        return FileUtil.getAccpetFilePDFString();
    }

    @Override
    protected String getMenuId() {
        return null;
    }
}
