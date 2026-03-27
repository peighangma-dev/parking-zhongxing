package com.parking.uc.controller;

import com.parking.common.core.Result;
import com.parking.uc.entity.SysUser;
import com.parking.uc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// @RestController
@RestController
@RequestMapping("/api/uc/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public Result<Map<String, Object>> login(
            @RequestParam String username,
            @RequestParam String password) {
        return Result.success(userService.login(username, password));
    }

    @GetMapping("/current")
    public Result<SysUser> getCurrentUser(@RequestHeader("Authorization") String token) {
        return Result.success(null);
    }
}
