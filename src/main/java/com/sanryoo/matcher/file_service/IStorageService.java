package com.sanryoo.matcher.file_service;

import com.sanryoo.matcher.modal.MatcherMessage;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

    void uploadImageProfile(Long id, String username, String column, MultipartFile file);

    MatcherMessage uploadImageMessage(MatcherMessage message, MultipartFile file);

    byte[] readFileContent(String path);

}
