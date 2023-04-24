package com.example.zscacm.controller;

import com.example.zscacm.utils.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

import static com.example.zscacm.utils.PathUtils.generateFilePath;
import static com.example.zscacm.utils.CosUtils.uploadFile;

@RestController
public class FileController {

    @PostMapping("uploadFile")
    public ResponseResult uploadFiles(MultipartFile file, String name) throws IOException {
        String key = "pic/" + generateFilePath(name);

        URL url = uploadFile(file, "zscacm-1302342664", key);
        return new ResponseResult<>(1, "1", null);
    }
}
