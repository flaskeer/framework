package cc.hao.server;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Cookie {

	private static final long LONG_TIME = 365 * 24 * 60 * 60 * 1000L;
	private static final String GMT = "EEE,d MMM yyyy hh:mm:ss z";
	
	private String name;
	private String value;
	private Date expires;
	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
		long now = System.currentTimeMillis();
		expires = new Date(now + LONG_TIME);
	}
	
	public Cookie(String name, String value, Date expires) {
		this.name = name;
		this.value = value;
		this.expires = expires;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	@Override
	public String toString() {
		return "Set-Cookie: " + name + "=" + value 
				+ "; Path=/; Expires=" + toGMT(expires);
	}

	private static String toGMT(Date date) {
		Locale locale = Locale.ENGLISH;
		DateFormatSymbols symbols = new DateFormatSymbols(locale);
		DateFormat fmt = new SimpleDateFormat(GMT,symbols);
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return fmt.format(date);
	}
	
	public static String get(List<Cookie> cookies,String name){
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals(name)){
				return cookie.getValue();
			}
		}
		return "";
	}
	
	
	
}
