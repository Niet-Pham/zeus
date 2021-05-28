package vn.tech.website.store.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class CommonUtil {

    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);

    public static boolean saveFile(String todayFolder, String id, UploadedFile uploadedFile) {
        String strFolder = PropertiesUtil.getProperty("upload_folder");
        File inFiles = new File(strFolder + todayFolder + File.separator);
        if (!inFiles.exists() && !inFiles.mkdirs()) {
            log.error("Can't create folder");
        }
        File file = new File(getFullFileUrl(todayFolder, id, uploadedFile.getFileName()));
        try {
            if (file.exists()) {
                file.delete();
            }
            FileUtils.copyInputStreamToFile(uploadedFile.getInputstream(), file);
            return true;
        } catch (IOException e) {
            log.error("Save file error", e);
            return false;
        }
    }

    public static String getFullFileUrl(String todayFolder, String id, String fileName) {
        return PropertiesUtil.getProperty("upload_folder") + todayFolder + File.separator
                + id + "." + FilenameUtils.getExtension(fileName);
    }

    public static String generateFileId() {
        return DateUtil.getCurrentDateStr();
    }
}
