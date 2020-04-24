package com.hyqfx.employee.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * java8时间工具类
 * @author len
 * @describe
 * @createTime 2019/12/17
 */
public class Java8TimeUtil {


    public static final DateTimeFormatter defaultFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        now.format(defaultFormat);
        System.out.println(now.format(defaultFormat));
    }


    /**
     * 格式化日期
     * @param dateTime
     * @param formatter 可选
     * @return
     */
    public static String aliasHandle(LocalDateTime dateTime,DateTimeFormatter formatter){
        if(formatter == null){
            formatter =  defaultFormat;
        }
        LocalDate targetDay = dateTime.toLocalDate();
        LocalDate today = LocalDate.now();
        LocalDate nextDay = today.plusDays(1);
        if(today.isEqual(targetDay)){
            return "今日 " + dateTime.toLocalTime().toString();
        }else if (nextDay.isEqual(targetDay)){
            return "明日 " + dateTime.toLocalTime().toString();
        }else{
            return dateTime.format(formatter);
        }


    }

    /**
     * 获取下一个半点时间字符串
     * 如：2020-01-01 00:00:01 下一个半点是：2020-01-01 00:30:00
     *     2020-01-01 00:45:45 下一个半点是：2020-01-01 01:00:00
     * @param timeStr 必须以格式 yyyy-MM-dd HH:mm:ss 传入
     * @return
     */
    public static String getNextHalfHour(String timeStr){
        if(timeStr == null){
            return timeStr;
        }
        String[] arr = timeStr.split(":");
        if(arr == null || arr.length != 3){
            return null;
        }
        Integer minute = Integer.valueOf(arr[1]);
        if(minute < 30){
            arr[1] = "30";
            return arr[0]+":"+arr[1]+":00";
        }else{
            arr[1] = "00";
            String[] dh = arr[0].split(" ");
            Integer hour = Integer.valueOf(dh[1]);
            if(hour == 23){
                LocalDate date = LocalDate.parse(dh[0],DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                date.plusDays(1);
                return date.toString()+" 00:00:00";
            }
            hour += 1;
            return dh[0]+" "+hour+":00:00";
        }
    }

    /**
     * 获取当前时间字符串
     * 默认格式：yyyy-MM-dd HH:mm:ss
     * @param format
     * @return
     * @throws Exception
     */
    public static String getNowStr(String format)throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String timeStr = null;
        if(format == null){
            timeStr = now.format(defaultFormat);
        }else{
            timeStr = now.format(DateTimeFormatter.ofPattern(format));
        }
        return timeStr;
    }

    /**
     * 获取当天的最初时间点
     * eg. 2019-12-12T00:00
     */
    public static LocalDateTime getBeginTime(String dateStr)throws Exception {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return beginTime;
    }

    /**
     * 获取当天的最终时间点
     * eg. 2019-12-12T23:59:59.999999999
     */
    public static LocalDateTime getEndTime(String dateStr)throws Exception {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return endTime;
    }


    /**
     * 获取这个月的第一天 LocalDateTime
     * eg. 2019-12-01T00:00
     */
    public static LocalDateTime firstDayOfMonthLDT(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime today = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime firstday = today.with(TemporalAdjusters.firstDayOfMonth());
        return firstday;
    }

    /**
     * 获取这个月的最后一天 LocalDateTime
     * eg. 2019-12-31T23:59:59.999999999
     */
    public static LocalDateTime lastDayOfMonthLDT(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime today = LocalDateTime.of(localDate, LocalTime.MAX);
        LocalDateTime lastday = today.with(TemporalAdjusters.lastDayOfMonth());
        return lastday;
    }

    /**
     * 时间段切割
     *
     * @param beginDate
     * @param endDate
     * @param interval  时间间隔，单位：分钟
     * @return
     */
    public static List<LocalDateTime> cutTime(LocalDateTime beginDate, LocalDateTime endDate, int interval) {
        if (!ObjectUtils.allNotNull(beginDate, endDate, interval)) {
            return null;
        }
        if (beginDate.isAfter(endDate)) {
            return null;
        }
        List<LocalDateTime> dateList = new ArrayList<>();
        LocalDateTime tempDate = beginDate;
        while (tempDate.isBefore(endDate)) {
            dateList.add(tempDate);
            tempDate = tempDate.plusMinutes(interval);
        }

        return dateList;
    }

    /**
     * 根据日期对象获取时分格式的字符串
     *
     * @param time
     * @return
     */
    public static String getHAMTime(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.toLocalTime().format(formatter);
    }

    /**
     * 判断b是否在a之前或 ab相等
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqualAndBeforeLT(LocalTime a, LocalTime b) {
        if (b.equals(a) || b.isBefore(a)) {
            return true;
        }
        return false;
    }

    /**
     * 比较日期时间对象中的时间大小
     * 时间a是否小于等于时间b
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqualAndBefore(LocalDateTime a, LocalDateTime b) {
        LocalTime alt = a.toLocalTime();
        LocalTime blt = b.toLocalTime();
        if (alt.equals(blt) || alt.isBefore(blt)) {
            return true;
        }
        return false;
    }

    /**
     * 比较日期时间对象中的时间大小
     * 时间a是否大于等于时间b
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqualAndAfter(LocalDateTime a, LocalDateTime b) {
        LocalTime alt = a.toLocalTime();
        LocalTime blt = b.toLocalTime();
        if (alt.equals(blt) || alt.isAfter(blt)) {
            return true;
        }
        return false;
    }

    /**
     * 判断b是否在a之后或 ab相等
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqualAndAfterLT(LocalTime a, LocalTime b) {
        if (b.equals(a)|| b.isAfter(a)) {
            return true;
        }
        return false;
    }

    /**
     * 比较日期时间对象中的时间大小
     * @param a
     * @param b
     * @return
     */
    public static boolean localTimeIsBefore(LocalDateTime a, LocalDateTime b){
        LocalTime alt = a.toLocalTime();
        LocalTime blt = b.toLocalTime();
        if (alt.isBefore(blt)){
            return true;
        }
        return false;

    }
	
	/**
     * 获取上个月第一天
     * @param currentLD
     * @return
     */
    public static LocalDate firstDayOfLastMonth(LocalDate currentLD){
        if(currentLD == null){
            currentLD = LocalDate.now();
        }
        return currentLD.plusMonths(-1).with(TemporalAdjusters.firstDayOfMonth());
    }


}
