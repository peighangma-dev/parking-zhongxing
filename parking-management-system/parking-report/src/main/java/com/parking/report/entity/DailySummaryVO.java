package com.parking.report.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DailySummaryVO {
    private LocalDate summaryDate;
    private Long entryCount;
    private Long exitCount;
    private Long currentParked;
    private BigDecimal totalAmountDue;
    private BigDecimal totalAmountPaid;
    private BigDecimal cashAmount;
    private BigDecimal wechatAmount;
    private BigDecimal alipayAmount;
    private BigDecimal etcAmount;
    private Long wechatCount;
    private Long alipayCount;
    private Long cashCount;
    private Long etcCount;
}
