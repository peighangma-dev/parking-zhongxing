package com.parking.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.vehicle.entity.Blacklist;
import com.parking.vehicle.entity.Vehicle;
import com.parking.vehicle.mapper.BlacklistMapper;
import com.parking.vehicle.mapper.VehicleMapper;
import com.parking.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;
    private final BlacklistMapper blacklistMapper;

    @Override
    public IPage<Vehicle> page(Integer current, Integer size, String plateNumber, String vehicleTypeCat, String status) {
        Page<Vehicle> page = new Page<>(current, size);
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(plateNumber)) {
            wrapper.like(Vehicle::getPlateNumber, plateNumber);
        }
        if (StringUtils.hasText(vehicleTypeCat)) {
            wrapper.eq(Vehicle::getVehicleTypeCat, vehicleTypeCat);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Vehicle::getStatus, status);
        }
        wrapper.orderByDesc(Vehicle::getCreatedAt);
        return vehicleMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Vehicle> list(String plateNumber, String vehicleTypeCat) {
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(plateNumber)) {
            wrapper.like(Vehicle::getPlateNumber, plateNumber);
        }
        if (StringUtils.hasText(vehicleTypeCat)) {
            wrapper.eq(Vehicle::getVehicleTypeCat, vehicleTypeCat);
        }
        wrapper.orderByDesc(Vehicle::getCreatedAt);
        return vehicleMapper.selectList(wrapper);
    }

    @Override
    public Vehicle getById(Long id) {
        Vehicle vehicle = vehicleMapper.selectById(id);
        if (vehicle == null) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        return vehicle;
    }

    @Override
    public Vehicle getByPlateNumber(String plateNumber) {
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Vehicle::getPlateNumber, plateNumber);
        return vehicleMapper.selectOne(wrapper);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Vehicle::getPlateNumber, vehicle.getPlateNumber());
        if (vehicleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "车牌号已存在");
        }
        vehicleMapper.insert(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        Vehicle existing = vehicleMapper.selectById(vehicle.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        
        if (StringUtils.hasText(vehicle.getPlateNumber()) 
                && !vehicle.getPlateNumber().equals(existing.getPlateNumber())) {
            LambdaQueryWrapper<Vehicle> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Vehicle::getPlateNumber, vehicle.getPlateNumber());
            wrapper.ne(Vehicle::getId, vehicle.getId());
            if (vehicleMapper.selectCount(wrapper) > 0) {
                throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "车牌号已被其他车辆使用");
            }
        }
        
        vehicleMapper.updateById(vehicle);
        return vehicle;
    }

    @Override
    public boolean delete(Long id) {
        return vehicleMapper.deleteById(id) > 0;
    }

    @Override
    public boolean enable(Long id) {
        Vehicle vehicle = getById(id);
        vehicle.setStatus("normal");
        return vehicleMapper.updateById(vehicle) > 0;
    }

    @Override
    public boolean disable(Long id) {
        Vehicle vehicle = getById(id);
        vehicle.setStatus("disabled");
        return vehicleMapper.updateById(vehicle) > 0;
    }

    @Override
    public boolean isBlacklisted(Long vehicleId) {
        LambdaQueryWrapper<Blacklist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blacklist::getVehicleId, vehicleId);
        return blacklistMapper.selectCount(wrapper) > 0;
    }

    @Override
    public void validateVehicleAccess(Long vehicleId) {
        Vehicle vehicle = getById(vehicleId);
        if ("disabled".equals(vehicle.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN.getCode(), "车辆已被禁用");
        }
        if (isBlacklisted(vehicleId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN.getCode(), "车辆在黑名单中，禁止入场");
        }
    }
}
