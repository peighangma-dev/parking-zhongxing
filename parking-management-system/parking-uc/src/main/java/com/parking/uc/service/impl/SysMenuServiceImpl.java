package com.parking.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.uc.entity.SysMenu;
import com.parking.uc.entity.SysRoleMenu;
import com.parking.uc.mapper.SysMenuMapper;
import com.parking.uc.mapper.SysRoleMenuMapper;
import com.parking.uc.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper menuMapper;
    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public IPage<SysMenu> page(Integer current, Integer size) {
        Page<SysMenu> page = new Page<>(current, size);
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysMenu::getSortOrder);
        return menuMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysMenu> getTree() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysMenu::getSortOrder);
        List<SysMenu> allMenus = menuMapper.selectList(wrapper);
        return buildTree(allMenus, 0L);
    }

    private List<SysMenu> buildTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(parentId))
                .peek(m -> m.setChildren(buildTree(menus, m.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public SysMenu getById(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        return roleMenuMapper.selectList(wrapper).stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public SysMenu create(SysMenu menu) {
        menuMapper.insert(menu);
        return menu;
    }

    @Override
    public SysMenu update(Long id, SysMenu menu) {
        menu.setId(id);
        menuMapper.updateById(menu);
        return menu;
    }

    @Override
    public Boolean delete(Long id) {
        return menuMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public Boolean assignMenus(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);
        
        for (Long menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
        return true;
    }
}
