package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

public interface FollowService {
    public Response apiCheckFollowPeople(Integer userId, Integer peopleId);

    public Response apiFollowPeople(Integer userId, String userNickname, Integer peopleId, String peopleNickname);

    public Response apiUnFollowPeople(Integer userId, Integer peopleId);

    public Response apiCheckFollowQuestion(Integer userId, Integer questionId);

    public Response apiFollowQuestion(Integer userId, Integer questionId);

    public Response apiUnFollowQuestion(Integer userId, Integer questionId);
}
