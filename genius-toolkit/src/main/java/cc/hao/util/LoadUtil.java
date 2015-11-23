package cc.hao.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class LoadUtil {
	
	private static ClassLoader classLoader;
	
	public static void init() throws IOException{
		URL[] urls = new URL[]{new URL(Cfg.jar())};
		classLoader = new URLClassLoader(urls);
	}
	
	public static Class<?> load(String name) throws ClassNotFoundException{
		return classLoader.loadClass(Cfg.pkg() + "." + name);
	}

}
