package com.orainge.wenwen.util;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.orainge.wenwen.exception.DefaultException;
import com.orainge.wenwen.exception.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class ImageUtil {
    private final String imgUrlPrefix = "/img";

    @SuppressWarnings("all")
    public String upload(MultipartFile file, String type) {
        String fileName = file.getOriginalFilename(); //得到上传的文件名
        String uuid = UUID.randomUUID().toString().replace("-", "");
        fileName = uuid + fileName.substring(fileName.lastIndexOf(".")); // 新文件名
        String filePath = WebsiteSettings.IMAGE_DISK_PATH + "/" + type + "/" + fileName;
        String result = null;

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        byte[] bytes = null;

        try {
            bytes = file.getBytes();
            File temp = new File(filePath);
            if (!temp.exists()) {
                if (temp.createNewFile()) {
                    fos = new FileOutputStream(temp);
                    bos = new BufferedOutputStream(fos);
                    bos.write(bytes);
                }
            }
            result = imgUrlPrefix + "/" + type + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    @SuppressWarnings("all")
    public String upload(String base64Data, String type) {
        String result = null;
        if (StringUtils.isEmpty(base64Data) || StringUtils.isEmpty(type)) // 图像数据为空
            return result;
        String[] d = base64Data.split("base64,");//将字符串分成数组
        if (d == null || d.length != 2)
            return result;
        String data = d[1]; //实体部分数据
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + getBase64Format(d[0]);
        String filePath = WebsiteSettings.IMAGE_DISK_PATH + "/" + type + "/" + fileName;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(data);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(filePath);
            out.write(bytes);
            out.flush();
            out.close();
            result = imgUrlPrefix + "/" + type + "/" + fileName;
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    public boolean delete(String urlPath) {
        int endIndex = urlPath.length();
        File f = new File(WebsiteSettings.IMAGE_DISK_PATH + urlPath.substring(urlPath.indexOf("/", 2), endIndex));
        return f.delete();
    }

    private String getBase64Format(String Base64DataPrefix) {
        //data:image/jpeg;base64,base64编码的jpeg图片数据
        if ("data:image/jpeg;".equalsIgnoreCase(Base64DataPrefix)) {
            return ".jpg";
        } else if ("data:image/x-icon;".equalsIgnoreCase(Base64DataPrefix)) {
            //data:image/x-icon;base64,base64编码的icon图片数据
            return ".ico";
        } else if ("data:image/gif;".equalsIgnoreCase(Base64DataPrefix)) {
            //data:image/gif;base64,base64编码的gif图片数据
            return ".gif";
        } else if ("data:image/png;".equalsIgnoreCase(Base64DataPrefix)) {
            //data:image/png;base64,base64编码的png图片数据
            return ".png";
        } else {
            return "";
        }
    }
}
