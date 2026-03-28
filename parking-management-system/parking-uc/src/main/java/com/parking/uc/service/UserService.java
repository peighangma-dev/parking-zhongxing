package com.parking.uc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.uc.entity.SysUser;
import java.util.List;
import java.util.Map;

public interface UserService {

    Map<String, Object> login(String username, String password);

    SysUser getById(Long id);

    SysUser getByUsername(String username);

    SysUser register(SysUser user);

    IPage<SysUser> page(Integer current, Integer size, String username, String nickname);

    boolean updateUser(Long id, SysUser user);

    boolean deleteUser(Long id);

    boolean updatePassword(Long id, String oldPassword, String newPassword);

    List<Long> getRoleIds(Long userId);

    boolean assignRoles(Long userId, List<Long> roleIds);
}
