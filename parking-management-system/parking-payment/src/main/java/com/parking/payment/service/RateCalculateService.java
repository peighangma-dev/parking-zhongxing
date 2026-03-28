package com.parking.payment.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RateCalculateService {
    
    BigDecimal calculateFee(String plateNumber, LocalDateTime entryTime, LocalDateTime exitTime);
    
    BigDecimal calculateFee(String plateNumber, LocalDateTime entryTime, LocalDateTime exitTime, String vehicleCategory);
}
