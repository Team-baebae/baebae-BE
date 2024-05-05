package com.web.baebaeBE.global.image.s3;

import java.io.InputStream;

public interface ImageStorageService {
    String uploadFile(String memberId, String answerId, String fileType, int index, InputStream inputStream, long size, String contentType);
    void deleteFile(String memberId, String answerId, String fileType, int index);
    String getFileUrl(String memberId, String answerId, String fileType, int index);
    String generateFilePath(String memberId, String answerId, String fileType, int index);
}
