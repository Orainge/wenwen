package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

public interface SearchService {
    public Response apiSearchQuestion(String keyword, Integer nextPage);

    public Response apiSearchUser(String keyword, Integer nextPage);
}
