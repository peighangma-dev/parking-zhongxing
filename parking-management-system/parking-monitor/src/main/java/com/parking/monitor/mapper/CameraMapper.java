package com.parking.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parking.monitor.entity.Camera;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CameraMapper extends BaseMapper<Camera> {
}
