package com.parking.oss.adapter;

import org.springframework.web.multipart.MultipartFile;

public interface StorageAdapter {

    String getProvider();

    String upload(MultipartFile file, String path);

    boolean delete(String path);

    String getAccessUrl(String path);
}
