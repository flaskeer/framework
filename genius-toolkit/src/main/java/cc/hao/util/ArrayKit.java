package cc.hao.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ArrayKit {

	public static boolean contains(Object[] array,Object obj){
		for (Object object : array) {
			if(object.equals(obj)){
				return true;
			}
		}
		return false;
	}
	
	public static String buildString(List<Byte> lists){
		return new String(toArray(lists));
	}
	
	public static byte[] toArray(List<Byte> lists){
		byte[] bytes = new byte[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			bytes[i] = lists.get(i);
		}
		return bytes;
	}
	
	public static <T> String toString(Collection<T> collection){
		StringBuilder sb = new StringBuilder();
		Iterator<T> it = collection.iterator();
		while(it.hasNext()){
			sb.append(it.next().toString());
			if(it.hasNext()){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	public static <T> List<T> toList(Collection<T> collection,Comparator<T> comparator){
		List<T> list = new ArrayList<>();
		list.addAll(collection);
		Collections.sort(list, comparator);
		return list;
	}
	
	public static <T> List<T> toList(Collection<T> collection,final Comparator<T> comparator,
			                          int pageIndex,int pageSize){
		int length = collection.size();
		int start = Math.min(length, (pageIndex - 1) * pageSize);
		int end = Math.min(length, pageIndex * pageSize);
		return toList(collection, comparator).subList(start, end);
	}
	
}
