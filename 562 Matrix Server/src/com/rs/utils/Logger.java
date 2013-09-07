package com.rs.utils;

import java.util.Calendar;
import java.util.Date;

import com.rs.Launcher;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.ServerChannelHandler;

public final class Logger {

	public static void handle(Throwable throwable) {
		System.out.println("ERROR! THREAD NAME: "
				+ Thread.currentThread().getName());
		throwable.printStackTrace();
	}

	public static void debug(long processTime) {
		log(Logger.class, "---DEBUG--- start");
		log(Logger.class, "WorldProcessTime: " + processTime);
		log(Logger.class,
				"WorldRunningTasks: " + WorldTasksManager.getTasksCount());
		log(Logger.class,
				"ConnectedChannels: "
						+ ServerChannelHandler.getConnectedChannelsSize());
		log(Logger.class, "---DEBUG--- end");
	}

	public static void log(Object classInstance, Object message) {
		log(classInstance.getClass().getSimpleName(), message);
	}

	public static void log(String className, Object message) {
		Date time = Calendar.getInstance().getTime();
		String text = "[" + time.toString() + "]" + " " + message.toString();
		System.err.println(text);
//		addPanelText(text);
	}
	
	public static void log(Object message) {
		Date time = Calendar.getInstance().getTime();
		String text = "[" + time.toString() + "]" + " " + message.toString();
		System.err.println(text);
//		addPanelText(text);
	}
	
	public static void addPanelText(String text) {
		Launcher.guiPanel.addText(text);
	}

	private Logger() {

	}

}
