package cn.mianshiyi.braumadmin.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具
 */
public class DateUtil {
    public static final String YYYY_MM_dd_HHMMSS_NORMAL = "yyyy-MM-dd HH:mm:ss"; // 日期格式
    public static final String YYYY_MM_dd_HHMMSSSSS_NORMAL = "yyyy-MM-dd HH:mm:ss.SSS"; // 日期格式
    public static final String YYYY_MM_dd = "yyyy-MM-dd"; // 年月日格式
    public static final String YYYYMMdd = "yyyyMMdd"; // 年月日格式
    public static final String YYYYMMddHHmm = "yyyyMMddHHmm"; // 日期格式
    public static final String yyyy_MM_ddTHHmmssSSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String yyyy_MM_ddTHHmmssSSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String yyyy_MM_ddTHHmmssSSSSSSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX";
    public static final int DAYSBETWEEN_180 = 180;
    public static final String yyyy_MM_ddTHHmmss = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String MM_dd = "MM.dd";
    public static final String yyyyMMddHHmmss_NORMAL = "yyyyMMddHHmmss";


    public static Date min(Date a, Date b) {
        if (a == null || b == null) {
            return null;
        }
        return a.before(b) ? a : b;
    }

    public static Date max(Date a, Date b) {
        if (a == null || b == null) {
            return null;
        }
        return a.after(b) ? a : b;
    }

    public static String min(String a, String b) throws Exception {
        if (a == null || b == null) {
            return null;
        }
        Date aDate = DateUtils.parseDate(a, YYYY_MM_dd);
        Date bDate = DateUtils.parseDate(b, YYYY_MM_dd);
        return aDate.before(bDate) ? a : b;
    }

    public static String max(String a, String b) throws Exception {
        if (a == null || b == null) {
            return null;
        }
        Date aDate = DateUtils.parseDate(a, YYYY_MM_dd);
        Date bDate = DateUtils.parseDate(b, YYYY_MM_dd);
        return aDate.after(bDate) ? a : b;
    }

    /**
     * 当前日期是星期几
     */
    public static int nowForWeek() {
        Calendar c = Calendar.getInstance();
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 获取当前日期的本星期一00:00:00
     */
    public static long getPreMonday() {
        return getMonday(2);
    }

    /**
     * 获取当前日期的下星期一00:00:00
     */
    public static long getNextMonday() {
        return getMonday(9);
    }

    /**
     * 获取星期一00:00:00
     */
    private static long getMonday(int daynum) {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int lastMonday = c.get(Calendar.DATE) - dayOfWeek + daynum;
        c.set(Calendar.DATE, lastMonday);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * 获取当前日期的本星期一00:00:00
     */
    public static Date getThisMonday() {
        return new Date(getMonday(2));
    }

    /**
     * 返回中文星期
     *
     * @param num
     * @return
     */
    public static String getWeekCn(int num) {
        String[] ch_Chars = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        return 0 <= num && num <= 6 ? ch_Chars[num] : String.valueOf(num);
    }

    /**
     * 当天的剩余秒数
     *
     * @return
     */
    public static int remainingSecondInToday() {
        return (24 * 3600) - getTodayElapsedSeconds();
    }

    /**
     * 获取今天0点到现在时刻之间的秒数
     *
     * @return
     */
    public static int getTodayElapsedSeconds() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 小时
        int minute = cal.get(Calendar.MINUTE);// 分           
        int second = cal.get(Calendar.SECOND);// 秒
        return hour * 3600 + minute * 60 + second;
    }

    /**
     * 获取今天0点到现在时刻之间的分钟数
     *
     * @return
     */
    public static int getTodayElapsedMinutes(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 小时
        int minute = cal.get(Calendar.MINUTE);// 分           
        int second = cal.get(Calendar.SECOND);// 秒
        return hour * 60 + minute;
    }

    public static Date getYesterdayEnd() {
        Calendar cal = Calendar.getInstance();
        Date truncate = DateUtils.truncate(cal.getTime(), Calendar.DATE);
        return DateUtils.addSeconds(truncate, -1);
    }

    public static Date getTodayStart() {
        Calendar cal = Calendar.getInstance();
        return DateUtils.truncate(cal.getTime(), Calendar.DATE);
    }

    public static Date nextDate(Date date) {
        return moveDate(date, 1);
    }

    public static Date prevDate(Date date) {
        return moveDate(date, -1);
    }

    public static Date moveDate(Date date, int d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, d);
        return cal.getTime();
    }

