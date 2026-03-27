package com.parking.report.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.common.core.Result;
import com.parking.report.entity.DailySummaryVO;
import com.parking.report.entity.MonthlySummaryVO;
import com.parking.report.entity.PassRecord;
import com.parking.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

// @RestController
@RestController
@RequestMapping("/api/report/v1")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public Result<IPage<PassRecord>> queryPassRecords(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String passType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.success(reportService.queryPassRecords(current, size, plateNumber, passType, startTime, endTime));
    }

    @GetMapping
    public Result<List<PassRecord>> exportPassRecords(
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String passType,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return Result.success(reportService.exportPassRecords(plateNumber, passType, startTime, endTime));
    }

    @GetMapping
    public Result<DailySummaryVO> getDailySummary(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return Result.success(reportService.getDailySummary(date));
    }

    @GetMapping
    public Result<MonthlySummaryVO> getMonthlySummary(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return Result.success(reportService.getMonthlySummary(year, month));
    }

    @GetMapping
    public Result<String> reconcile(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        DailySummaryVO summary = reportService.getDailySummary(date);
        return Result.success("对账完成，当日入场: " + summary.getEntryCount() + 
                           ", 出场: " + summary.getExitCount() + 
                           ", 在场: " + summary.getCurrentParked());
    }
}
