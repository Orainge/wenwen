package com.orainge.wenwen.service.impl;

import com.orainge.wenwen.mongo.util.NotificationHelper;
import com.orainge.wenwen.mongo.util.NotificationResponseData;
import com.orainge.wenwen.service.NotificationService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationHelper notificationHelper;

    @Override
    public Response toLiteFeed(Integer userId, Integer page) {
        Response response = new Response();
        List<NotificationResponseData> list = notificationHelper.getFeed(userId, page);
        Map<String, Object> result = new HashMap<String, Object>();
        if (list == null) {
            result.put("end", 1);
        } else {
            result.put("data", list);
            if (list.size() < NotificationHelper.DEFAULT_PAGE_SIZE) {
                result.put("end", 1);
            }
        }
        response.setData(result);
        return response;
    }

    @Override
    public Response toLiteMessage(Integer userId, Integer page) {
        Response response = new Response();
        List<NotificationResponseData> list = notificationHelper.getMessage(userId, page);
        Map<String, Object> result = new HashMap<String, Object>();
        if (list == null) {
            result.put("end", 1);
        } else {
            result.put("data", list);
            if (list.size() < NotificationHelper.DEFAULT_PAGE_SIZE) {
                result.put("end", 1);
            }
        }
        response.setData(result);
        return response;
    }

    @Override
    public Response toLiteLike(Integer userId, Integer page) {
        Response response = new Response();
        List<NotificationResponseData> list = notificationHelper.getLike(userId, page);
        Map<String, Object> result = new HashMap<String, Object>();
        if (list == null) {
            result.put("end", 1);
        } else {
            result.put("data", list);
            if (list.size() < NotificationHelper.DEFAULT_PAGE_SIZE) {
                result.put("end", 1);
            }
        }
        response.setData(result);
        return response;
    }
}
