package com.parking.common.core;

import cn.hutool.core.date.DateUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_MINI_PATTERN = "yyyyMMddHHmmss";
    public static final String DATE_COMPACT_PATTERN = "yyyyMMdd";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    public static final DateTimeFormatter DATETIME_MINI_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_MINI_PATTERN);
    public static final DateTimeFormatter DATE_COMPACT_FORMATTER = DateTimeFormatter.ofPattern(DATE_COMPACT_PATTERN);

    public static String now() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    public static String nowDate() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    public static String nowTime() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATETIME_FORMATTER);
    }

    public static String format(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMATTER);
    }

    public static String formatCompact(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATETIME_MINI_FORMATTER);
    }

    public static LocalDateTime parse(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static long betweenMinutes(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }

    public static long betweenHours(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toHours();
    }

    public static long betweenDays(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    public static boolean isToday(LocalDateTime dateTime) {
        LocalDate today = LocalDate.now();
        return dateTime.toLocalDate().equals(today);
    }

    public static boolean isWeekend(LocalDateTime dateTime) {
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        return dayOfWeek == 6 || dayOfWeek == 7;
    }

    public static LocalDateTime startOfDay(LocalDateTime dateTime) {
        return dateTime.with(LocalTime.MIN);
    }

    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        return dateTime.with(LocalTime.MAX);
    }

    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime.plusDays(days);
    }

    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime.plusHours(hours);
    }

    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.plusMinutes(minutes);
    }

    public static String formatCn(LocalDateTime dateTime) {
        return DateUtil.format(dateTime, DATETIME_PATTERN);
    }
}
