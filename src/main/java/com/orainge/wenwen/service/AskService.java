package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

import java.util.List;
import java.util.Map;

public interface AskService {
    public Map<String, String> toAsk(String userId);

    public Response apiAsk(Integer userId, String title, String content, Integer anonymous, List<String> topicIdList);

    public Response apiSaveDraft(Integer userId, String title, String content, Integer anonymous, String topicIdList);
}
