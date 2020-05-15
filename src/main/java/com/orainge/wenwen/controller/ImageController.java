package com.orainge.wenwen.controller;

import com.alibaba.druid.util.StringUtils;
import com.orainge.wenwen.exception.NullRequestParametersException;
import com.orainge.wenwen.service.ImageService;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/imgUpload")
    @ResponseBody
    public Response imgUpload(@RequestParam(required = false, name = "files") List<MultipartFile> files, @RequestParam(required = false, name = "type") String type) throws NullRequestParametersException {
        if (files == null || files.size() == 0 || StringUtils.isEmpty(type)) {
            throw new NullRequestParametersException();
        }
        return imageService.upload(files, type);
    }

//    @DeleteMapping(value = "/imgDelete")
////    @GetMapping(value = "/imgDelete")
//    @ResponseBody
//    public Response imgDelete(@RequestParam(required = false, name = "urlPath") String urlPath) throws NullRequestParametersException {
//        if (StringUtils.isEmpty(urlPath)) {
//            throw new NullRequestParametersException();
//        }
//        return imageService.delete(urlPath);
//    }
}
