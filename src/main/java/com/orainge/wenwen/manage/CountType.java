package com.orainge.wenwen.manage;

public class CountType {
    ;

    private Integer type;
    private Integer subType;
    private String name;

    private CountType(Integer type, Integer subType, String name) {
        this.type = type;
        this.subType = subType;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public Integer getSubType() {
        return subType;
    }

    public String getName() {
        return name;
    }
}
