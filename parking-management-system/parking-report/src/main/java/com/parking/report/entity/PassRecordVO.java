package com.parking.report.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PassRecordVO {
    private Long id;
    private String plateNumber;
    private String laneName;
    private String passType;
    private String passStatus;
    private BigDecimal recognitionConfidence;
    private String imageUrl;
    private BigDecimal feeCalculated;
    private LocalDateTime passTime;
    private String barrierName;
}
