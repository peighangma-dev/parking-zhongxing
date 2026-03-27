package com.parking.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.parking.report.entity.DailySummaryVO;
import com.parking.report.entity.MonthlySummaryVO;
import com.parking.report.entity.PassRecord;
import com.parking.report.mapper.PassRecordMapper;
import com.parking.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PassRecordMapper passRecordMapper;

    @Override
    public IPage<PassRecord> queryPassRecords(Integer current, Integer size, String plateNumber,
                                               String passType, String startTime, String endTime) {
        Page<PassRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<PassRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(plateNumber)) {
            wrapper.like(PassRecord::getPlateNumber, plateNumber);
        }
        if (StringUtils.hasText(passType)) {
            wrapper.eq(PassRecord::getPassType, passType);
        }
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(PassRecord::getPassTime, LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(PassRecord::getPassTime, LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        wrapper.orderByDesc(PassRecord::getPassTime);
        return passRecordMapper.selectPage(page, wrapper);
    }

    @Override
    public List<PassRecord> exportPassRecords(String plateNumber, String passType,
                                               String startTime, String endTime) {
        LambdaQueryWrapper<PassRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(plateNumber)) {
            wrapper.like(PassRecord::getPlateNumber, plateNumber);
        }
        if (StringUtils.hasText(passType)) {
            wrapper.eq(PassRecord::getPassType, passType);
        }
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(PassRecord::getPassTime, LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(PassRecord::getPassTime, LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        wrapper.orderByDesc(PassRecord::getPassTime);
        return passRecordMapper.selectList(wrapper);
    }

    @Override
    public DailySummaryVO getDailySummary(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        LambdaQueryWrapper<PassRecord> entryWrapper = new LambdaQueryWrapper<>();
        entryWrapper.eq(PassRecord::getPassType, "entry");
        entryWrapper.between(PassRecord::getPassTime, startOfDay, endOfDay);
        Long entryCount = passRecordMapper.selectCount(entryWrapper);
        
        LambdaQueryWrapper<PassRecord> exitWrapper = new LambdaQueryWrapper<>();
        exitWrapper.eq(PassRecord::getPassType, "exit");
        exitWrapper.between(PassRecord::getPassTime, startOfDay, endOfDay);
        Long exitCount = passRecordMapper.selectCount(exitWrapper);
        
        DailySummaryVO summary = new DailySummaryVO();
        summary.setSummaryDate(date);
        summary.setEntryCount(entryCount);
        summary.setExitCount(exitCount);
        summary.setCurrentParked(entryCount - exitCount);
        summary.setTotalAmountDue(BigDecimal.ZERO);
        summary.setTotalAmountPaid(BigDecimal.ZERO);
        
        return summary;
    }

    @Override
    public MonthlySummaryVO getMonthlySummary(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        
        LambdaQueryWrapper<PassRecord> entryWrapper = new LambdaQueryWrapper<>();
        entryWrapper.eq(PassRecord::getPassType, "entry");
        entryWrapper.between(PassRecord::getPassTime, startOfMonth, endOfMonth);
        Long totalEntryCount = passRecordMapper.selectCount(entryWrapper);
        
        LambdaQueryWrapper<PassRecord> exitWrapper = new LambdaQueryWrapper<>();
        exitWrapper.eq(PassRecord::getPassType, "exit");
        exitWrapper.between(PassRecord::getPassTime, startOfMonth, endOfMonth);
        Long totalExitCount = passRecordMapper.selectCount(exitWrapper);
        
        MonthlySummaryVO summary = new MonthlySummaryVO();
        summary.setMonth(yearMonth);
        summary.setTotalEntryCount(totalEntryCount);
        summary.setTotalExitCount(totalExitCount);
        summary.setTotalRevenue(BigDecimal.ZERO);
        summary.setAvgDailyRevenue(BigDecimal.ZERO);
        
        return summary;
    }

    @Override
    public List<DailySummaryVO> getDailySummaries(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }
}
