package com.orainge.wenwen.controller.util;

import com.alibaba.druid.util.StringUtils;
import com.orainge.wenwen.exception.InvalidVariableException;
import com.orainge.wenwen.exception.NullRequestParametersException;

import java.util.Map;

public class ControllerHelper {
    public static Object[] getParametersInObject(Map<String, Object> map, String... parameters) throws NullRequestParametersException {
        if (parameters.length == 0) {
            throw new NullRequestParametersException();
        }

        Object[] object;
        if (map == null || map.isEmpty()) {
            throw new NullRequestParametersException("JSON数据");
        } else {
            // 测试参数能不能取出来
            object = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                object[i] = map.get(parameters[i]);
                if (object[i] == null) {
                    throw new NullRequestParametersException(parameters[i]);
                }
            }
        }
        return object;
    }

    public static Integer[] getParametersInInteger(Map<String, Integer> map, String... parameters) throws NullRequestParametersException {
        if (parameters.length == 0) {
            throw new NullRequestParametersException();
        }

        Integer[] object;
        if (map == null || map.isEmpty()) {
            throw new NullRequestParametersException("JSON数据");
        } else {
            // 测试参数能不能取出来
            object = new Integer[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                object[i] = map.get(parameters[i]);
                if (object[i] == null) {
                    throw new NullRequestParametersException(parameters[i]);
                }
            }
        }
        return object;
    }

    public static String[] getParametersInString(Map<String, String> map, String... parameters) throws NullRequestParametersException {
        if (parameters.length == 0) {
            throw new NullRequestParametersException();
        }

        String[] object;
        if (map == null || map.isEmpty()) {
            throw new NullRequestParametersException("JSON数据");
        } else {
            // 测试参数能不能取出来
            object = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                object[i] = map.get(parameters[i]);
                if (object[i] == null) {
                    throw new NullRequestParametersException(parameters[i]);
                }
            }
        }
        return object;
    }

    public static String[] getParametersInMultipartFile(Map<String, String> map, String... parameters) throws NullRequestParametersException {
        if (parameters.length == 0) {
            throw new NullRequestParametersException();
        }

        String[] object;
        if (map == null || map.isEmpty()) {
            throw new NullRequestParametersException("JSON数据");
        } else {
            // 测试参数能不能取出来
            object = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                object[i] = map.get(parameters[i]);
                if (object[i] == null) {
                    throw new NullRequestParametersException(parameters[i]);
                }
            }
        }
        return object;
    }

    public static void validVariableIsExist(String variable, String variableName) throws NullRequestParametersException {
        if (StringUtils.isEmpty(variable)) {
            throw new InvalidVariableException(variableName);
        }
    }

    public static void validVariableIsExist(Integer variable, String variableName) throws NullRequestParametersException {
        if (variable == null) {
            throw new InvalidVariableException(variableName);
        }
    }
}