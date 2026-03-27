package com.parking.common.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SUCCESS(0, "success"),
    
    SYSTEM_ERROR(1000, "系统错误"),
    SYSTEM_BUSY(1001, "系统繁忙，请稍后重试"),
    INVALID_PARAMETER(1002, "参数错误"),
    NULL_POINTER(1003, "空指针异常"),
    
    UNAUTHORIZED(2000, "未登录或登录已过期"),
    FORBIDDEN(2001, "无权限访问"),
    TOKEN_INVALID(2002, "Token无效"),
    TOKEN_EXPIRED(2003, "Token已过期"),
    
    DEVICE_OFFLINE(3001, "设备离线"),
    DEVICE_FAULT(3002, "设备故障"),
    BARRIER_RAISE_TIMEOUT(3003, "抬杆超时"),
    BARRIER_LOWER_TIMEOUT(3004, "落杆超时"),
    
    PAYMENT_FAILED(4001, "支付失败"),
    PAYMENT_TIMEOUT(4002, "支付超时"),
    ORDER_NOT_FOUND(4003, "订单不存在"),
    ORDER_CANCELLED(4004, "订单已取消"),
    ORDER_EXPIRED(4005, "订单已过期"),
    INSUFFICIENT_BALANCE(4006, "余额不足"),
    
    VEHICLE_BLACKLISTED(5001, "车辆在黑名单"),
    MEMBER_EXPIRED(5002, "月卡已过期"),
    PARKING_FULL(5003, "车位已满"),
    VEHICLE_NOT_FOUND(5004, "车辆不存在"),
    MEMBER_NOT_FOUND(5005, "会员不存在"),
    
    FILE_UPLOAD_FAILED(6001, "文件上传失败"),
    FILE_NOT_FOUND(6002, "文件不存在"),
    STORAGE_CONFIG_ERROR(6003, "存储配置错误"),
    
    USER_NOT_FOUND(7001, "用户不存在"),
    USERNAME_EXISTS(7002, "用户名已存在"),
    PASSWORD_ERROR(7003, "密码错误"),
    ROLE_NOT_FOUND(7004, "角色不存在"),
    PERMISSION_DENIED(7005, "权限不足"),
    
    CAMERA_OFFLINE(8001, "摄像头离线"),
    VIDEO_STREAM_ERROR(8002, "视频流获取失败"),
    
    INVOICE_NOT_FOUND(9001, "发票不存在"),
    INVOICE_ISSUED(9002, "发票已开具"),
    INVOICE_CANCELLED(9003, "发票已作废"),
    
    RESOURCE_NOT_FOUND(9998, "资源不存在"),
    UNKNOWN_ERROR(9999, "未知错误");

    private final int code;
    private final String message;
}
