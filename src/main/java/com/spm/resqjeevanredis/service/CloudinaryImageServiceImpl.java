package com.spm.resqjeevanredis.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Url;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
    private final Cloudinary cloudinary;

    public CloudinaryImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map upload(MultipartFile multipartFile) {
        try {
            Map data = this.cloudinary.uploader().upload(multipartFile.getBytes(),Map.of());
            return data;
        }
        catch (IOException exception){
            throw new RuntimeException("Error uploading image" + exception.getMessage());
        }
    }

    @Override
    public InputStream getImageFile(String public_id) throws IOException {
        String url = cloudinary.url().generate(public_id);
        return downloadImage(url);
    }

    private InputStream downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        return connection.getInputStream();
    }
}
