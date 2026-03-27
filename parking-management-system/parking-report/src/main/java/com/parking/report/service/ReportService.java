package com.parking.report.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.parking.report.entity.DailySummaryVO;
import com.parking.report.entity.MonthlySummaryVO;
import com.parking.report.entity.PassRecord;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    IPage<PassRecord> queryPassRecords(Integer current, Integer size, String plateNumber, 
                                        String passType, String startTime, String endTime);

    List<PassRecord> exportPassRecords(String plateNumber, String passType, 
                                        String startTime, String endTime);

    DailySummaryVO getDailySummary(LocalDate date);

    MonthlySummaryVO getMonthlySummary(Integer year, Integer month);

    List<DailySummaryVO> getDailySummaries(LocalDate startDate, LocalDate endDate);
}
