package vn.tech.website.store.util;

import java.util.Date;

public class ValueUtil {

    private ValueUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getStringByObject(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Date getDateByObject(Object obj) {
        if (obj == null) {
            return null;
        }
        return (Date) obj;
    }

    public static Long getLongByObject(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return null;
        }
        return Long.valueOf(obj.toString());
    }

    public static Double getDoubleByObject(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return null;
        }
        return Double.valueOf(obj.toString());
    }

    public static Float getFloatByObject(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return null;
        }
        return Float.valueOf(obj.toString());
    }

    public static Integer getIntegerByObject(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return null;
        }
        return Integer.valueOf(obj.toString());
    }

    public static Boolean getBooleanByObject(Object obj) {
        return (obj != null && "1,true".contains(obj.toString()));
    }

//    public static List<Long> getListByObject(Object obj){
//        if(obj == null || obj.toString().isEmpty()){
//            return new ArrayList<>();
//        }
//        String[] temp = obj.toString().split(",");
//        return Arrays.asList(obj.toString().split(","))
//                .stream()
//                .mapToLong(Long::parseLong)
//                .toArray();
//    }

}
