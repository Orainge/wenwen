package com.orainge.wenwen.service.impl;

import com.github.pagehelper.PageHelper;
import com.orainge.wenwen.mybatis.entity.Question;
import com.orainge.wenwen.mybatis.mapper.QuestionMapper;
import com.orainge.wenwen.service.IndexService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private QuestionMapper questionMapper;
    private final Integer pageSize = 10;

    @Override
    public Response apiTopView(Integer nextPage) {
        Response response = new Response();
        PageHelper.startPage(nextPage, pageSize);
        List<Question> list = questionMapper.getTopView();
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
    public Response apiTopAnswer(Integer nextPage) {
        Response response = new Response();
        PageHelper.startPage(nextPage, pageSize);
        List<Question> list = questionMapper.getTopAnswer();
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
