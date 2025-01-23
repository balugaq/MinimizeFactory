package io.github.ignorelicensescn.minimizefactory.datastorage.database.types;

public enum Column {
    KEY_LOCATION_VARCHAR("location",DataType.VARCHAR,80),
    VALUE_CORE_LOCATION_VARCHAR("core_location",DataType.VARCHAR,80),
    VALUE_DATA_BLOB("data",DataType.BLOB,0),
    VALUE_NODE_TYPE_INT("nodetype",DataType.INTEGER,5),
    VALUE_LOCK_STATUS_BOOLEAN("lock_status",DataType.BOOLEAN,0);

    public final String columnInnerName;
    public final DataType type;
    public final int length;

    Column(String columnInnerName, DataType type, int length){
        this.columnInnerName = columnInnerName;
        this.type = type;
        this.length = length;
    }

    public static final Column KEY_COLUMN = KEY_LOCATION_VARCHAR;


    public String columnStringForInitialize(String endWith){
        return columnStringForInitialize() + endWith;
    }

    public String columnStringForInitialize(){
        String result = "`"+ columnInnerName +"` " + type.name();
        if (length != 0){
            result += "("+length+")";
        }
        return result;
    }


}
