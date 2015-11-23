package cc.hao.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map.Entry;

public class Cfg {
	
	private static String jar;
	private static String pkg;
	private static int port;
	private static int pool;
	private static int buffer;
	private static long timeout;
	private static long cache;
	private static String defaultEntity;
	private static String defaultAction;
	private static String loginEntity;
	private static String loginAction;
	private static String charset;
	private static String log;
	
	public static void init() throws Exception{
		for(Entry<String, String> pair: CfgUtil.load("Cfg").entrySet()){
			ReflectionUtil.setField(Cfg.class, pair.getKey(), pair.getValue());
		}
		timeout *= 60 * 1000;
		cache *= 60 * 1000;
		OutputStream os = new FileOutputStream(Cfg.log(),true);
		PrintStream out = new PrintStream(os);
		System.setErr(out);
		System.setOut(out);
		
	}

	public static String jar() {
		return jar;
	}

	public static String pkg() {
		return pkg;
	}

	public static int port() {
		return port;
	}

	public static int pool() {
		return pool;
	}

	public static int buffer() {
		return buffer;
	}

	public static long timeout() {
		return timeout;
	}

	public static long cache() {
		return cache;
	}

	public static String defaultEntity() {
		return defaultEntity;
	}

	public static String defaultAction() {
		return defaultAction;
	}

	public static String loginEntity() {
		return loginEntity;
	}

	public static String loginAction() {
		return loginAction;
	}

	public static String charset() {
		return charset;
	}

	public static String log() {
		return log;
	}

	
	

}
