package com.parking.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.vehicle.entity.Owner;
import com.parking.vehicle.mapper.OwnerMapper;
import com.parking.vehicle.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerMapper ownerMapper;

    @Override
    public IPage<Owner> page(Integer current, Integer size, String name, String phone) {
        Page<Owner> page = new Page<>(current, size);
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Owner::getName, name);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.eq(Owner::getPhone, phone);
        }
        wrapper.orderByDesc(Owner::getCreatedAt);
        return ownerMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Owner> list(String name, String phone) {
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Owner::getName, name);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.eq(Owner::getPhone, phone);
        }
        wrapper.orderByDesc(Owner::getCreatedAt);
        return ownerMapper.selectList(wrapper);
    }

    @Override
    public Owner getById(Long id) {
        Owner owner = ownerMapper.selectById(id);
        if (owner == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return owner;
    }

    @Override
    public Owner save(Owner owner) {
        ownerMapper.insert(owner);
        return owner;
    }

    @Override
    public Owner update(Owner owner) {
        if (owner.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        Owner existing = ownerMapper.selectById(owner.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        ownerMapper.updateById(owner);
        return owner;
    }

    @Override
    public boolean delete(Long id) {
        return ownerMapper.deleteById(id) > 0;
    }
}
