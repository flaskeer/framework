package cc.hao.server;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.hao.util.StringKit;

public class CookieUtil {

	private static Pattern pattern = Pattern.compile("cookie: (.+)");

	public static List<Cookie> cookies(String head){
		Matcher matcher = pattern.matcher(head);
		List<Cookie> cookies = new ArrayList<>();
		while(matcher.find()){
			for(String pair :matcher.group(1).split("; ")){
				List<String> parts = StringKit.split(pair, '=');
				if(!parts.get(0).equals("session")){
					cookies.add(new Cookie(parts.get(0), parts.get(1)));
				}
			}
		}
		return cookies;
	}
	
}
