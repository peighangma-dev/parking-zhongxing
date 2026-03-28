package com.parking.uc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.uc.entity.SysMenu;
import java.util.List;

public interface SysMenuService {
    IPage<SysMenu> page(Integer current, Integer size);
    List<SysMenu> getTree();
    SysMenu getById(Long id);
    List<Long> getMenuIdsByRoleId(Long roleId);
    SysMenu create(SysMenu menu);
    SysMenu update(Long id, SysMenu menu);
    Boolean delete(Long id);
    Boolean assignMenus(Long roleId, List<Long> menuIds);
}
