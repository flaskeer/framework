package cc.hao.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import cc.hao.core.Entity;
import cc.hao.core.Text;
import cc.hao.util.FileUtil;
import cc.hao.util.LoadUtil;

public class Meta {

	private static final String META_FILE = "data/meta";
	private static AtomicLong idCounter = new AtomicLong(1);
	private static Map<Long, Meta> metaMap = new ConcurrentHashMap<>();
	
	private long position;
	private int size;
	private String className;
	
	private Meta(String line){
		String[] parts = line.split(",");
		position = Long.parseLong(parts[0]);
		size = Integer.parseInt(parts[1]);
		className = parts[2];
		
	}
	
	public Meta(long position, int size, String className) {
		this.position = position;
		this.size = size;
		this.className = className;
	}
	
	public long getPosition() {
		return position;
	}
	
	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "Meta [position=" + position + ", size=" + size + ", className=" + className + "]";
	}
	
	public static long generateId(){
		return idCounter.incrementAndGet();
	}
	
	public static boolean metaFileExists(){
		return new File(META_FILE).exists();
	}
	
	public static void createFile() throws Exception{
		new File(META_FILE).createNewFile();
	}
	
	public static Meta get(long id){
		return metaMap.get(id);
	}
	
	public static Entity getEntity(){
		return null;
		
	}
	
	public static void saveMeta(Entity entity,long position,int size) throws Exception{
		Long id = entity.getId();
		String className = entity.getClass().getSimpleName();
		Meta meta = new Meta(position, size, className);
		metaMap.put(id, meta);
		writeMeta(id + "," + meta);
	}

	public static void writeMeta(String content) throws Exception {
		PrintWriter pw = FileUtil.writer(content);
		pw.println(content);
		pw.close();
	}
	
	public static Class<?> getClazz(long id) throws ClassNotFoundException{
		return metaMap.get(id).getClazz();
	}
	
	public static void delete(long id) throws Exception{
		writeMeta(String.valueOf(id));
		metaMap.remove(id);
	}

	public static void readMeta() throws IOException{
		long max = 1;
		BufferedReader reader = FileUtil.reader(META_FILE);
		String line;
		while((line = reader.readLine()) != null){
			String[] parts = line.split(",",2);
			long id = Long.parseLong(parts[0]);
			max = Math.max(max,id);
			if(parts.length == 1){
				metaMap.remove(id);
			}else{
				metaMap.put(id, new Meta(parts[1]));
			}
		}
		idCounter.set(max);
	}
	
	private Class<?> getClazz() throws ClassNotFoundException {
		return className.equals("Text") ? Text.class : LoadUtil.load(className);
	}
}
