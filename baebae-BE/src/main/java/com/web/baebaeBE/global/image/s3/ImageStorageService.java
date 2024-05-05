package com.web.baebaeBE.global.image.s3;

import java.io.InputStream;

public interface ImageStorageService {
    String uploadFile(String path, String fileName, InputStream inputStream, long size, String contentType);
    void deleteFile(String path, String fileName);
    String getFileUrl(String path, String fileName);
}
