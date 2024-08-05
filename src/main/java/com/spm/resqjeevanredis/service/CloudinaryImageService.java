package com.spm.resqjeevanredis.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface CloudinaryImageService {
    Map upload(MultipartFile multipartFile);
    public InputStream getImageFile(String imageId) throws IOException;
}
