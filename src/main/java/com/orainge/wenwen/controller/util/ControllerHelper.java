package com.orainge.wenwen.controller.util;

import com.alibaba.druid.util.StringUtils;

import java.util.Map;

public class ControllerHelper {
    public static Object[] getParametersInObject(Map<String, Object> map, String... parameters) throws NullParametersException {
        if (parameters.length == 0) {
            throw new NullParametersException();
        }

        Object[] object;
        if (map == null || map.isEmpty()) {
            throw new NullParametersException(true, "JSON数据");
        } else {
            // 测试参数能不能取出来
            object = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                object[i] = map.get(parameters[i]);
                if (object[i] == null) {
                    throw new NullParametersException(true, parameters[i]);
                }
            }
        }
        return object;
    }

    public static Integer[] getParametersInInteger(Map<String, Integer> map, String... parameters) throws NullParametersException {
        if (parameters.length == 0) {
            throw new NullParametersException();
        }

        Integer[] object;
        if (map == null || map.isEmpty()) {
            throw new NullParametersException(true, "JSON数据");
        } else {
            // 测试参数能不能取出来
            object = new Integer[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                object[i] = map.get(parameters[i]);
                if (object[i] == null) {
                    throw new NullParametersException(true, parameters[i]);
                }
            }
        }
        return object;
    }

    public static String[] getParametersInString(Map<String, String> map, String... parameters) throws NullParametersException {
        if (parameters.length == 0) {
            throw new NullParametersException();
        }

        String[] object;
        if (map == null || map.isEmpty()) {
            throw new NullParametersException(true, "JSON数据");
        } else {
            // 测试参数能不能取出来
            object = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                object[i] = map.get(parameters[i]);
                if (object[i] == null) {
                    throw new NullParametersException(true, parameters[i]);
                }
            }
        }
        return object;
    }

    public static void validTokenIsExist(String token) throws NullParametersException {
        if (StringUtils.isEmpty(token)) {
            throw new NullParametersException(true, "token");
        }
    }
}