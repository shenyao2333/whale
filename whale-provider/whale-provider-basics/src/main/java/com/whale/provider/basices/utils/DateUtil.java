package com.whale.provider.basices.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {

	private static final long ONE_MILLIS = 1000;
	private static final long ONE_MINUTE = 60;
	private static final long ONE_HOUR = 3600;
	private static final long ONE_DAY = 86400;
	private static final long ONE_MONTH = 2592000;
	private static final long ONE_YEAR = 31104000;





	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String formatDate() {
		return formatDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, String pattern) {
		String formatDate = null;
		if (pattern != null) {
			formatDate = DateFormatUtils.format(date, pattern);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}




    /**
     * 获取 本月 第一天
     * @return
     */
    public static  String monthFirst(String pattern){
        Calendar   cal_1=Calendar.getInstance();
        cal_1.add(Calendar.MONTH, 0);
        cal_1.set(Calendar.DAY_OF_MONTH,1);
        Date dateFrom = cal_1.getTime();
        return formatDate(dateFrom, pattern);
    }


    /**
     * 获取 本月 最后一天
     * @return
     */
    public static  String monthEnd(String pattern){

        return formatDate(new Date(), pattern);

    }

    /**
     * 得到某一天的该星期的第一日 00:00:00
     *
     * @param date
     * @param firstDayOfWeek
     *            一个星期的第一天为星期几
     *
     * @return
     */
    public static Date getFirstDayOfWeek(Date date, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        if(date != null){
            cal.setTime(date);
        }
        cal.setFirstDayOfWeek(firstDayOfWeek);//设置一星期的第一天是哪一天
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);//指示一个星期中的某天
        cal.set(Calendar.HOUR_OF_DAY, 0);//指示一天中的小时。HOUR_OF_DAY 用于 24 小时制时钟。例如，在 10:04:15.250 PM 这一时刻，HOUR_OF_DAY 为 22。
        cal.set(Calendar.MINUTE, 0);//指示一小时中的分钟。例如，在 10:04:15.250 PM 这一时刻，MINUTE 为 4。
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

	/**
	 * 获取昨天
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getLastDay(Date date, String pattern) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		// 一天
		cl.add(Calendar.DAY_OF_YEAR, -1);
		Date dateFrom = cl.getTime();
		return formatDate(dateFrom, pattern);
	}

	/**
	 * 获取昨天
	 *
	 * @param date
	 * @param
	 * @return
	 */
	public static Date getLastDay(Date date ) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		// 一天
		cl.add(Calendar.DAY_OF_YEAR, -1);
		return cl.getTime();

	}



    /**
     * 获取前后几天时间
     *
     * @param date  时间
     * @param dayNum  负数为几天前，否则 为 几天后
     * @param pattern 返回格式
     * @return
     */
    public static String getDateByDayNum(Date date,int dayNum, String pattern) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        // 几天
        cl.add(Calendar.DAY_OF_YEAR, dayNum);
        Date dateFrom = cl.getTime();
        return formatDate(dateFrom, pattern);
    }

	/**
	 * 获取前后几天时间
	 *
	 * @param date  时间
	 * @param dayNum  负数为几天前，否则 为 几天后
	 * @return
	 */
	public static Date getDateByDayNum(Date date,int dayNum) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		// 几天
		cl.add(Calendar.DAY_OF_YEAR, dayNum);
		return cl.getTime();
	}

	/**
	 * 获取上个月
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getLastMouth(Date date, String pattern) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		// 一个月
		cl.add(Calendar.MONTH, -1);
		Date dateFrom = cl.getTime();
		return formatDate(dateFrom, pattern);
	}

	/**
	 * 获取上一年
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getLastYear(Date date, String pattern) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		// 一年
		cl.add(Calendar.YEAR, -1);
		Date dateFrom = cl.getTime();
		return formatDate(dateFrom, pattern);
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 获取过去的天数
	 *
	 * @param date
	 * @return
	 */
	public static long getAgoDays(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (ONE_DAY * ONE_MILLIS);
	}

	/**
	 * 获取过去的小时
	 *
	 * @param date
	 * @return
	 */
	public static long getAgoHour(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (ONE_HOUR * ONE_MILLIS);
	}



	/**
	 * 获取过去的分钟
	 *
	 * @param date
	 * @return
	 */
	public static long getAgoMinutes(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (ONE_MINUTE * ONE_MILLIS);
	}


	/**
	 * Java获取两个日期之间的所有日期集合
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDayList(String startTime, String endTime) {

		// 返回的日期集合
		List<String> days = new ArrayList<String>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);
			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(dateFormat.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;
	}

	/**
	 * 获取两个日期之间的天数
	 *
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (ONE_MILLIS * ONE_DAY);
	}

    /**
     * 得到指定日期的一天的开始时刻00:00:00
     *
     * @param date
     * @return
     */
    public static Date getStartDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String temp = format.format(date);
        temp += " 00:00:00";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        try {
            return format1.parse(temp);
        } catch (Exception e) {
            return null;
        }
    }

	/**
	 * 得到指定日期的一天的开始时刻00:00:00
	 *
	 * @param date
	 * @return
	 */
	public static Date getStartDate(Date date,String pttern) {
		SimpleDateFormat format = new SimpleDateFormat(pttern);
		String temp = format.format(date);
		temp += " 00:00:00";
		SimpleDateFormat format1 = new SimpleDateFormat(pttern+" HH:mm:ss");
		try {
			return format1.parse(temp);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 得到指定日期的一天的结束时刻23:59:59
	 *
	 * @param date
	 * @return
	 */
	public static Date getEndDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String temp = format.format(date);
		temp += " 23:59:59";
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format1.parse(temp);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 得到指定日期的一天的结束时刻23:59:59
	 *
	 * @param date
	 * @return
	 */
	public static Date getEndDate(Date date,String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String temp = format.format(date);
		temp += " 23:59:59";
		SimpleDateFormat format1 = new SimpleDateFormat(pattern+" HH:mm:ss");
		try {
			return format1.parse(temp);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 距离今天多久
	 *
	 * @param date
	 * @return
	 */
	public static String formatFromToday(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			long time = date.getTime() / ONE_MILLIS;
			long now = System.currentTimeMillis() / ONE_MILLIS;
			long ago = now - time;
			if (ago <= ONE_HOUR) {
				return ago / ONE_MINUTE + "分钟前";
			} else if (ago <= ONE_DAY) {
				return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟前";
			} else if (ago <= ONE_DAY * 2) {
				return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
			} else if (ago <= ONE_DAY * 3) {
				return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
			} else if (ago <= ONE_MONTH) {
				long day = ago / ONE_DAY;
				return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
			} else if (ago <= ONE_YEAR) {
				long month = ago / ONE_MONTH;
				long day = ago % ONE_MONTH / ONE_DAY;
				return month + "个月" + day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
						+ calendar.get(Calendar.MINUTE) + "分";
			} else {
				long year = ago / ONE_YEAR;
				// JANUARY which is 0 so month+1
				int month = calendar.get(Calendar.MONTH) + 1;
				return year + "年前" + month + "月" + calendar.get(Calendar.DATE) + "日";
			}

		} else {
			return "";
		}

	}

	/**
	 * 两个时间距离多久
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String formatFromBeginAndEnd(Date begin,Date end) {
		if (begin != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(begin);

			long time = begin.getTime() / ONE_MILLIS;
			long now = end.getTime() / ONE_MILLIS;
			long ago = now - time;
			if (ago < ONE_MINUTE) {
				return ago+"秒";
			}else if (ago <= ONE_HOUR) {
				return ago / ONE_MINUTE + "分钟";
			} else if (ago <= ONE_DAY) {
				return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
			}else if (ago <= ONE_MONTH) {
				long day = ago / ONE_DAY;
				return day + "天" + calendar.get(Calendar.HOUR_OF_DAY) + "小时" + calendar.get(Calendar.MINUTE) + "分";
			} else if (ago <= ONE_YEAR) {
				long month = ago / ONE_MONTH;
				long day = ago % ONE_MONTH / ONE_DAY;
				return month + "个月" + day + "天" + calendar.get(Calendar.HOUR_OF_DAY) + "小时"
						+ calendar.get(Calendar.MINUTE) + "分";
			} else {
				long year = ago / ONE_YEAR;
				// JANUARY which is 0 so month+1
				int month = calendar.get(Calendar.MONTH) + 1;
				return year + "年" + month + "月" + calendar.get(Calendar.DATE) + "日";
			}

		} else {
			return "";
		}

	}

	/**
	 * 距离今天多久
	 *
	 * @param createAt
	 * @return
	 */
	public static String formatFromTodayCn(Date createAt) {
		// 定义最终返回的结果字符串。
		String interval = null;
		if (createAt == null) {
			return "";
		}
		long millisecond = System.currentTimeMillis() - createAt.getTime();

		long second = millisecond / ONE_MILLIS;

		if (second <= 0) {
			second = 0;
		}
		// *--------------微博体（标准）
		if (second == 0) {
			interval = "刚刚";
		} else if (second < ONE_MINUTE / 2) {
			interval = second + "秒以前";
		} else if (second >= ONE_MINUTE / 2 && second < ONE_MINUTE) {
			interval = "半分钟前";
		} else if (second >= ONE_MINUTE && second < ONE_MINUTE * ONE_MINUTE) {
			// 大于1分钟 小于1小时
			long minute = second / ONE_MINUTE;
			interval = minute + "分钟前";
		} else if (second >= ONE_HOUR && second < ONE_DAY) {
			// 大于1小时 小于24小时
			long hour = (second / ONE_MINUTE) / ONE_MINUTE;
			interval = hour + "小时前";
		} else if (second >= ONE_DAY && second <= ONE_DAY * 2) {
			// 大于1D 小于2D
			interval = "昨天" + formatDate(createAt, "HH:mm");
		} else if (second >= ONE_DAY * 2 && second <= ONE_DAY * 7) {
			// 大于2D小时 小于 7天
			long day = ((second / ONE_MINUTE) / ONE_MINUTE) / 24;
			interval = day + "天前";
		} else if (second <= ONE_DAY * 365 && second >= ONE_DAY * 7) {
			// 大于7天小于365天
			interval = formatDate(createAt, "MM-dd HH:mm");
		} else if (second >= ONE_DAY * 365) {
			// 大于365天
			interval = formatDate(createAt, "yyyy-MM-dd HH:mm");
		} else {
			interval = "0";
		}
		return interval;
	}

	/**
	 * 距离截止日期还有多长时间
	 *
	 * @param date
	 * @return
	 */
	public static String formatFromDeadline(Date date) {
		long deadline = date.getTime() / ONE_MILLIS;
		long now = (System.currentTimeMillis()) / ONE_MILLIS;
		long remain = deadline - now;
		if (remain <= ONE_HOUR) {
			return "只剩下" + remain / ONE_MINUTE + "分钟";
		} else if (remain <= ONE_DAY) {
			return "只剩下" + remain / ONE_HOUR + "小时" + (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
		} else {
			long day = remain / ONE_DAY;
			long hour = remain % ONE_DAY / ONE_HOUR;
			long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
			return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
		}

	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 *
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (ONE_DAY * ONE_MILLIS);
		long hour = (timeMillis / (ONE_HOUR * ONE_MILLIS) - day * 24);
		long min = ((timeMillis / (ONE_MINUTE * ONE_MILLIS)) - day * 24 * ONE_MINUTE - hour * ONE_MINUTE);
		long s = (timeMillis / ONE_MILLIS - day * 24 * ONE_MINUTE * ONE_MINUTE - hour * ONE_MINUTE * ONE_MINUTE
				- min * ONE_MINUTE);
		long sss = (timeMillis - day * 24 * ONE_MINUTE * ONE_MINUTE * ONE_MILLIS
				- hour * ONE_MINUTE * ONE_MINUTE * ONE_MILLIS - min * ONE_MINUTE * ONE_MILLIS - s * ONE_MILLIS);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}



	/**
	 * 日期格式字符串转换成Unix时间戳
	 *
	 * @param dateStr 字符串日期
	 * @param format  如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2UnixTimeStamp(String dateStr, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(dateStr).getTime() / ONE_MINUTE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 毫秒转化时分秒毫秒 10000 - 10秒
	 *
	 * @param ms
	 * @return
	 */
	public static String formatTime(Long ms) {
		long ss = ONE_MINUTE;
		long mi = ss * ONE_MINUTE;
		long hh = mi * ONE_MINUTE;
		long dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		StringBuffer sb = new StringBuffer();
		if (day > 0) {
			sb.append(day + "天");
		}
		if (hour > 0) {
			sb.append(hour + "小时");
		}
		if (minute > 0) {
			sb.append(minute + "分钟");
		}
		if (second > 0) {
			sb.append(second + "秒");
		}
		return sb.toString();
	}

	public static long dateToLong(String strDate) {
		Date date = parseDate(strDate);

		return date.getTime() / 1000;
	}

	public static String transferLongToDate(String dateFormat, Long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millSec);
		return sdf.format(date);
	}

	public static String dateToString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		return sdf.format(new Date());
	}

	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		return sdf.format(date);
	}

	public static void main(String[] args) {
		try {
//			System.out.println(getLastDay(new Date(), "yyyy-MM-dd"));
//			System.out.println(getLastWeek(new Date(), "yyyy-MM-dd"));
//			System.out.println(getLastMouth(new Date(), "yyyy-MM-dd"));
//			System.out.println(formatTime(10000L));
			// 1396506300000
			// 1452211200
			// 1452211200000

			/*Long date = System.currentTimeMillis();
			Long adate = getAgoMinutes10(date);
			System.out.println(adate);
			System.out.println(transferLongToDate("yyyy-MM-dd HH:mm:ss", adate));*/
			long dateToLong = dateToLong("2019-07-5 09:00");
			System.out.println(dateToLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 计算两个日期之间相差的天数
	 * @param sdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws Exception
	 * calendar 对日期进行时间操作
	 * getTimeInMillis() 获取日期的毫秒显示形式
	 */
	public static int daysBetween(Date sdate,Date bdate) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days));
	}




	/**
	 * 比较两个日期的时间差 毫秒数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long diffDate(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}


	/**
	 * 查看是否大于当前时间
	 * @param time
	 * @return
	 */
	public static boolean checkIsGt(Date time){
		return time.getTime() > System.currentTimeMillis();
	}





	public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();

		min.setTime(sdf.parse(minDate));
		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

		max.setTime(sdf.parse(maxDate));
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

		Calendar curr = min;
		while (curr.before(max)) {
			result.add(sdf.format(curr.getTime()));
			curr.add(Calendar.MONTH, 1);
		}

		return result;
	}

    /**
     * 获取两个日期之间的所有日期
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }


}
