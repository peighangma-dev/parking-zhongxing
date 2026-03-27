package com.parking.uc.controller;

import com.parking.common.core.Result;
import com.parking.uc.entity.SysUser;
import com.parking.uc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "用户认证")
@RestController
@RequestMapping("/api/uc/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户登录")
    @PostMapping("/users/login")
    public Result<Map<String, Object>> login(
            @RequestParam String username,
            @RequestParam String password) {
        return Result.success(userService.login(username, password));
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/users/me")
    public Result<SysUser> getCurrentUser(@RequestHeader("Authorization") String token) {
        return Result.success(null);
    }
}
