package com.parking.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.parking.common.core.BusinessException;
import com.parking.common.core.ErrorCode;
import com.parking.uc.entity.SysUser;
import com.parking.uc.mapper.SysUserMapper;
import com.parking.uc.service.UserService;
import com.parking.uc.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper userMapper;
    private final JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(String username, String password) {
        SysUser user = getByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (!"normal".equals(user.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "用户已被禁用");
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
        return user;
    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return userMapper.selectOne(wrapper);
    }
}
