package vn.tech.website.store.util;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * @author anhlt
 */
public class FacesUtil {

    /**
     * Add add message
     *
     * @param message  the message would be displayed
     * @param severity severity
     */
    private static void addMessage(String message, Severity severity) {
        FacesMessage facesMsg = new FacesMessage(severity, message, message);
        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(null, facesMsg);
    }

    /**
     * Add error message
     *
     * @param msg the error message
     */
    public static void addErrorMessage(String msg) {
        addMessage(msg, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Add error message, has title of message
     *
     * @param title   tiêu đề thông báo lỗi
     * @param content nội dung lỗi
     */
    public static void addErrorMsg(String title, String content) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, content));
    }

    /**
     * Add error message, has title of message
     *
     * @param msg   the error message
     * @param title title of message
     */
    public static void addErrorMsg(String controlId, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(controlId, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, msg));
    }

    /**
     * Add error message theo control ui
     *
     * @param controlId id của ui component
     * @param msg
     */
    public static void addErrorMessage(String controlId, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(controlId, facesMsg);
    }

    public static boolean isExistErrorMessage(String controlId) {
        return FacesContext.getCurrentInstance().getMessageList(controlId).size() > 0;
    }

    /**
     * Add success message
     *
     * @param msg the success message
     */
    public static void addSuccessMessage(String msg) {
        addMessage(msg, FacesMessage.SEVERITY_INFO);
    }

//    /**
//     * @param title   tiêu đề thông báo
//     * @param content nội dung thông báo
//     */
//    public static void addSuccessMsg(String title, String content) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, content));
//    }

    public static void addSuccessMessage(String controlId, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(controlId, facesMsg);
    }

    public static void addSuccessMessage(String controlId, String title, String message) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, title, message);
        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(controlId, facesMsg);
    }

    /**
     * Add warning message
     *
     * @param msg the warning message
     */
    public static void addWarningMessage(String msg) {
        addMessage(msg, FacesMessage.SEVERITY_WARN);
    }

    /**
     * Add warning message
     *
     * @param msg the warning message
     */
    public static void addWarningMsg(String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, title, msg));
    }

    /**
     * Get servlet request based on facesContext
     *
     * @return instance of servlet request
     */
    public static HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    /**
     * Get request parameter
     *
     * @param name the parameter name
     * @return value of request parameter
     */
    public static String getRequestParameter(String name) {
        String value = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get(name);
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * Get servlet context based on current instance of faces context
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    /**
     * Get context path based on current instance of faces context
     *
     * @return context path
     */
    public static String getContextPath() {
        return FacesUtil.getServletContext().getContextPath();
    }

    /**
     * Get servlet context based on current instance of faces context
     *
     * @return the servlet context
     */
    public static Map<String, String> getRequestParameterMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    }

    public static void resetDataTable(String formId, String dataTableId) {
        //reset table state
        DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":" + formId + ":" + dataTableId);
        table.setValueExpression("sortBy", null);
        table.setSortBy(null);
        table.reset();
    }

    public static void closeDialog(String widgetVar) {
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('" + widgetVar + "').hide();");
    }

    public static void updateView(String viewId) {
        PrimeFaces current = PrimeFaces.current();
        current.ajax().update(viewId);
    }

    public static void updateView(List<String> viewIds) {
        PrimeFaces current = PrimeFaces.current();
        current.ajax().update(viewIds);
    }

    public static void showDialog(String widgetVar) {
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('" + widgetVar + "').show();");
    }

    public static void redirect(String url) {
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            ((HttpServletRequest) FacesContext
                                    .getCurrentInstance().getExternalContext()
                                    .getRequest()).getContextPath()
                                    + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            ((HttpServletRequest) FacesContext
                                    .getCurrentInstance().getExternalContext()
                                    .getRequest()).getRequestURL().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void externalRedirect(String url) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRealPath(String path) {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath(path);
    }
}
