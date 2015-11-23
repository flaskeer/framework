package cc.hao.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {
	
	public static PrintWriter writer(String path) throws IOException{
		return new PrintWriter(new BufferedWriter(new FileWriter(path,true)));
	}

	public static BufferedReader reader(String path) throws IOException{
		return new BufferedReader(new FileReader(path));
	}
	
	public static String read(String path) throws Exception{
		return new String(bytes(path));
	}
	
	public static byte[] bytes(String path) throws Exception{
		@SuppressWarnings("resource")
		FileChannel fileChannel = new FileInputStream(path).getChannel();
		return new ChannelReader(fileChannel).read();
	}
	
	public static String read(String path,long position,int size) throws IOException{
		@SuppressWarnings("resource")
		FileChannel channel = new FileInputStream(path).getChannel();
		channel.position(position);
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		channel.read(byteBuffer);
		channel.close();
		return new String(byteBuffer.array());
	}
	
}
