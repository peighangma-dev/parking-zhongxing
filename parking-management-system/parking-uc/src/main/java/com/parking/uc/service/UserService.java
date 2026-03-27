package com.parking.uc.service;

import com.parking.uc.entity.SysUser;
import java.util.Map;

public interface UserService {

    Map<String, Object> login(String username, String password);

    SysUser getById(Long id);

    SysUser getByUsername(String username);
}
