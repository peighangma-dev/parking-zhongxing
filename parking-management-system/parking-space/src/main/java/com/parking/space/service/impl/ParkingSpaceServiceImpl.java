package com.parking.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.space.entity.ParkingSpace;
import com.parking.space.mapper.ParkingSpaceMapper;
import com.parking.space.service.ParkingSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceMapper spaceMapper;

    @Override
    public IPage<ParkingSpace> page(Integer current, Integer size, Long areaId, String spaceType, String status) {
        Page<ParkingSpace> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingSpace> wrapper = new LambdaQueryWrapper<>();
        if (areaId != null) {
            wrapper.eq(ParkingSpace::getAreaId, areaId);
        }
        if (StringUtils.hasText(spaceType)) {
            wrapper.eq(ParkingSpace::getSpaceType, spaceType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(ParkingSpace::getStatus, status);
        }
        wrapper.orderByDesc(ParkingSpace::getCreatedAt);
        return spaceMapper.selectPage(page, wrapper);
    }

    @Override
    public List<ParkingSpace> list(Long areaId, String spaceType, String status) {
        LambdaQueryWrapper<ParkingSpace> wrapper = new LambdaQueryWrapper<>();
        if (areaId != null) {
            wrapper.eq(ParkingSpace::getAreaId, areaId);
        }
        if (StringUtils.hasText(spaceType)) {
            wrapper.eq(ParkingSpace::getSpaceType, spaceType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(ParkingSpace::getStatus, status);
        }
        wrapper.orderByDesc(ParkingSpace::getCreatedAt);
        return spaceMapper.selectList(wrapper);
    }

    @Override
    public ParkingSpace getById(Long id) {
        ParkingSpace space = spaceMapper.selectById(id);
        if (space == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return space;
    }

    @Override
    public ParkingSpace save(ParkingSpace space) {
        spaceMapper.insert(space);
        return space;
    }

    @Override
    public ParkingSpace update(ParkingSpace space) {
        if (space.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        spaceMapper.updateById(space);
        return space;
    }

    @Override
    public boolean delete(Long id) {
        return spaceMapper.deleteById(id) > 0;
    }

    @Override
    public boolean occupy(Long id, String plateNumber) {
        ParkingSpace space = getById(id);
        if (!"empty".equals(space.getStatus())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "车位不是空闲状态");
        }
        space.setStatus("occupied");
        space.setVehiclePlate(plateNumber);
        return spaceMapper.updateById(space) > 0;
    }

    @Override
    public boolean release(Long id) {
        ParkingSpace space = getById(id);
        if (!"occupied".equals(space.getStatus())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "车位不是占用状态");
        }
        space.setStatus("empty");
        space.setVehiclePlate(null);
        return spaceMapper.updateById(space) > 0;
    }
}
