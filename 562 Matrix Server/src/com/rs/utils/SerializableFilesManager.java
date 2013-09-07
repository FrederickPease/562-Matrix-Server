package com.rs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.rs.game.player.Player;

public class SerializableFilesManager {
	
	public synchronized static final boolean containsPlayer(String username) {
		return new File("data/characters/" + username + ".p").exists();
	}
	
	public synchronized static Player loadPlayer(String username) {
		try {
			return (Player) loadSerializedFile(new File("data/characters/" + username + ".p"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized static void savePlayer(Player player) {
		try {
			storeSerializableClass(player, new File("data/characters/" + player.getUsername() + ".p"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static final Object loadSerializedFile(File f) throws IOException, ClassNotFoundException {
    	if(!f.exists())
    		return null;
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
        Object object = in.readObject();
        in.close();
        return object;
    }
    
    public static final void storeSerializableClass(Serializable o, File f) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
        out.writeObject(o);
        out.close();
    }
	
	private SerializableFilesManager() {
		
	}
	
}
