package com.parking.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.oss.adapter.StorageAdapter;
import com.parking.oss.adapter.LocalStorageAdapter;
import com.parking.oss.entity.FileInfo;
import com.parking.oss.mapper.FileInfoMapper;
import com.parking.oss.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileInfoMapper fileInfoMapper;
    private final Map<String, StorageAdapter> adapters = new ConcurrentHashMap<>();

    public FileServiceImpl(FileInfoMapper fileInfoMapper) {
        this.fileInfoMapper = fileInfoMapper;
        adapters.put("local", new LocalStorageAdapter());
    }

    @Override
    public IPage<FileInfo> page(Integer current, Integer size, String fileName, String storageType) {
        Page<FileInfo> page = new Page<>(current, size);
        LambdaQueryWrapper<FileInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(fileName)) {
            wrapper.like(FileInfo::getFileName, fileName);
        }
        if (StringUtils.hasText(storageType)) {
            wrapper.eq(FileInfo::getStorageType, storageType);
        }
        wrapper.orderByDesc(FileInfo::getCreatedAt);
        return fileInfoMapper.selectPage(page, wrapper);
    }

    @Override
    public FileInfo getById(Long id) {
        FileInfo fileInfo = fileInfoMapper.selectById(id);
        if (fileInfo == null) {
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }
        return fileInfo;
    }

    @Override
    public FileInfo upload(MultipartFile file, String path) {
        StorageAdapter adapter = adapters.get("local");
        String filePath = adapter.upload(file, path);
        
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFilePath(filePath);
        fileInfo.setFileUrl(adapter.getAccessUrl(filePath));
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(file.getContentType());
        fileInfo.setStorageType("local");
        fileInfo.setBucketName("default");
        
        fileInfoMapper.insert(fileInfo);
        log.info("文件信息保存成功: {}", fileInfo.getId());
        return fileInfo;
    }

    @Override
    public boolean delete(Long id) {
        FileInfo fileInfo = getById(id);
        StorageAdapter adapter = adapters.get(fileInfo.getStorageType());
        if (adapter != null) {
            adapter.delete(fileInfo.getFilePath());
        }
        return fileInfoMapper.deleteById(id) > 0;
    }
}