    /**
     * 获取时间的前后分钟
     *
     * @param date
     * @param m
     * @return
     */
    public static Date moveMinute(Date date, int m) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, m);
        return cal.getTime();
    }

    public static Date moveSecond(Date date, int s) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, s);
        return cal.getTime();
    }


    /**
     * 计算两个日期之间的日期差
     *
     * @param from
     * @param to
     * @param calendarType 计算类型
     * @return
     */
    public static Double daysBetween(Date from, Date to, int calendarType) {
        if (from == null || to == null) {
            return null;
        }
        long l = from.getTime() - to.getTime();
        switch (calendarType) {
            case Calendar.MILLISECOND:
                return (double) l;
            case Calendar.SECOND:
                return (double) l / 1000;
            case Calendar.MINUTE:
                return (double) l / 1000 / 60;
            case Calendar.HOUR:
                return (double) l / 1000 / 60 / 60;
            case Calendar.DAY_OF_MONTH:
                return (double) l / 1000 / 60 / 60 / 24;
            default:
        }
        return null;
    }

    /**
     * 获取当前时间往后推minute分钟
     */
    public static final Date getExpirTimeThirtyMinute(int minute) {
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.MINUTE, minute);
        return calender.getTime();
    }

    /**
     * yyyy-MM-dd
     */
    public static Date getToday() {
        return DateUtils.truncate(Calendar.getInstance(), Calendar.DATE).getTime();
    }

    public static Date getToday(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    /**
     * 功能描述：<br>
     * 转换为时间
     *
     * @param candidateDateStr 时间
     * @param format           时间格式
     * @return
     * @throws ParseException
     * @author huan.yan 2014-8-8 下午5:11:19 <br>
     */
    public static Date parse(String candidateDateStr, String format) throws ParseException {
        return getDateFormat(format).parse(candidateDateStr);
    }

    /**
     * 功能描述：<br>
     * 将long转换为Date
     *
     * @param longTime 时间
     * @param format   时间格式
     * @return
     * @throws ParseException
     */
    public static Date parseLong(long longTime, String format) throws ParseException {
        return DateUtil.parse(DateUtil.getDateFormat(format).format(longTime), format);
    }

    public static Date calendarParse(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date, hourOfDay, minute, second);
        return calendar.getTime();
    }

    public static SimpleDateFormat getDateFormat(String pattern) {
        LRUMap sdfMap = dateFormatThreadLocal.get();
        SimpleDateFormat sdf = (SimpleDateFormat) sdfMap.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            sdfMap.put(pattern, sdf);
        }
        return sdf;
    }

    /**
     * 功能描述：<br>
     *
     * @param date       时间
     * @param dataFormat 时间格式
     * @return
     * @author huan.yan 2014-6-6 下午12:53:41 <br>
     */
    public static String format(Date date, String dataFormat) {
        return getDateFormat(dataFormat).format(date);
    }

    public static String format(Date date) {
        return getDateFormat(YYYY_MM_dd_HHMMSS_NORMAL).format(date);
    }

    /**
     * 功能描述：<br>
     * 按日历类型进行增加
     *
     * @param date         需要处理的时间
     * @param calendarType 日期类型
     * @param count        数量
     * @return
     * @author huan.yan 2015-4-3 下午6:42:44 <br>
     */
    public static Date add(Date date, int calendarType, int count) {
        if (count == 0) {
            return date;
        }
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarType, count);
        return c.getTime();
    }

    private static final int MAX_SDF_SIZE = 20;
    private static final ThreadLocal<LRUMap> dateFormatThreadLocal = new ThreadLocal<LRUMap>() {
        @Override
        protected LRUMap initialValue() {
            return new LRUMap(MAX_SDF_SIZE);
        }
    };

    /**
     * 获得当天零时零分零秒
     *
     * @return
     */
    public static Date initDateByDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 00);
        return calendar.getTime();
    }


    public static int differentDaysByMillisecond(String beginDate, String endDate) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_dd);
            Date date1 = df.parse(beginDate);
            Date date2 = df.parse(endDate);
            int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
            return days;
        } catch (Exception e) {
            return 0;
        }
    }

    public static List<Integer> splitOneDay(String beginDate, String endDate) {
        if (StringUtils.isEmpty(beginDate) || StringUtils.isEmpty(endDate)) {
            return null;
        }
        try {
            List<Integer> dateList = Lists.newArrayList();
            SimpleDateFormat df = new SimpleDateFormat(DateUtil.YYYY_MM_dd);
            SimpleDateFormat df1 = new SimpleDateFormat(DateUtil.YYYYMMdd);
            int days = DateUtil.differentDaysByMillisecond(beginDate, endDate);
            if (days < 0) {
                return null;
            }
            Date date = df.parse(beginDate);
            for (int i = 0; i <= days; i++) {
                Date middle = DateUtils.addDays(date, i);
                dateList.add(Integer.valueOf(df1.format(middle)));
            }
            return dateList;
        } catch (Exception e) {
            return null;
        }
    }


    public static Date parseNoException(String date, String pattern) {
        try {
            return DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {

        }
        return null;
    }

    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.valueOf(timestamp);
    }
}
