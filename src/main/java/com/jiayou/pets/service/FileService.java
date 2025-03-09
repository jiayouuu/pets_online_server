package com.jiayou.pets.service;

import com.jiayou.pets.dto.file.FileUploadReq;
import com.jiayou.pets.dto.response.ResEntity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public interface FileService {
    ResEntity<HashMap<String,Object>> upload(FileUploadReq req);

    File getFileByName(String name) throws IOException;
}
