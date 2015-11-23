package cc.hao.core;

public abstract class OMThread extends Thread{

	protected abstract void doRun() throws Exception;
	
	protected void handleException(Exception e){
		e.printStackTrace();
	}
	
	private void wrapRun(){
		try {
			doRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		wrapRun();
	}
}
