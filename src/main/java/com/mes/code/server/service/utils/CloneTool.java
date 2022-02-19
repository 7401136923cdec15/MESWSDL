package com.mes.code.server.service.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;

/**
 * 工具类 可置于service中
 * 
 * @author ShrisJava
 *
 */
public class CloneTool {

	public CloneTool() {
		// TODO Auto-generated constructor stub
	}

	public static <T> T Clone(Object wObject, Class<T> clazz) {

		T wT = null;

		String wJson = JSON.toJSONString(wObject);

		wJson = StringUtils.RepleaceRegx(wJson,
				"(\\d{4}([-\\/])\\d{1,2}([-\\/])\\d{1,2}([T\\s])\\d{1,2}:\\d{1,2}:\\d{1,2})(\\.\\d{3}\\+\\d{4})?");

		wT = JSON.parseObject(wJson, clazz);
		return wT;
	}

	public static <T> List<T> CloneArray(Object wObject, Class<T> clazz) {

		List<T> wTList = new ArrayList<T>();

		String wJson = JSON.toJSONString(wObject);

		Matcher wMatcher = Pattern.compile(
				"(\\d{4}[-\\/]\\d{1,2}[-\\/]\\d{1,2}[T\\s]\\d{1,2}:\\d{1,2}:\\d{1,2})(\\.\\d{1,3}\\+\\d{1,4})?",
				Pattern.CASE_INSENSITIVE).matcher(wJson);
		StringBuffer sb = new StringBuffer();
		while (wMatcher.find()) {
			wMatcher.appendReplacement(sb, StringUtils
					.parseCalendarToString(StringUtils.parseCalendar(wMatcher.group(1)), "yyyy-MM-dd HH:mm:ss"));

		}
		wMatcher.appendTail(sb);

		wTList = JSON.parseArray(wJson, clazz);
		return wTList;
	}

	@SuppressWarnings("rawtypes")
	public static List<Map<String, Object>> CloneArray(Object wObject) {

		List<Map<String, Object>> wTList = new ArrayList<Map<String, Object>>();

		String wJson = JSON.toJSONString(wObject);
		List<Map> wTListTemp = JSON.parseArray(wJson, Map.class);
		for (Map map : wTListTemp) {
			Map<String, Object> wMap = new HashMap<String, Object>();
			for (Object wKey : map.keySet()) {
				wMap.put(wKey.toString(), map.get(wKey));
			}
			wTList.add(wMap);

		}

		return wTList;
	}

	public static <T> List<T> CloneArray(List<T> wObject, Class<T> clazz) {

		List<T> wTList = new ArrayList<T>();

		String wJson = JSON.toJSONString(wObject);
		wTList = JSON.parseArray(wJson, clazz);
		return wTList;
	}

}
