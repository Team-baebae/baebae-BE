package com.web.baebaeBE.infra.image.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
@Service
public class S3ImageStorageService implements ImageStorageService {
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(String path, String fileName, InputStream inputStream, long size, String contentType) {
        String key = path + "/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(size);
        metadata.setContentType(contentType);
        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead); // 파일을 공개 읽기 가능하게 설정
        amazonS3Client.putObject(request);
        return getFileUrl(path, fileName);
    }

    @Override
    public void deleteFile(String path, String fileName) {
        String key = path + "/" + fileName;
        amazonS3Client.deleteObject(bucketName, key);
    }

    @Override
    public String getFileUrl(String path, String fileName) {
        String key = path + "/" + fileName;
        return amazonS3Client.getUrl(bucketName, key).toExternalForm();
    }
}
