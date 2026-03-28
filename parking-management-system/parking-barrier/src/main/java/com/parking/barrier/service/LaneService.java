package com.parking.barrier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.barrier.entity.Lane;

public interface LaneService {
    IPage<Lane> page(Integer current, Integer size, Long barrierId);
    Lane getById(Long id);
    Lane save(Lane lane);
    Lane update(Long id, Lane lane);
    Boolean delete(Long id);
}
