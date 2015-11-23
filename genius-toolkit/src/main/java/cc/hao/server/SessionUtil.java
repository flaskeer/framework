package cc.hao.server;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.hao.core.OMThread;
import cc.hao.util.Cfg;

public class SessionUtil {
	
	private static Map<String, Session> sessions = new ConcurrentHashMap<>();
	private static Pattern pattern = Pattern.compile("session=(.+?);");
	
	public static void init(){
		new Killer().start();
	}
	
	public static Session getSession(String head){
		Session session = find(head);
		if(session == null){
			String uuid = UUID.randomUUID().toString();
			session = new Session(uuid);
			sessions.put(uuid, session);
		}else{
			session.touch();
		}
		return session;
	}
	
	public static Session find(String head){
		Matcher matcher = pattern.matcher(head + ";");
		while(matcher.find()){
			return sessions.get(matcher.group(1));
		}
		return null;
	}
	
	private static class Killer extends OMThread{

		@Override
		protected void doRun() throws Exception {
			Thread.sleep(Cfg.timeout());
			while(true){
				Thread.sleep(Cfg.timeout());
				kill();
			}
		}

		private void kill() {
			long now = new Date().getTime();
			Iterator<Session> iterator = sessions.values().iterator();
			while(iterator.hasNext()){
				checkAndKill(now,iterator);
			}
			
		}

		private void checkAndKill(long now, Iterator<Session> iterator) {
			Session session = iterator.next();
			while(now - session.touched() > Cfg.timeout()){
				iterator.remove();
			}
		}
		
	}

}
