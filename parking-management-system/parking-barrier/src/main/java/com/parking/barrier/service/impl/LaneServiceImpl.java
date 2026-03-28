package com.parking.barrier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.barrier.entity.Lane;
import com.parking.barrier.mapper.LaneMapper;
import com.parking.barrier.service.LaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LaneServiceImpl implements LaneService {

    private final LaneMapper laneMapper;

    @Override
    public IPage<Lane> page(Integer current, Integer size, Long barrierId) {
        Page<Lane> page = new Page<>(current, size);
        LambdaQueryWrapper<Lane> wrapper = new LambdaQueryWrapper<>();
        if (barrierId != null) {
            wrapper.eq(Lane::getBarrierId, barrierId);
        }
        wrapper.orderByDesc(Lane::getId);
        return laneMapper.selectPage(page, wrapper);
    }

    @Override
    public Lane getById(Long id) {
        return laneMapper.selectById(id);
    }

    @Override
    public Lane save(Lane lane) {
        laneMapper.insert(lane);
        return lane;
    }

    @Override
    public Lane update(Long id, Lane lane) {
        lane.setId(id);
        laneMapper.updateById(lane);
        return lane;
    }

    @Override
    public Boolean delete(Long id) {
        return laneMapper.deleteById(id) > 0;
    }
}
