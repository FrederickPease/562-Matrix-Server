package com.rs.cores;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class CoresManager {

	protected static boolean shutdown;
	public static WorldProcessor worldProcessor;
	public static ScheduledExecutorService serverChannelExecutor;
	public static ScheduledExecutorService fastExecutor;
	public static int serverChannelExecutorThreadsCount;
	public static Timer fastExecutor1;
	public static ScheduledExecutorService slowExecutor;
	public static final void init() {
		worldProcessor = new WorldProcessor(Thread.MAX_PRIORITY);
		if(Runtime.getRuntime().availableProcessors() >= 6) { //7
			serverChannelExecutorThreadsCount = Runtime.getRuntime().availableProcessors()- 4;//5;
			//fastExecutor = Executors.newScheduledThreadPool(2);
			fastExecutor1 = new Timer("Fast Executor");
			fastExecutor = Executors.newSingleThreadScheduledExecutor(); //for now we dont need more than 1 vcore for this
			slowExecutor = Executors.newScheduledThreadPool(2);
		}else {
			serverChannelExecutorThreadsCount = 2;
			fastExecutor1 = new Timer("Fast Executor");
			fastExecutor = Executors.newSingleThreadScheduledExecutor();
			slowExecutor = Executors.newSingleThreadScheduledExecutor();
		}
		serverChannelExecutor = Executors.newScheduledThreadPool(serverChannelExecutorThreadsCount);
		worldProcessor.start();
	}
	
	
	public static final void shutdown() {
		shutdown = true;
		serverChannelExecutor.shutdown();
		fastExecutor.shutdown();
		slowExecutor.shutdown();
	}
	
	private CoresManager() {
		
	}
}
