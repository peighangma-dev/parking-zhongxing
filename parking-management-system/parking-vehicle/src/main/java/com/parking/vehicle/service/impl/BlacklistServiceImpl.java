package com.parking.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.vehicle.entity.Blacklist;
import com.parking.vehicle.mapper.BlacklistMapper;
import com.parking.vehicle.service.BlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistMapper blacklistMapper;

    @Override
    public IPage<Blacklist> page(Integer current, Integer size, Long vehicleId) {
        Page<Blacklist> page = new Page<>(current, size);
        LambdaQueryWrapper<Blacklist> wrapper = new LambdaQueryWrapper<>();
        if (vehicleId != null) {
            wrapper.eq(Blacklist::getVehicleId, vehicleId);
        }
        wrapper.orderByDesc(Blacklist::getCreatedAt);
        return blacklistMapper.selectPage(page, wrapper);
    }

    @Override
    public java.util.List<Blacklist> list(Long vehicleId) {
        LambdaQueryWrapper<Blacklist> wrapper = new LambdaQueryWrapper<>();
        if (vehicleId != null) {
            wrapper.eq(Blacklist::getVehicleId, vehicleId);
        }
        wrapper.orderByDesc(Blacklist::getCreatedAt);
        return blacklistMapper.selectList(wrapper);
    }

    @Override
    public Blacklist getById(Long id) {
        return blacklistMapper.selectById(id);
    }

    @Override
    public Blacklist add(Long vehicleId, String reason, Long operatorId) {
        Blacklist blacklist = new Blacklist();
        blacklist.setVehicleId(vehicleId);
        blacklist.setReason(reason);
        blacklist.setCreatedAt(LocalDateTime.now());
        blacklist.setCreatedBy(operatorId);
        blacklistMapper.insert(blacklist);
        return blacklist;
    }

    @Override
    public boolean remove(Long id) {
        return blacklistMapper.deleteById(id) > 0;
    }

    @Override
    public boolean isBlacklisted(Long vehicleId) {
        LambdaQueryWrapper<Blacklist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blacklist::getVehicleId, vehicleId);
        return blacklistMapper.selectCount(wrapper) > 0;
    }
}
