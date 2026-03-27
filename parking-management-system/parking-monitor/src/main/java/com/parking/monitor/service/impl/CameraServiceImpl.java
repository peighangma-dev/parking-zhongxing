package com.parking.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.monitor.entity.Camera;
import com.parking.monitor.mapper.CameraMapper;
import com.parking.monitor.service.CameraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CameraServiceImpl implements CameraService {

    private final CameraMapper cameraMapper;

    @Override
    public IPage<Camera> page(Integer current, Integer size, String cameraCode, String cameraName, String status) {
        Page<Camera> page = new Page<>(current, size);
        LambdaQueryWrapper<Camera> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(cameraCode)) {
            wrapper.like(Camera::getCameraCode, cameraCode);
        }
        if (StringUtils.hasText(cameraName)) {
            wrapper.like(Camera::getCameraName, cameraName);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Camera::getStatus, status);
        }
        wrapper.orderByDesc(Camera::getCreatedAt);
        return cameraMapper.selectPage(page, wrapper);
    }

    @Override
    public Camera getById(Long id) {
        Camera camera = cameraMapper.selectById(id);
        if (camera == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return camera;
    }

    @Override
    public Camera save(Camera camera) {
        cameraMapper.insert(camera);
        return camera;
    }

    @Override
    public Camera update(Camera camera) {
        if (camera.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        cameraMapper.updateById(camera);
        return camera;
    }

    @Override
    public boolean delete(Long id) {
        return cameraMapper.deleteById(id) > 0;
    }

    @Override
    public String getStreamUrl(Long cameraId) {
        Camera camera = getById(cameraId);
        if (StringUtils.hasText(camera.getStreamUrl())) {
            return camera.getStreamUrl();
        }
        return String.format("rtsp://%s:%d/ch%d", 
            camera.getIpAddress(), camera.getPort(), camera.getChannel());
    }

    @Override
    public String captureImage(Long cameraId) {
        Camera camera = getById(cameraId);
        log.info("模拟抓图, cameraId={}, cameraName={}", cameraId, camera.getCameraName());
        return "/images/capture/" + cameraId + "_" + System.currentTimeMillis() + ".jpg";
    }
}
