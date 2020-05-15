package com.orainge.wenwen.service.impl;

import com.github.pagehelper.PageHelper;
import com.orainge.wenwen.mybatis.entity.Question;
import com.orainge.wenwen.mybatis.entity.User;
import com.orainge.wenwen.mybatis.mapper.QuestionMapper;
import com.orainge.wenwen.mybatis.mapper.UserMapper;
import com.orainge.wenwen.service.SearchService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class SearchServiceImpl implements SearchService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    private final Integer pageSize = 10;

    @Override
    public Response apiSearchQuestion(String keyword, Integer nextPage) {
        Response response = new Response();
        PageHelper.startPage(nextPage, pageSize);
        List<Question> list = questionMapper.searchQuestion(keyword);
        Map<String, Object> result = new HashMap<String, Object>();
        if (list == null || list.size() == 0) {
            result.put("end", 1);
        } else {
            result.put("data", list);
            if (list.size() < pageSize) {
                result.put("end", 1);
            }
        }
        response.setData(result);
        return response;
    }

    @Override
    public Response apiSearchUser(String keyword, Integer nextPage) {
        Response response = new Response();
        PageHelper.startPage(nextPage, pageSize);
        List<User> list = userMapper.searchUser(keyword);
        Map<String, Object> result = new HashMap<String, Object>();
        if (list == null || list.size() == 0) {
            result.put("end", 1);
        } else {
            result.put("data", list);
            if (list.size() < pageSize) {
                result.put("end", 1);
            }
        }
        response.setData(result);
        return response;
    }
}
