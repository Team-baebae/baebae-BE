package com.web.baebaeBE.global.image.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class S3ImageStorageService implements ImageStorageService {
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(String memberId, String answerId, String fileType, int index, InputStream inputStream, long size, String contentType) {
        String key = generateFilePath(memberId, answerId, fileType, index);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(size);
        metadata.setContentType(contentType);
        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(request);
        return getFileUrl(memberId, answerId, fileType, index);
    }

    @Override
    public void deleteFile(String memberId, String answerId, String fileType, int index) {
        String key = generateFilePath(memberId, answerId, fileType, index);
        amazonS3Client.deleteObject(bucketName, key);
    }

    @Override
    public String getFileUrl(String memberId, String answerId, String fileType, int index) {
        String key = generateFilePath(memberId, answerId, fileType, index);
        return amazonS3Client.getUrl(bucketName, key).toExternalForm();
    }

    public String generateFilePath(String memberId, String answerId, String fileType, int index) {
        switch (fileType) {
            case "profile":
                return memberId + "/profile.jpg";
            case "image":
                return memberId + "/" + answerId + "/image_" + index + ".jpg";
            case "music":
                return memberId + "/" + answerId + "/music.jpg";
            case "audio":
                return memberId + "/" + answerId + "/audio.mp3";
            default:
                throw new IllegalArgumentException("Unknown file type");
        }
    }

    public void deleteFileByUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String key = url.getPath().substring(1); // URL에서 키 추출
            amazonS3Client.deleteObject(bucketName, key);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file URL", e);
        }
    }
}