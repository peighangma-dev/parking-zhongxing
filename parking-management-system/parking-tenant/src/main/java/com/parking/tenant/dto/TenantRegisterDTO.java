package com.parking.tenant.dto;

import lombok.Data;

@Data
public class TenantRegisterDTO {
    private String tenantCode;
    private String tenantName;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String password;
    private Long packageId;
}
