package vn.tech.website.store.util;

import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Log4j2
public class DateUtil {
    public static final String FROM_DATE_FORMAT = "dd/MM/yyyy 00:00:00";
    public static final String TO_DATE_FORMAT = "dd/MM/yyyy 23:59:59";
    public static final String FROM_DATE_FORMAT_WITH_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static final String TO_DATE_FORMAT_WITH_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String HHMMSS_DDMMYYYY = "HH:mm:ss dd/MM/yyyy";
    public static final String DATE_FORMAT_UPLOAD = "ddMMyyyyHHmmssSSS";
    public static final String DDMMYYYY = "dd/MM/yyyy";
    public static final String DAY_DD_MONTH_MM_YEAR_YYYY = "'Ngày' dd 'tháng' MM 'năm' yyyy";
    public static final String YYYYMMDD_FOLDER = "yyyyMMdd";
    public static final String MMDDYYYYHHMMSS = "MM-dd-yyyy-HH-mm-ss"; //08-16-2020-23-59-59
    public static final String DATE_FORMAT_SECOND_END = "dd/MM/yyyy HH:mm:59";
    public static final String DATE_FORMAT_MINUTE = "dd/MM/yyyy HH:mm:00";
    public static final String DATE_FORMAT_HOUR = "dd/MM/yyyy HH:00:00";
    public static final String HOUR_DATE_FORMAT = "HH:mm:ss dd/MM/yyyy";
    public static final SimpleDateFormat cmdateFormat = new SimpleDateFormat(DATE_FORMAT);
    public static String typeTime;

    public static Date formatFromDate(Date fromDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FROM_DATE_FORMAT);
        String strDate = dateFormat.format(fromDate);
        try {
            return cmdateFormat.parse(strDate);
        } catch (ParseException e) {
            log.error(e);
        }
        return fromDate;
    }

    public static Date formatToDate(Date toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TO_DATE_FORMAT);
        String strDate = dateFormat.format(toDate);
        try {
            return cmdateFormat.parse(strDate);
        } catch (ParseException e) {
            log.error(e);
        }
        return toDate;
    }

    public static String formatToPattern(Date date, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date formatDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String strDate = dateFormat.format(date);
        try {
            return cmdateFormat.parse(strDate);
        } catch (ParseException e) {

        }
        return date;
    }

    public static Date formatDatePattern(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDatePattern(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String strDate = sdf.format(date);
        try {
            return cmdateFormat.parse(strDate);
        } catch (ParseException e) {

        }
        return null;
    }

    public static String getCurrentDateStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_UPLOAD);
        return dateFormat.format(new Date());
    }

    public static String getTodayFolder() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYYMMDD_FOLDER);
        return dateFormat.format(new Date());
    }

    public static String setMinute(Date date, int minus){
        SimpleDateFormat dateFormat = new SimpleDateFormat(MMDDYYYYHHMMSS);
        Date temp = new Date();
        temp.setTime(date.getTime() + minus * 60 * 1000);
        return dateFormat.format(temp);
    }

    public static Date plusDay(Date date, int day) {
        return plusHour(date, day * 24);
    }

    public static Date minusDay(Date date, int day) {
        return minusHour(date, day * 24);
    }

    public static Date plusHour(Date date, int hour) {
        return plusMinute(date, hour * 60);
    }

    public static Date minusHour(Date date, int hour) {
        return minusMinute(date, hour * 60);
    }

    public static Date plusMinute(Date date, int minus) {
        return plusSecond(date, minus * 60);
    }

    public static Date minusMinute(Date date, int minus) {
        return minusSecond(date, minus * 60);
    }

    public static Date plusSecond(Date date, long second) {
        try {
            Date temp = new Date();
            temp.setTime(date.getTime() + second * 1000);
            return temp;
        } catch (Exception e) {
            return date;
        }
    }

    public static Date minusSecond(Date date, long second) {
        try {
            Date temp = new Date();
            temp.setTime(date.getTime() - second * 1000);
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    public static String buildStringPattern(Date time, String pattern) {
        try {
            return new SimpleDateFormat(pattern).format(time);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date buildTimePattern(Date time, String pattern) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(new SimpleDateFormat(pattern).format(time));
        } catch (Exception e) {
            return null;
        }
    }

    public static Long betweenDate(Date fromDate, Date toDate) {
        LocalDate fDate = toLocalDate(fromDate);
        LocalDate tDate = toLocalDate(toDate);
        Duration diff = Duration.between(fDate.atStartOfDay(), tDate.atStartOfDay());
        return diff.toDays();
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static long[] countdown(Date fromDate, Date toDate) {
        long diff = (toDate.getTime() - fromDate.getTime());
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(diff));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff));
        long milliseconds = TimeUnit.MILLISECONDS.toMillis(diff) - TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toSeconds(diff));

        return new long[]{
                Math.max(days, 0),
                Math.max(hours, 0),
                Math.max(minutes, 0),
                Math.max(seconds, 0),
                Math.max(milliseconds, 0)};
    }

    public static Date convertLocalDateToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long countTimeBefore(Date dateBefore) {
        Date nowDate = new Date();
        long diff = nowDate.getTime() - dateBefore.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

        if (days != 0) {
            typeTime = "ngày";
            return days;
        } else if (hours != 0) {
            typeTime = "giờ";
            return hours;
        } else if (minutes != 0) {
            typeTime = "phút";
            return minutes;
        } else {
            typeTime = "giây";
            return seconds;
        }
    }
}
