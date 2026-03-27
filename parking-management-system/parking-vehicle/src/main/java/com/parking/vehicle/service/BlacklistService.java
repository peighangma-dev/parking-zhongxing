package com.parking.vehicle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.vehicle.entity.Blacklist;
import java.util.List;

public interface BlacklistService {

    IPage<Blacklist> page(Integer current, Integer size, Long vehicleId);

    List<Blacklist> list(Long vehicleId);

    Blacklist getById(Long id);

    Blacklist add(Long vehicleId, String reason, Long operatorId);

    boolean remove(Long id);

    boolean isBlacklisted(Long vehicleId);
}
