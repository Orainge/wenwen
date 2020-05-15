package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

import java.util.Map;

public interface PeopleService {
    public Map<String, Object> toPeople(String peopleId);

    public Response apiQuestion(Integer userId, Integer peopleId, Integer nextPage);

    public Response apiAnswer(Integer userId, Integer peopleId, Integer nextPage);

    public Response apiFollowQuestion(Integer peopleId, Integer nextPage);

    public Response apiFollowUser(Integer peopleId, Integer nextPage);
}
