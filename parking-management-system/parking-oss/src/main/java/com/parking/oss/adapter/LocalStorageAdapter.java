package com.parking.oss.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LocalStorageAdapter implements StorageAdapter {

    private static final String BASE_PATH = "/var/parking/oss/";

    @Override
    public String getProvider() {
        return "local";
    }

    @Override
    public String upload(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : "";
        String newFileName = UUID.randomUUID().toString().replace("-", "") + extension;
        String fullPath = BASE_PATH + path + "/" + newFileName;
        
        File dest = new File(fullPath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        
        try {
            file.transferTo(dest);
            log.info("文件上传成功: {}", fullPath);
            return fullPath;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();
            log.info("文件删除: {}, result: {}", path, deleted);
            return deleted;
        }
        return false;
    }

    @Override
    public String getAccessUrl(String path) {
        return "/api/oss/files/" + path.substring(path.lastIndexOf("/") + 1);
    }
}
