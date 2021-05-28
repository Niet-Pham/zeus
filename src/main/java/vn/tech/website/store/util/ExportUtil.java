package vn.tech.website.store.util;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.tech.website.store.dto.ExportOrderDto;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportUtil {

    private static Logger log = LoggerFactory.getLogger(ExportUtil.class);

    private ExportUtil() {
    }

    public static StreamedContent downloadExcelOrdersDetailFile(
            ExportOrderDto reportExcelDto, String fileName, String templeteFile,
            String reportFile) throws IOException {

        ServletContext servletContext = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext());
        // Lấy file mẫu
        String templateFilePath = servletContext.getRealPath(templeteFile);
        // File report sẽ sinh ra
        String reportFilePath = servletContext.getRealPath(reportFile);

        Context context = new Context();
        context.putVar("item", reportExcelDto);
        JxlsHelper helper = JxlsHelper.getInstance();
        helper.setProcessFormulas(false);
        helper.processTemplate(new FileInputStream(templateFilePath), new FileOutputStream(reportFilePath), context);
        InputStream osStream = new FileInputStream(reportFilePath);
        return new DefaultStreamedContent(osStream, "application/vnd.ms-excel", fileName);
    }

    public static StreamedContent downloadExcelFile(String fileName, String templateFile,
                                                    String reportFile) throws IOException {

        ServletContext servletContext = ((ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext());
        // Lấy file mẫu
        String templateFilePath = servletContext.getRealPath(templateFile);
        // File report sẽ sinh ra
        String reportFilePath = servletContext.getRealPath(reportFile);

        Context context = new Context();
        context.putVar("item", null);
        JxlsHelper helper = JxlsHelper.getInstance();
        helper.setProcessFormulas(false);
        helper.processTemplate(new FileInputStream(templateFilePath), new FileOutputStream(reportFilePath), context);
        InputStream osStream = new FileInputStream(reportFilePath);
        return new DefaultStreamedContent(osStream, "application/vnd.ms-excel", fileName);
    }

    public static String getFileNameExport(String prefix) {
        try {
            Date date = new Date();
            return prefix
                    + "_"
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(date)
                    + ".xlsx";
        } catch (Exception e) {
            log.error("Format date error", e);
            return "ReportExportFile.xlsx";
        }
    }
}
