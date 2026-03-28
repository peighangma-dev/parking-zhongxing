package com.parking.uc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.uc.entity.SysUser;
import com.parking.uc.entity.SysRole;
import com.parking.uc.service.UserService;
import com.parking.uc.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/uc/v1")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("密码不能为空");
        }
        return Result.success(userService.login(username, password));
    }

    @GetMapping("/current")
    public Result<Map<String, Object>> getCurrentUser(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return Result.error("未授权");
        }
        token = token.substring(7);
        if (!jwtUtils.validateToken(token)) {
            return Result.error("token无效或已过期");
        }
        Long userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("无法解析用户信息");
        }
        SysUser user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        List<SysRole> roles = userService.getUserRoles(userId);
        boolean isSuperAdmin = roles.stream().anyMatch(r -> "SUPER_ADMIN".equals(r.getRoleCode()));
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("tenantId", user.getTenantId());
        result.put("isSuperAdmin", isSuperAdmin);
        result.put("roles", roles);
        return Result.success(result);
    }

    @GetMapping("/users/page")
    public Result<IPage<SysUser>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname) {
        return Result.success(userService.page(current, size, username, nickname));
    }

    @GetMapping("/users/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PostMapping("/users")
    public Result<SysUser> register(@RequestBody SysUser user) {
        return Result.success(userService.register(user));
    }

    @PutMapping("/users/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody SysUser user) {
        return Result.success(userService.updateUser(id, user));
    }

    @DeleteMapping("/users/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.deleteUser(id));
    }

    @PutMapping("/users/{id}/password")
    public Result<Boolean> updatePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        return Result.success(userService.updatePassword(id, oldPassword, newPassword));
    }

    @GetMapping("/users/{id}/roles")
    public Result<List<Long>> getUserRoles(@PathVariable Long id) {
        return Result.success(userService.getRoleIds(id));
    }

    @PutMapping("/users/{id}/roles")
    public Result<Boolean> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        return Result.success(userService.assignRoles(id, roleIds));
    }
}
