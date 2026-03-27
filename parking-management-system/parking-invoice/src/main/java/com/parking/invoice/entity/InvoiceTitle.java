package com.parking.invoice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.parking.common.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("invoice_title")
public class InvoiceTitle extends BaseEntity {

    private Long userId;

    private String titleType;

    private String companyName;

    private String taxNumber;

    private String bankName;

    private String bankAccount;

    private String address;

    private String phone;

    private String email;

    private Integer isDefault;
}
