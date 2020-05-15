package com.orainge.wenwen.service.impl;

import com.orainge.wenwen.exception.FileUploadException;
import com.orainge.wenwen.service.ImageService;
import com.orainge.wenwen.util.ImageUtil;
import com.orainge.wenwen.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageUtil imageUtil;

    @Override
    @SuppressWarnings("all")
    public Response upload(List<MultipartFile> files, String type) throws FileUploadException {
        Response response = new Response();
        String imgUrls[] = new String[files.size()];
        int failFlag = -1; // 表示当前下标的元素没上传成功

        for (int i = 0; i < files.size(); i++) {
            imgUrls[i] = imageUtil.upload(files.get(i), type);
            if (imgUrls[i] == null) {
                failFlag = i - 1;
                break;
            }
        }
        if (failFlag != -1) {
            // 上传失败
            for (int i = 0; i < failFlag; i++) {
                imageUtil.delete(imgUrls[i]);
            }
            throw new FileUploadException();
        } else {
            // 上传成功
            response.setData(imgUrls);
        }
        return response;
    }

//    @Override
//    public Response delete(String urlPath) {
//        Response response = new Response();
//        if (imageUtil.delete(urlPath)) {
//            // 删除成功
//            response.setMessage("删除成功");
//        } else {
//            // 删除失败
//            response.setCode(1);
//            response.setMessage("删除失败");
//        }
//        return response;
//    }
}
