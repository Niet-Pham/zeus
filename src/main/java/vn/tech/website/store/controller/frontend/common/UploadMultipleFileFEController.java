package vn.tech.website.store.controller.frontend.common;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.dto.common.UploadMultipleFileNameDto;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.FileUtil;

import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Named
@Scope(value = "session")
public class UploadMultipleFileFEController extends BaseFEController {
    private static Logger log = LoggerFactory.getLogger(UploadMultipleFileFEController.class);

    private UploadMultipleFileNameDto uploadMultipleFileNameDto;

    public void resetAll(Map<String, String> listFiles) {
        uploadMultipleFileNameDto = new UploadMultipleFileNameDto();
        if (listFiles == null) {
            listFiles = new LinkedHashMap<>();
        }
        uploadMultipleFileNameDto.setListToShow(listFiles);
        uploadMultipleFileNameDto.setListToAdd(new LinkedHashMap<>());
        uploadMultipleFileNameDto.setListToDelete(new LinkedHashMap<>());
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
            if (uploadMultipleFileNameDto.getListToShow().containsValue(e.getFile().getFileName())) {
                setErrorForm("Tên file đã tồn tại, vui lòng chọn file khác");
                return;
            }

            String image = FileUtil.saveFile(e.getFile());
            uploadMultipleFileNameDto.getListToShow().put(image, e.getFile().getFileName());
            uploadMultipleFileNameDto.getListToAdd().put(image, e.getFile().getFileName());
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }

    public void onRemoveFile(String image) {
        // Nếu chưa có trong database
        uploadMultipleFileNameDto.getListToShow().remove(image);
        if (uploadMultipleFileNameDto.getListToAdd().containsKey(image)) {
            uploadMultipleFileNameDto.getListToAdd().remove(image);
        } else {
            uploadMultipleFileNameDto.getListToDelete().put(image, uploadMultipleFileNameDto.getListToAdd().get(image));
        }
    }

    @Override
    protected String getMenuId() {
        return null;
    }
}
