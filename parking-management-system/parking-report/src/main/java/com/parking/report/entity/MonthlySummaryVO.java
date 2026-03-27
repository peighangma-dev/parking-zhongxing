package com.parking.report.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.YearMonth;

@Data
public class MonthlySummaryVO {
    private YearMonth month;
    private Long totalEntryCount;
    private Long totalExitCount;
    private BigDecimal totalRevenue;
    private BigDecimal avgDailyRevenue;
    private BigDecimal cashAmount;
    private BigDecimal electronicAmount;
    private Long totalOrders;
    private Long refundedOrders;
    private BigDecimal refundedAmount;
}
