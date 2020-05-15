package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

public interface NotificationService {
    public Response toLiteFeed(Integer userId, Integer page);

    public Response toLiteMessage(Integer userId, Integer page);

    public Response toLiteLike(Integer userId, Integer page);
}
