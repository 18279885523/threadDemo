package com.example.threaddemo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface InformationService {

    Map<String, Object> importData(MultipartFile file) throws ExecutionException, InterruptedException, IOException;

}
