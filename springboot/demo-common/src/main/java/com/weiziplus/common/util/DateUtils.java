package com.weiziplus.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author wanglongwei
 * @date 2020/05/27 14/54
 */
public class DateUtils {

    /**
     * 时间转时间字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        if (ToolUtils.isBlank(pattern)) {
            return null;
        }
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    /**
     * 时间转时间字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        if (null == date) {
            return null;
        }
        return dateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * string转LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate stringToLocalData(String date) {
        if (null == date) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    /**
     * string转LocalDateTime
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String dateTime) {
        if (null == dateTime) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * 时间字符串转时间
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date, String pattern) throws ParseException {
        if (ToolUtils.isBlank(date)) {
            return null;
        }
        if (ToolUtils.isBlank(pattern)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.parse(date);
    }

    /**
     * 时间字符串转时间
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date) throws ParseException {
        if (ToolUtils.isBlank(date)) {
            return null;
        }
        String basePattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(basePattern.substring(0, date.length()));
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.parse(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(formatter);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getNowDate() {
        return LocalDate.now().toString();
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Integer getNowDateNum() {
        return Integer.valueOf(LocalDate.now().toString());
    }

    public static void main(String[] args) {
        System.out.println(getNowDate());
        System.out.println(getNowDateTime());
        //2019-03-31
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        // 15:56:36.232
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        // 2019-03-31T15:56:36.233
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        // 2019-03-31T07:56:36.233Z
        Instant instant = Instant.now();
        System.out.println(instant);
        // 获取当前的时间戳（毫秒）
        long instantMilli = instant.toEpochMilli();
        System.out.println(instantMilli);
        // 一个小时后的时间=加了一个小时时间
        Instant plus1Hours = instant.plus(1, ChronoUnit.HOURS);
        // 计算两个时间之间的时间量
        long until = instant.until(plus1Hours, ChronoUnit.SECONDS);
        System.out.println(until);
        // 一个小时前的时间=减了一个小时
        Instant minus1Hours = instant.minus(1, ChronoUnit.HOURS);
        System.out.println(minus1Hours);
        // Instant转换成java.util.Date类型
        Date date = Date.from(instant);
        System.out.println(date);
        // java.util.Date转换成Instant类型
        Instant dateInstant = date.toInstant();
        System.out.println(dateInstant);
        // 当天开始时间
        LocalDateTime start = LocalDateTime.of(localDate, LocalTime.MIN);
        // 当天结束时间
        LocalDateTime end = LocalDateTime.of(localDate, LocalTime.MAX);
        System.out.println(start);
        System.out.println(end);
        // 计算两个时间之间的时间量度
        Duration duration = Duration.between(start, end);
        System.out.println(duration.toDays());
        System.out.println(duration.toHours());
        System.out.println(duration.toMinutes());
        System.out.println(duration.toMillis());
        System.out.println(duration.getSeconds());
        // 格式化初始指定时间
        System.out.println(LocalDateTime.parse("2019-03-01 11:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
