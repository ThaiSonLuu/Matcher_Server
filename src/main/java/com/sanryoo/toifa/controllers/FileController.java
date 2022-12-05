package com.sanryoo.toifa.controllers;

import com.sanryoo.toifa.modal.ResponseObject;
import com.sanryoo.toifa.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private IStorageService storageService;

    @PostMapping("")
    ResponseEntity<ResponseObject> uploadFile(@RequestParam("id") Long id, @RequestParam("username") String username, @RequestParam("column") String column, @RequestParam("file") MultipartFile file) {
        try {
            String generatedFileName = storageService.uploadFile(id, username, column, file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Upload file successful", generatedFileName)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", e.getMessage(), "")
            );
        }
    }

    // files/avatar.jpg
    @GetMapping("/{username}/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable("username") String username, @PathVariable("fileName") String fileName) {
        try {
            byte[] bytes = storageService.readFileContent(username, fileName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }

//    @GetMapping("")
//    public ResponseEntity<ResponseObject> getUploadedFiles() {
//        try {
//            List<String> urls = storageService.loadAll()
//                    .map(path -> {
//                        //convert fileName to url(send request "readDetailFile")
//                        return MvcUriComponentsBuilder.fromMethodName(FileController.class,
//                                "readDetailFile", path.getFileName().toString()).build().toUri().toString();
//                    })
//                    .collect(Collectors.toList());
//            return ResponseEntity.ok(new ResponseObject("ok", "List files successfully", urls));
//        }catch (Exception exception) {
//            return ResponseEntity.ok(new
//                    ResponseObject("failed", "List files failed", new String[] {}));
//        }
//    }
}
