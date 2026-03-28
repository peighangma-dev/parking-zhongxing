package com.parking.tenant.dto;

import lombok.Data;

@Data
public class TenantLoginDTO {
    private String tenantCode;
    private String username;
    private String password;
}
