package com.parking.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.uc.entity.SysRole;
import com.parking.uc.mapper.SysRoleMapper;
import com.parking.uc.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper roleMapper;

    @Override
    public IPage<SysRole> page(Integer current, Integer size, String roleName) {
        Page<SysRole> page = new Page<>(current, size);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (roleName != null && !roleName.isEmpty()) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        wrapper.orderByDesc(SysRole::getId);
        return roleMapper.selectPage(page, wrapper);
    }

    @Override
    public SysRole getById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public List<SysRole> getAll() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getStatus, "normal");
        return roleMapper.selectList(wrapper);
    }

    @Override
    public SysRole create(SysRole role) {
        roleMapper.insert(role);
        return role;
    }

    @Override
    public SysRole update(Long id, SysRole role) {
        role.setId(id);
        roleMapper.updateById(role);
        return role;
    }

    @Override
    public Boolean delete(Long id) {
        return roleMapper.deleteById(id) > 0;
    }
}
