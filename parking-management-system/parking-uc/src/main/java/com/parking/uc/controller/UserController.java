package com.parking.uc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.uc.entity.SysUser;
import com.parking.uc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
