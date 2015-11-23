package cc.hao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CfgUtil {
	
	public static Map<String,String> load(String fileName) throws IOException{
		Map<String,String> result = new HashMap<>();
		BufferedReader reader = FileUtil.reader(fileName + ".cfg");
		String line;
		while((line = reader.readLine()) != null){
			String[] pair = line.trim().split("=");
			result.put(pair[0], pair[1]);
		}
		return result;
	}

}


