package com.parking.oss.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.oss.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    IPage<FileInfo> page(Integer current, Integer size, String fileName, String storageType);

    FileInfo getById(Long id);

    FileInfo upload(MultipartFile file, String path);

    boolean delete(Long id);
}
