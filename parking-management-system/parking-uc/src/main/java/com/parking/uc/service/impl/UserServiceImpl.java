package com.parking.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.uc.entity.SysUser;
import com.parking.uc.entity.SysUserRole;
import com.parking.uc.mapper.SysUserMapper;
import com.parking.uc.mapper.SysUserRoleMapper;
import com.parking.uc.service.UserService;
import com.parking.uc.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(String username, String password) {
        SysUser user = getByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND.getCode(), "用户名或密码错误");
        }
        String hashedPassword = hashPassword(password);
        if (!hashedPassword.equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "用户名或密码错误");
        }
        if (!"normal".equals(user.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN.getCode(), "用户已被禁用");
        }
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        return result;
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public SysUser register(SysUser user) {
        if (getByUsername(user.getUsername()) != null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "用户名已存在");
        }
        user.setPassword(hashPassword(user.getPassword()));
        user.setStatus("normal");
        userMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public IPage<SysUser> page(Integer current, Integer size, String username, String nickname) {
        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like(SysUser::getUsername, username);
        }
        if (nickname != null && !nickname.isEmpty()) {
            wrapper.like(SysUser::getNickname, nickname);
        }
        wrapper.orderByDesc(SysUser::getId);
        IPage<SysUser> result = userMapper.selectPage(page, wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return result;
    }

    @Override
    public boolean updateUser(Long id, SysUser user) {
        SysUser existing = getById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.setId(id);
        user.setPassword(null);
        userMapper.updateById(user);
        return true;
    }

    @Override
    public boolean deleteUser(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        String hashedOld = hashPassword(oldPassword);
        if (!hashedOld.equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "原密码错误");
        }
        SysUser update = new SysUser();
        update.setId(id);
        update.setPassword(hashPassword(newPassword));
        userMapper.updateById(update);
        return true;
    }

    @Override
    public List<Long> getRoleIds(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        return userRoleMapper.selectList(wrapper).stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        userRoleMapper.delete(wrapper);
        
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
        return true;
    }

    private String hashPassword(String password) {
        return DigestUtils.md5DigestAsHex(("parking" + password + "salt").getBytes(StandardCharsets.UTF_8));
    }
}
