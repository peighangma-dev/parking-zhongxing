package com.parking.common.core;

import cn.hutool.core.util.StrUtil;

import java.util.UUID;

public class StringUtils extends cn.hutool.core.util.StrUtil {

    public static boolean isBlank(String str) {
        return StrUtil.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StrUtil.isNotBlank(str);
    }

    public static boolean isEmpty(String str) {
        return StrUtil.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return StrUtil.isNotEmpty(str);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String defaultIfBlank(String str, String defaultStr) {
        return StrUtil.defaultIfBlank(str, defaultStr);
    }

    public static String defaultIfEmpty(String str, String defaultStr) {
        return StrUtil.defaultIfEmpty(str, defaultStr);
    }

    public static String format(String template, Object... params) {
        return StrUtil.format(template, params);
    }

    public static String capitalize(String str) {
        return StrUtil.capitalize(str);
    }

    public static String uncapitalize(String str) {
        return StrUtil.uncapitalize(str);
    }

    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String sub(String str, int fromIndex, int toIndex) {
        return StrUtil.sub(str, fromIndex, toIndex);
    }

    public static boolean contains(CharSequence sequence, CharSequence searchSeq) {
        return StrUtil.contains(sequence, searchSeq);
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StrUtil.containsIgnoreCase(str, searchStr);
    }

    public static String replace(String text, String searchString, String replacement) {
        return StrUtil.replace(text, searchString, replacement);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String mask(String str, int start, int end, char maskChar) {
        if (isEmpty(str)) {
            return str;
        }
        int length = str.length();
        if (start < 0 || end > length || start > end) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i >= start && i < end) {
                sb.append(maskChar);
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() < 7) {
            return phone;
        }
        return mask(phone, 3, 7, '*');
    }

    public static String maskIdCard(String idCard) {
        if (isEmpty(idCard) || idCard.length() < 8) {
            return idCard;
        }
        return mask(idCard, 4, idCard.length() - 4, '*');
    }

    public static String maskPlateNumber(String plateNumber) {
        if (isEmpty(plateNumber) || plateNumber.length() < 5) {
            return plateNumber;
        }
        return mask(plateNumber, 2, plateNumber.length() - 1, '*');
    }
}
