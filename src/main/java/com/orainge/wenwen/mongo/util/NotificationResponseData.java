package com.orainge.wenwen.mongo.util;

import java.util.List;

public class NotificationResponseData {
    private Integer type;
    private Integer subType;
    private List<Object> param;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public List<Object> getParam() {
        return param;
    }

    public void setParam(List<Object> param) {
        this.param = param;
    }
}
