package vn.tech.website.store.jsf;

import com.sun.faces.renderkit.html_basic.TextRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class InputRender extends TextRenderer {

    @Override
    protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws java.io.IOException {

        String [] attributes = {"placeholder", "required", "oninput", "autocomplete", "data-loading-text"};

        ResponseWriter writer = context.getResponseWriter();
        for(String attribute : attributes) {
            Object value = component.getAttributes().get(attribute);
            if(value != null) {
                writer.writeAttribute(attribute, value, attribute);
            }
        }
        super.getEndTextToRender(context, component, currentValue);
    }

}