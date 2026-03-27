package com.parking.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.space.entity.ParkingArea;
import com.parking.space.mapper.ParkingAreaMapper;
import com.parking.space.service.ParkingAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParkingAreaServiceImpl implements ParkingAreaService {

    private final ParkingAreaMapper areaMapper;

    @Override
    public IPage<ParkingArea> page(Integer current, Integer size, String areaName, String floor) {
        Page<ParkingArea> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingArea> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(areaName)) {
            wrapper.like(ParkingArea::getAreaName, areaName);
        }
        if (StringUtils.hasText(floor)) {
            wrapper.eq(ParkingArea::getFloor, floor);
        }
        wrapper.orderByDesc(ParkingArea::getCreatedAt);
        return areaMapper.selectPage(page, wrapper);
    }

    @Override
    public List<ParkingArea> list(String areaName, String floor) {
        LambdaQueryWrapper<ParkingArea> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(areaName)) {
            wrapper.like(ParkingArea::getAreaName, areaName);
        }
        if (StringUtils.hasText(floor)) {
            wrapper.eq(ParkingArea::getFloor, floor);
        }
        wrapper.orderByDesc(ParkingArea::getCreatedAt);
        return areaMapper.selectList(wrapper);
    }

    @Override
    public ParkingArea getById(Long id) {
        ParkingArea area = areaMapper.selectById(id);
        if (area == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return area;
    }

    @Override
    public ParkingArea save(ParkingArea area) {
        areaMapper.insert(area);
        return area;
    }

    @Override
    public ParkingArea update(ParkingArea area) {
        if (area.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        areaMapper.updateById(area);
        return area;
    }

    @Override
    public boolean delete(Long id) {
        return areaMapper.deleteById(id) > 0;
    }

    @Override
    public Map<String, Object> getStatistics() {
        List<ParkingArea> areas = areaMapper.selectList(null);
        int totalSpaces = areas.stream().mapToInt(ParkingArea::getTotalSpaces).sum();
        int occupiedSpaces = areas.stream().mapToInt(ParkingArea::getOccupiedSpaces).sum();
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalSpaces", totalSpaces);
        result.put("occupiedSpaces", occupiedSpaces);
        result.put("availableSpaces", totalSpaces - occupiedSpaces);
        result.put("occupancyRate", totalSpaces > 0 ? (double) occupiedSpaces / totalSpaces * 100 : 0);
        result.put("areaCount", areas.size());
        
        return result;
    }
}
