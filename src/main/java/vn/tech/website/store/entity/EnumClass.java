package vn.tech.website.store.entity;

public enum EnumClass {
    Integer,
    Long,
    Boolean,
    Double,
    Float,
    String,
    Date;

    public static EnumClass getClass(String type) throws Exception {
        switch (type) {
            case "Integer":
            case "int":
                return EnumClass.Integer;
            case "Long":
            case "long":
                return EnumClass.Long;
            case "Boolean":
            case "boolean":
                return EnumClass.Boolean;
            case "Double":
            case "double":
                return EnumClass.Double;
            case "Float":
            case "float":
                return EnumClass.Float;
            case "String":
                return EnumClass.String;
            case "Date":
                return EnumClass.Date;
            default:
                throw new Exception("No enum class defined!");
        }
    }
}
