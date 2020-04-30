package net.gittab.githubtravis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * process list set util.
 *
 * @author xiaohua zhou
 **/
public class ListUtil {

	/**
	 * list 分页.
	 * @param list list
	 * @param pageSize 每页大小
	 * @param <T> list object
	 * @return list object
	 */
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<>();
		}
		int length = list.size();
		// 计算可以分成多少页,这里减少了计算
		int page = (length + pageSize - 1) / pageSize;
		List<List<T>> newList = new ArrayList<>(page);
		for (int i = 0; i < page; i++) {
			// 开始位置
			int fromIndex = i * pageSize;
			// 结束位置
			int toIndex = (i + 1) * pageSize < length ? (i + 1) * pageSize : length;
			newList.add(list.subList(fromIndex, toIndex));
		}
		return newList;
	}

	/**
	 * str to list.
	 * @param commaStr 逗号分隔的字符串
	 * @return str list
	 */
	public static List<String> str2List(String commaStr) {
		if (StringUtils.isEmpty(commaStr)) {
			return new ArrayList<>(0);
		}
		return Arrays.asList(commaStr.split(","));
	}

	/**
	 * list to str.
	 * @param strList str list
	 * @return 逗号分隔的字符串
	 */
	public static String list2Str(List<String> strList) {
		if (CollectionUtils.isEmpty(strList)) {
			return "";
		}
		StringBuffer strBuffer = new StringBuffer();
		strList.forEach(item -> strBuffer.append(item).append(","));
		return strBuffer.substring(0, strBuffer.length() - 1);
	}

}
