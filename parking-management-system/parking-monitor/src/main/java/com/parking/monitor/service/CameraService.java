package com.parking.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.monitor.entity.Alarm;
import com.parking.monitor.entity.Camera;
import java.time.LocalDateTime;

public interface CameraService {

    IPage<Camera> page(Integer current, Integer size, String cameraCode, String cameraName, String status);

    Camera getById(Long id);

    Camera save(Camera camera);

    Camera update(Camera camera);

    boolean delete(Long id);

    String getStreamUrl(Long cameraId);

    String captureImage(Long cameraId);
}
