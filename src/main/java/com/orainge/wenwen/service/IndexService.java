package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

public interface IndexService {
    public Response apiTopView(Integer nextPage);

    public Response apiTopAnswer(Integer nextPage);
}
