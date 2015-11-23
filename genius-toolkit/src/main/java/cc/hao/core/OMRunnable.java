package cc.hao.core;

public interface OMRunnable {

	public void run() throws Exception;
	
	public static class Empty implements OMRunnable{
		@Override
		public void run() throws Exception {
		}
	}
	
}
