package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    public Response upload(List<MultipartFile> files, String type);

//    public Response delete(String urlPath);
}
