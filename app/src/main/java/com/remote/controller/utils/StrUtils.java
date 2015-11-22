package com.remote.controller.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chen Haitao on 2015/7/23.
 */
public class StrUtils {
    public static final String REPORT_MEMBER_CONSUME_SEARCH_KEY = "^[0-9a-zA-Z\\u4e00-\\u9fa5\\s]{1,50}$";  // 必须为1-50个字符(中文、数字和英文)
    public static final String PHONE_REGEX = "^(\\+?\\d{2}-?)?(1[0-9])\\d{9}$";   // 手机号;
    public static final String TRUE_NAME = "^[0-9a-zA-Z\\u4e00-\\u9fa5\\s]{2,15}$";  // 必须为2-15个字符(中文、数字和英文)
    public static final String USER_NAME = "^[0-9a-zA-Z\\u4e00-\\u9fa5\\-_\\s]{3,25}$";  // 必须为3-25个字符(支持中文、英文、数字及-和_)
    //public static final String PASSWD = "^(?![a-z]+$)(?![A-Z]+$)(?![0-9]+$)[0-9a-zA-Z\\W]\\S{6,20}$";  // 6-20位字符，包含字母、数字或符号中两种
    public static final String PASSWD = "^[0-9a-zA-Z\\W]\\S{5,20}$";  // 6-20位字符，支持字母、数字或符号的组合
    public static final String DEVICE_NO = "^[0-9a-zA-Z]{1,6}$";  // 1-6位字符，支持字母、数字
    public static final String DITITAL = "^[0-9.]+$";  //


    public static String getText(String text) {
        if (text == null) {
            return "";
        }
        return text.trim();
    }

    public static boolean isNull(String str) {
        return str == null || str.length() == 0 || str.trim().equalsIgnoreCase("null");
    }

    public static boolean isNotNull(String str) {
        return str != null && str.length() > 0 && (!str.trim().equalsIgnoreCase("null"));
    }

    public static String getDecimalFormatStr(double original) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(original);
    }

//    public static double getDecimalFormat(double original) {
//        DecimalFormat df = new DecimalFormat("######0.00");
//        L.e("str : " + df.format(original));
//        return Double.parseDouble(df.format(original));
//    }

    public static String getBigDecimalFormatStr(BigDecimal original) {
        if (original == null) {
            L.e("BigDecimal is null");
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(original);
    }

    public static boolean isMobileNum(String phone) {
        Pattern p = Pattern.compile(PHONE_REGEX);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    public static boolean match(String matcher, String str) {
        Pattern p = Pattern.compile(matcher);
        Matcher m = p.matcher(str);
        if (m.find()) {
            L.d("match : matcher=" + matcher + ", str=" + str);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDigital(String d) {
        return match(DITITAL, d);
    }


    public static String getSignStr(Object object) {
        if (object == null) {
            return "";
        }
        return String.format("%.2f", object);
    }

    public static String getSignStr(double d) {
        return String.format("%.2f", d);
    }


    public static String getRandomBarCode() {
        StringBuilder str = new StringBuilder("66");
        int a[] = new int[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = (int) (10 * (Math.random()));
            str.append(a[i]);
        }
        str.append(getCheckCode(str.toString()));
        return str.toString();
    }

    private static String getCheckCode(String string) {
        int evenCount = 0;
        int oddCount = 0;
        for (int i = string.length() - 1; i >= 0; i--) {
            int j = string.length() - i;
            if (j % 2 == 1) {
                evenCount += Integer.parseInt(string.substring(i, i + 1));
            } else {
                oddCount += Integer.parseInt(string.substring(i, i + 1));
            }
        }
        return String.valueOf((evenCount * 3 + oddCount) % 10);
    }

    public static void setTextStyle(Context ctx, TextView tx, int resId, int val, int styleId, int start, int end) {
        SpannableString styledText = new SpannableString(String.format(ctx.getString(resId), String.valueOf(val)));
        styledText.setSpan(new TextAppearanceSpan(ctx, styleId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tx.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    public static void setTextStyle(Context ctx, TextView tx, int resId, long val, int styleId, int start, int end) {
        SpannableString styledText = new SpannableString(String.format(ctx.getString(resId), String.valueOf(val)));
        styledText.setSpan(new TextAppearanceSpan(ctx, styleId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tx.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    public static void setTextStyle(Context ctx, TextView tx, int resId, double val, int styleId, int start, int end) {
        SpannableString styledText = new SpannableString(String.format(ctx.getString(resId), StrUtils.getDecimalFormatStr(val)));
        styledText.setSpan(new TextAppearanceSpan(ctx, styleId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tx.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    public static void setTextStyle(Context ctx, TextView tx, int resId, String val, int styleId, int start, int end) {
        SpannableString styledText = new SpannableString(String.format(ctx.getString(resId), val));
        styledText.setSpan(new TextAppearanceSpan(ctx, styleId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tx.setText(styledText, TextView.BufferType.SPANNABLE);
    }

    public static String setGetPhoneMask(String input) {
        if (StrUtils.isNotNull(input)) {
            String maskNumber = input.substring(0, 3) + "****" + input.substring(7, input.length());
            return maskNumber;
        }
        return "";
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
