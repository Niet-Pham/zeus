package vn.tech.website.store.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityMapper {
    private static Logger log = LoggerFactory.getLogger(EntityMapper.class);

    private static final String FIELD_PATTERN = "\\sAS\\s[`|'|\"]?([a-z0-9\\-_]+)[`|'|\"]?[,|\\s]";
    private static final String FIELD_IGNORE_PATTERN = ".*(?i:tbl|tbl_tmp).*";

    public static List<?> mapper(Query query, String sqlString, Class<?> objectClass) {
        try {
            // declare variables
            List<Field> fieldList = new LinkedList<>();
            List<Object> outputs = new ArrayList<>();

            // find out fields
            Pattern FIELD_MATCHER = Pattern.compile(FIELD_PATTERN, Pattern.CASE_INSENSITIVE);
            Matcher m = FIELD_MATCHER.matcher(sqlString);
            while (m.find()) {
                String fieldName = m.group(1);
                if (!fieldName.matches(FIELD_IGNORE_PATTERN)) {
                    fieldList.add(getField(objectClass, fieldName));
                }
            }

            // execute query
            List<Object[]> result = query.getResultList();
            for (Object[] obj : result) {
                int i = 0;
                Object temp = objectClass.newInstance();
                for (Object o : obj) {
                    Field field = fieldList.get(i++);
                    field.set(temp, getValue(field, o));
                }
                outputs.add(temp);
            }

            return outputs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getValue(Field field, Object value) throws Exception {
        EnumClass enumClass = EnumClass.getClass(field.getType().getSimpleName());
        switch (enumClass) {
            case Integer:
                return ValueUtil.getIntegerByObject(value);
            case Long:
                return ValueUtil.getLongByObject(value);
            case Boolean:
                return ValueUtil.getBooleanByObject(value);
            case Double:
                return ValueUtil.getDoubleByObject(value);
            case Float:
                return ValueUtil.getFloatByObject(value);
            case String:
                return ValueUtil.getStringByObject(value);
            case Date:
                return ValueUtil.getDateByObject(value);
            default:
                return null;
        }
    }

    private static Field getField(Class<?> objectClass, String fieldName) throws Exception {
        try {
            Field field = objectClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            if (objectClass.getSuperclass() == null || "Object".equals(objectClass.getSuperclass().getTypeName())) {
                throw new Exception(e);
            }
            return getField(objectClass.getSuperclass(), fieldName);
        }
    }
}