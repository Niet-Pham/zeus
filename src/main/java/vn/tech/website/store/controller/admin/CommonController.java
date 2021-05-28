/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.tech.website.store.controller.admin;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.FacesUtil;
import vn.tech.website.store.util.FileUtil;
import vn.tech.website.store.util.PropertiesUtil;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Named
@Getter
@Setter
@Scope(value = "session")
public class CommonController implements Serializable {
    @Autowired
    private HttpServletRequest request;
    private String yearNow;

    public static String abbreviate(String text, int size) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        if (text.length() > size) {
            return text.substring(0, size).trim() + "...";
        }
        return text;
    }

    public boolean isPdfFile(String fileName) {
        return FileUtil.isPdfFileExt(fileName);
    }

    public boolean isOfficeFile(String fileName) {
        return FileUtil.isOfficeFileExt(fileName);
    }

    public boolean isImageFile(String fileName) {return FileUtil.isAcceptFileImageType(fileName);}

    public StreamedContent getFileDownload(String fileName) {
        StreamedContent streamedContent = FileUtil.getDownloadFileFromDatabase(fileName);
        if (streamedContent == null) {
            FacesUtil.addErrorMessage(Constant.ERROR_MESSAGE_ID, "File không tồn tại");
        }
        return streamedContent;
    }

    public String getCurrentDomain() {
        return String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()
        );
    }

    // tải về file đính kèm lý do truất quyền
    public void download(String file) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.reset();
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + file);
            OutputStream responseOutputStream = response.getOutputStream();
            InputStream fileInputStream = FileUtil.getDownloadFileFromDatabase(file).getStream();
            byte[] bytesBuffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) {
                responseOutputStream.write(bytesBuffer, 0, bytesRead);
            }
            responseOutputStream.flush();
            fileInputStream.close();
            responseOutputStream.close();
            facesContext.responseComplete();
        } catch (Exception e) {
            FacesUtil.redirect("/error/404.xhtml");
        }
    }

    public void download(String file, String userFileName) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.reset();
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + userFileName);
            OutputStream responseOutputStream = response.getOutputStream();
            InputStream fileInputStream = FileUtil.getDownloadFileFromDatabase(file).getStream();
            byte[] bytesBuffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) {
                responseOutputStream.write(bytesBuffer, 0, bytesRead);
            }
            responseOutputStream.flush();
            fileInputStream.close();
            responseOutputStream.close();
            facesContext.responseComplete();
        } catch (Exception e) {
            FacesUtil.redirect("/error/404.xhtml");
        }
    }

    public String showAddress(String diaChi, String phuongXa, String quanHuyen, String tinhTp) {
        List<String> list = new ArrayList<>(Arrays.asList(
                diaChi,
                phuongXa,
                quanHuyen,
                tinhTp
        ));
        list.removeIf(StringUtils::isBlank);

        return String.join(", ", list);
    }

    public String statusDeposit(boolean byStoreeer, boolean retract) {
        if (retract) {
            return " do rút lại giá đã trả";
        }
        if (!byStoreeer) {
            return " do không trả giá vòng trước";
        } else {
            return " bởi đấu giá viên";
        }
    }

    public String upperCaseFirstChar(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public int getYearNow() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public String buildURLImage(String imagePath) {
        String domain = PropertiesUtil.getProperty("domain.static.context");
        return domain + imagePath;
    }
}
