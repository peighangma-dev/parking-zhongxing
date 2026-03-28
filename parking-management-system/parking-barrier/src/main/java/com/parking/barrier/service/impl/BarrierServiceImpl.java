package com.parking.barrier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.barrier.entity.Barrier;
import com.parking.barrier.mapper.BarrierMapper;
import com.parking.barrier.service.BarrierService;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarrierServiceImpl implements BarrierService {

    private final BarrierMapper barrierMapper;

    @Override
    public IPage<Barrier> page(Integer current, Integer size, Long tenantId, String code, String name, String status) {
        Page<Barrier> page = new Page<>(current, size);
        LambdaQueryWrapper<Barrier> wrapper = new LambdaQueryWrapper<>();
        if (tenantId != null && tenantId > 0) {
            wrapper.eq(Barrier::getTenantId, tenantId);
        }
        if (StringUtils.hasText(code)) {
            wrapper.like(Barrier::getCode, code);
        }
        if (StringUtils.hasText(name)) {
            wrapper.like(Barrier::getName, name);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Barrier::getStatus, status);
        }
        wrapper.orderByDesc(Barrier::getCreatedAt);
        return barrierMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Barrier> list(Long tenantId, String code, String name, String status) {
        LambdaQueryWrapper<Barrier> wrapper = new LambdaQueryWrapper<>();
        if (tenantId != null && tenantId > 0) {
            wrapper.eq(Barrier::getTenantId, tenantId);
        }
        if (StringUtils.hasText(code)) {
            wrapper.like(Barrier::getCode, code);
        }
        if (StringUtils.hasText(name)) {
            wrapper.like(Barrier::getName, name);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Barrier::getStatus, status);
        }
        wrapper.orderByDesc(Barrier::getCreatedAt);
        return barrierMapper.selectList(wrapper);
    }

    @Override
    public Barrier getById(Long id) {
        Barrier barrier = barrierMapper.selectById(id);
        if (barrier == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return barrier;
    }

    @Override
    public Barrier getByCode(String code) {
        LambdaQueryWrapper<Barrier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Barrier::getCode, code);
        return barrierMapper.selectOne(wrapper);
    }

    @Override
    public Barrier save(Barrier barrier) {
        barrierMapper.insert(barrier);
        return barrier;
    }

    @Override
    public Barrier update(Barrier barrier) {
        if (barrier.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        barrierMapper.updateById(barrier);
        return barrier;
    }

    @Override
    public boolean delete(Long id) {
        return barrierMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Barrier barrier = getById(id);
        barrier.setStatus(status);
        return barrierMapper.updateById(barrier) > 0;
    }
}
