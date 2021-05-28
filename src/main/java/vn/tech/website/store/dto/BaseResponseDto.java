package vn.tech.website.store.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.lang.reflect.Field;

@Getter
@Setter
public class BaseResponseDto {
    private static final long serialVersionUID = -1365232660431182216L;

    public ObjectNode buildObjectNode() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object object = field.get(this);
                objectNode.put(field.getName(), object instanceof String ? object.toString() : objectMapper.writeValueAsString(object));
            }
            return objectNode;
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
            return new ObjectMapper().createObjectNode();
        }
    }

}
