package net.gittab.githubtravis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * DateUtil.
 *
 * @author xiaohua zhou
 **/
public class DateUtil {

	private static ThreadLocal<SimpleDateFormat> SHORT_SDF_LOCAL = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

	private static ThreadLocal<SimpleDateFormat> SHORT_SDF_LOCAL1 = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy/MM/dd"));

	private static ThreadLocal<SimpleDateFormat> LONG_SDF_LOCAL = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	/**
	 * 获取指定时间当天的开始时间字符串，精确到秒.
	 * @param date date
	 * @return date str
	 */
	public static String formatToLongDateStr(Date date) {
		SimpleDateFormat dateFormat = LONG_SDF_LOCAL.get();
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return dateFormat.format(date);
	}

	/**
	 * 获取指定时间当天的开始时间字符串，精确到秒.
	 * @param date date
	 * @return date str
	 */
	public static String getDayStartStr(Date date) {
		String dateStr = SHORT_SDF_LOCAL.get().format(date);
		return dateStr + " 00:00:00";
	}

	/**
	 * 获取指定时间当天的结束时间字符串，精确到秒.
	 * @param date date
	 * @return date str
	 */
	public static String getDayEndStr(Date date) {
		String dateStr = SHORT_SDF_LOCAL.get().format(date);
		return dateStr + " 23:59:59";
	}

	/**
	 * 获取当天的开始时间，精确到秒.
	 * @return date
	 */
	public static Date getDayStart() {
		Date date = null;
		try {
			String dateStr = SHORT_SDF_LOCAL.get().format(new Date());
			date = LONG_SDF_LOCAL.get().parse(dateStr + " 00:00:00");
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取当天的结束时间，精确到秒.
	 * @return date
	 */
	public static Date getDayEnd() {
		Date date = null;
		try {
			String dateStr = SHORT_SDF_LOCAL.get().format(new Date());
			date = LONG_SDF_LOCAL.get().parse(dateStr + " 23:59:59");
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 计算到当前时间的天数.
	 * @param date date
	 * @return days
	 */
	public static long calDaysUntilNow(Date date) {
		if (Objects.isNull(date)) {
			return 0;
		}
		int days = Days.daysBetween(DateTime.now().withTimeAtStartOfDay(),
				new DateTime(date).withTimeAtStartOfDay()).getDays();
		return days < 0 ? 0 : days;
	}

	public static String getCurrentDateStr() {
		return SHORT_SDF_LOCAL1.get().format(new Date());
	}

	public static void main(String[] args) {
		System.out.println(calDaysUntilNow(new DateTime().plusDays(4).toDate()));
	}

}
