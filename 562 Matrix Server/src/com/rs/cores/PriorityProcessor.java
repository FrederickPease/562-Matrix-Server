package com.rs.cores;

public abstract class PriorityProcessor extends Thread {

	protected PriorityProcessor(int priority) {
		setPriority(priority);
	}

	@Override
	public final void run() {
		while(!CoresManager.shutdown) {
			long sleepTime = process();
			if(sleepTime > 0)
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}

	public abstract long process();

}
