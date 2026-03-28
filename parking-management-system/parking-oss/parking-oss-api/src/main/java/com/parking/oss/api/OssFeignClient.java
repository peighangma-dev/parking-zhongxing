package com.parking.oss.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "parking-oss", url = "${feign.oss.url:http://localhost:8087}")
public interface OssFeignClient {

    @PostMapping("/api/oss/v1/upload")
    Map<String, Object> upload(@RequestPart("file") MultipartFile file);
    
    @PostMapping("/api/oss/v1/upload/simple")
    Map<String, String> uploadSimple(@RequestPart("file") MultipartFile file);
}
