package com.parking.common.core;

import java.util.UUID;

public class StringUtils {

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    public static String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static String format(String template, Object... params) {
        if (template == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);
            if (c == '{' && index < params.length) {
                sb.append(params[index++]);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String sub(String str, int fromIndex, int toIndex) {
        if (str == null) {
            return null;
        }
        if (fromIndex < 0) fromIndex = 0;
        if (toIndex > str.length()) toIndex = str.length();
        if (fromIndex > toIndex) return "";
        return str.substring(fromIndex, toIndex);
    }

    public static boolean contains(CharSequence sequence, CharSequence searchSeq) {
        if (sequence == null || searchSeq == null) {
            return false;
        }
        return sequence.toString().contains(searchSeq);
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toString().toLowerCase().contains(searchStr.toString().toLowerCase());
    }

    public static String replace(String text, String searchString, String replacement) {
        if (isEmpty(text) || isEmpty(searchString)) {
            return text;
        }
        return text.replace(searchString, replacement == null ? "" : replacement);
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
