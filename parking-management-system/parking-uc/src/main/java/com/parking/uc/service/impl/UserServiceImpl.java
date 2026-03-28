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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Map<String, Object> login(String username, String password) {
        SysUser user = getByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND.getCode(), "用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
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
        user.setPassword(encodePassword(user.getPassword()));
        user.setStatus("normal");
        userMapper.insert(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public IPage<SysUser> page(Integer current, Integer size, String username, String nickname) {
        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like(SysUser::getUsername, username);
        }
        if (StringUtils.hasText(nickname)) {
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
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "原密码错误");
        }
        if (oldPassword.equals(newPassword)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "新密码不能与原密码相同");
        }
        if (!isPasswordStrong(newPassword)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode(), "密码强度不足，至少8位，包含字母和数字");
        }
        SysUser update = new SysUser();
        update.setId(id);
        update.setPassword(encodePassword(newPassword));
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

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }
}
