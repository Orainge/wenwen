package com.orainge.wenwen.exception.type;

public enum MongoDBError implements Error {
    INSERT_ERROR(0, "插入"),
    DELETE_ERROR(1, "删除"),
    UPDATE_ERROR(2, "修改"),
    QUERY_ERROR(3, "查询");

    private Integer code;
    private String type;

    private MongoDBError(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getType() {
        return type;
    }
}
