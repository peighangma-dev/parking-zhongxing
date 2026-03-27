package com.parking.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.uc.entity.ParkingLot;
import com.parking.uc.mapper.ParkingLotMapper;
import com.parking.uc.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotMapper parkingLotMapper;

    @Override
    public ParkingLot getById(Long id) {
        ParkingLot lot = parkingLotMapper.selectById(id);
        if (lot == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return lot;
    }

    @Override
    public IPage<ParkingLot> page(Integer current, Integer size, String lotName) {
        Page<ParkingLot> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingLot> wrapper = new LambdaQueryWrapper<>();
        if (lotName != null && !lotName.isEmpty()) {
            wrapper.like(ParkingLot::getLotName, lotName);
        }
        wrapper.orderByDesc(ParkingLot::getId);
        return parkingLotMapper.selectPage(page, wrapper);
    }

    @Override
    public ParkingLot create(ParkingLot lot) {
        LambdaQueryWrapper<ParkingLot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParkingLot::getLotCode, lot.getLotCode());
        if (parkingLotMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "停车场编码已存在");
        }
        lot.setStatus("active");
        lot.setOccupiedSpaces(0);
        parkingLotMapper.insert(lot);
        return lot;
    }

    @Override
    public boolean update(Long id, ParkingLot lot) {
        ParkingLot existing = getById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        lot.setId(id);
        return parkingLotMapper.updateById(lot) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return parkingLotMapper.deleteById(id) > 0;
    }
}
