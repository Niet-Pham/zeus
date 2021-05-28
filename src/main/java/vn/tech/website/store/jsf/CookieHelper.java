package vn.tech.website.store.jsf;

import org.springframework.util.StringUtils;
import vn.tech.website.store.util.PropertiesUtil;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieHelper {

    public static void setCookie(String name, String value) {
        setCookie((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(),
                name,
                value,
                Integer.parseInt(PropertiesUtil.getProperty("cookie.auth.expire")));
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        try {
            Cookie cookie = new Cookie(name, encodeCookieValue(value));
            cookie.setMaxAge(expiry);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {

        }
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (name.equals(cookie.getName())) {
                        return decodeCookieValue(cookie.getValue());
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void removeCookie(String name) {
        removeCookie((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse(), name);
    }

    public static void removeCookie(HttpServletResponse response, String name) {
        setCookie(response, name, null, 0);
    }

    private static String encodeCookieValue(String value) {
        try {
            if (StringUtils.isEmpty(value)) return "";
            return URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    private static String decodeCookieValue(String value) {
        try {
            if (StringUtils.isEmpty(value)) return "";
            return URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }
}
