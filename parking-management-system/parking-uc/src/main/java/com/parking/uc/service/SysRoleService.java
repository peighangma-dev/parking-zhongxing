package com.parking.uc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.uc.entity.SysRole;
import java.util.List;

public interface SysRoleService {
    IPage<SysRole> page(Integer current, Integer size, String roleName);
    SysRole getById(Long id);
    List<SysRole> getAll();
    SysRole create(SysRole role);
    SysRole update(Long id, SysRole role);
    Boolean delete(Long id);
}
