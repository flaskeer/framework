package cc.hao.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringKit {
	
	private static final Map<Character, String> HTML_MAP;
	
	static{
		HTML_MAP = new HashMap<>();
		HTML_MAP.put('\n', "<br>");
		HTML_MAP.put(' ', "&nbsp;");
		HTML_MAP.put('"', "&quot;");
		HTML_MAP.put('<', "&lt;");
		HTML_MAP.put('>', "&gt;");
		HTML_MAP.put('&', "&amp;");
	}
	
	public static String toHtml(String text){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			String replace = HTML_MAP.get(c);
			sb.append(replace == null ? c:replace);
		}
		return sb.toString();
	}
	
	public static String upperFirst(String input){
		return input.substring(0,1).toUpperCase() + input.substring(1);
	}
	public static String lowerFirst(String input){
		return input.substring(0,1).toLowerCase() + input.substring(1);
	}

	public static boolean isEmpty(Object s){
		return s == null || s.equals("");
	}
	
	public static List<String> split(String text,char sperator){
		List<String> result = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(c == sperator){
				result.add(sb.toString());
				sb = new StringBuilder();
			}else{
				sb.append((char)c);
			}
		}
		result.add(sb.toString());
		return result;
	}
}
