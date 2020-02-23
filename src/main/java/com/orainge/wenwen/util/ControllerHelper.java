package com.orainge.wenwen.util;

import com.alibaba.druid.util.StringUtils;

import java.util.Map;

public class ControllerHelper {
    public static Object[] getMapParamtersInObject(Map<String, Object> map, String... paramters) throws NullParmatersException {
        if (paramters.length == 0) {
            throw new NullParmatersException();
        }

        Object[] object = new Object[paramters.length];
        if (map == null) {
            throw new NullParmatersException(true, "JSON数据");
        } else {
            // 测试参数能不能取出来
            for (int i = 0; i < paramters.length; i++) {
                object[i] = map.get(paramters[i]);
                if (object[i] == null) {
                    throw new NullParmatersException(true, paramters[i]);
                }
            }
        }
        return object;
    }

    public static Integer[] getMapParamtersInInteger(Map<String, Integer> map, String... paramters) throws NullParmatersException {
        if (paramters.length == 0) {
            throw new NullParmatersException();
        }

        Integer[] object = new Integer[paramters.length];
        if (map == null) {
            throw new NullParmatersException(true, "JSON数据");
        } else {
            // 测试参数能不能取出来
            for (int i = 0; i < paramters.length; i++) {
                object[i] = map.get(paramters[i]);
                if (object[i] == null) {
                    throw new NullParmatersException(true, paramters[i]);
                }
            }
        }
        return object;
    }

    public static String[] getMapParamtersInString(Map<String, String> map, String... paramters) throws NullParmatersException {
        if (paramters.length == 0) {
            throw new NullParmatersException();
        }

        String[] object = new String[paramters.length];
        if (map == null) {
            throw new NullParmatersException(true, "JSON数据");
        } else {
            // 测试参数能不能取出来
            for (int i = 0; i < paramters.length; i++) {
                object[i] = map.get(paramters[i]);
                if (object[i] == null) {
                    throw new NullParmatersException(true, paramters[i]);
                }
            }
        }
        return object;
    }

    public static void validToken(String token) throws NullParmatersException {
        if (StringUtils.isEmpty(token)) {
            throw new NullParmatersException(true, "token");
        }
    }
}