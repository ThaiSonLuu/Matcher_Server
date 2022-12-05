package com.sanryoo.toifa.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    String uploadFile(Long id, String username, String column, MultipartFile file);
    byte[] readFileContent(String username, String fileName);
}
