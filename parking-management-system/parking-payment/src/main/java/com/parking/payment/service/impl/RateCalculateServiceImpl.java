package com.parking.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.parking.common.core.BusinessException;
import com.parking.payment.entity.RateRule;
import com.parking.payment.mapper.RateRuleMapper;
import com.parking.payment.service.RateCalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateCalculateServiceImpl implements RateCalculateService {

    private final RateRuleMapper rateRuleMapper;

    private static final BigDecimal FIRST_HOUR_MINUTES = BigDecimal.valueOf(60);
    private static final BigDecimal MAX_DAYS = BigDecimal.valueOf(24);

    @Override
    public BigDecimal calculateFee(String plateNumber, LocalDateTime entryTime, LocalDateTime exitTime) {
        return calculateFee(plateNumber, entryTime, exitTime, "all");
    }

    @Override
    public BigDecimal calculateFee(String plateNumber, LocalDateTime entryTime, LocalDateTime exitTime, String vehicleCategory) {
        if (entryTime == null || exitTime == null) {
            throw new BusinessException(400, "入场时间和离场时间不能为空");
        }
        if (exitTime.isBefore(entryTime)) {
            throw new BusinessException(400, "离场时间不能早于入场时间");
        }
        
        long totalMinutes = Duration.between(entryTime, exitTime).toMinutes();
        if (totalMinutes <= 0) {
            return BigDecimal.ZERO;
        }
        
        if (totalMinutes <= 15) {
            return BigDecimal.ZERO;
        }
        
        RateRule rule = selectApplicableRule(entryTime, vehicleCategory);
        if (rule == null) {
            throw new BusinessException(500, "未找到适用的费率规则");
        }
        
        BigDecimal totalFee;
        if (totalMinutes <= 60) {
            totalFee = rule.getFirstHourFee();
        } else {
            long additionalMinutes = totalMinutes - 60;
            BigDecimal additionalHours = BigDecimal.valueOf(additionalMinutes)
                    .divide(FIRST_HOUR_MINUTES, 2, RoundingMode.CEILING);
            totalFee = rule.getFirstHourFee()
                    .add(additionalHours.multiply(rule.getSubsequentFee()));
        }
        
        if (rule.getDailyMaxFee() != null) {
            BigDecimal days = BigDecimal.valueOf(totalMinutes)
                    .divide(BigDecimal.valueOf(1440), 2, RoundingMode.CEILING);
            BigDecimal maxFee = rule.getDailyMaxFee().multiply(days);
            if (totalFee.compareTo(maxFee) > 0) {
                totalFee = maxFee;
            }
        }
        
        return totalFee.setScale(2, RoundingMode.HALF_UP);
    }

    private RateRule selectApplicableRule(LocalDateTime dateTime, String vehicleCategory) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        LocalTime time = dateTime.toLocalTime();
        boolean isNight = time.isAfter(LocalTime.of(22, 0)) || time.isBefore(LocalTime.of(6, 0));
        
        LambdaQueryWrapper<RateRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RateRule::getStatus, "active");
        wrapper.eq(RateRule::getVehicleCategory, vehicleCategory);
        wrapper.orderByDesc(RateRule::getPriority);
        
        List<RateRule> rules = rateRuleMapper.selectList(wrapper);
        
        if (rules == null || rules.isEmpty()) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RateRule::getStatus, "active");
            wrapper.eq(RateRule::getVehicleCategory, "all");
            wrapper.orderByDesc(RateRule::getPriority);
            rules = rateRuleMapper.selectList(wrapper);
        }
        
        for (RateRule rule : rules) {
            if (isWeekend && "weekend".equals(rule.getRuleType())) {
                return rule;
            }
            if (isNight && "night".equals(rule.getRuleType())) {
                return rule;
            }
            if ("default".equals(rule.getRuleType())) {
                return rule;
            }
        }
        
        return rules.isEmpty() ? null : rules.get(0);
    }
}
