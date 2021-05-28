package vn.tech.website.store.util;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wickedsource.docxstamper.DocxStamper;
import org.wickedsource.docxstamper.DocxStamperConfiguration;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
@Getter
@Setter
public class ExportWordUtil<T> {

    private static Logger log = LoggerFactory.getLogger(ExportWordUtil.class);
    private static final String CONTEXT_PATH_TEMPLATE = "/WEB-INF/template/";
    private static final String CONTEXT_PATH_REPORT = "/WEB-INF/report/";

    public ExportWordUtil() {
    }


    public StreamedContent downloadWordFile(T docDto, String fileName, String templeteFileName) {

        ServletContext servletContext = ((ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext());
        // Lấy file mẫu
        String templateFilePath = servletContext.getRealPath(CONTEXT_PATH_TEMPLATE + templeteFileName);
        // File report sẽ sinh ra
        String reportFilePath = servletContext.getRealPath(CONTEXT_PATH_REPORT + fileName + ".docx");

        DocxStamper<T> stamper = new DocxStamper<>(new DocxStamperConfiguration());
        try {
            stamper.stamp(new FileInputStream(templateFilePath), docDto, new FileOutputStream(reportFilePath));
            InputStream osStream = new FileInputStream(reportFilePath);
            return new DefaultStreamedContent(osStream, "application/vnd.ms-word", getFileNameExport(fileName));
        } catch (FileNotFoundException e) {
            log.error("Export word error", e);
        }
        return null;
    }

    private String getFileNameExport(String prefix) {
        try {
            Date date = new Date();
            return prefix
                    + "_"
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(date)
                    + ".docx";
        } catch (Exception e) {
            log.error("Format date error", e);
            return "ReportExportFile.docx";
        }
    }
}
