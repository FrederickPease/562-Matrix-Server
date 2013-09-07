package com.rs.cache.loaders;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.io.InputStream;
import com.rs.utils.Utils;

public final class InterfaceScript {

	@SuppressWarnings("unused")
	private char aChar6337;
	@SuppressWarnings("unused")
	private char aChar6345;
	private String defaultStringValue;
	private int defaultIntValue;
	private HashMap<Long, Object> values;
	
	private static final ConcurrentHashMap<Integer, InterfaceScript> interfaceScripts = new ConcurrentHashMap<Integer, InterfaceScript>();
	
	public static final InterfaceScript getInterfaceScript(int scriptId) {
		InterfaceScript script = interfaceScripts.get(scriptId);
		if(script != null)
			return script;
		byte[] data = Cache.getCacheFileManagers()[17].getFileData(scriptId >>> 0xba9ed5a8, scriptId & 0xff);
		script = new InterfaceScript();
		if(data != null)
			script.readValueLoop(new InputStream(data));
		interfaceScripts.put(scriptId, script);
		return script;
		
	}
	
	public Object getValue(long key) {
		if(values == null)
			return null;
		return values.get(key);
	}
	
	public int getSize() {
		if(values == null)
			return 0;
		return values.size();
	}
	
	public long getKeyForValue(Object value) {
		for(Long key : values.keySet()) {
			if(values.get(key).equals(value))
				return key;
		}
		return -1;
	}
	
	public int getIntValue(long key) {
		if(values == null)
			return defaultIntValue;
		Object value = values.get(key);
		if(value == null || !(value instanceof Integer))
			return defaultIntValue;
		return (Integer) value;
	}
	
	public String getStringValue(long key) {
		if(values == null)
			return defaultStringValue;
		Object value = values.get(key);
		if(value == null || !(value instanceof String))
			return defaultStringValue;
		return (String) value;
	}
	
    private void readValueLoop(InputStream stream) {
        for (; ;) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0)
                break;
            readValues(stream, opcode);
        }
    }
	
	private void readValues(InputStream stream, int opcode) {
		if(opcode == 1)
			aChar6337 = Utils.method2782((byte) stream.readByte());
		else if (opcode == 2)
			aChar6345 = Utils.method2782((byte) stream.readByte());
		else if (opcode == 3)
			defaultStringValue = stream.readString();
		else if (opcode == 4)
			defaultIntValue = stream.readInt();
		else if (opcode == 5 || opcode == 6) {
			int count = stream.readUnsignedShort();
			int loop = opcode == 7 || opcode == 8 ? stream.readUnsignedShort() : count;
			values = new HashMap<Long, Object>(Utils.getHashMapSize(count));
			for(int i = 0; i < loop; i++) {
				int key = stream.readInt();
				Object value = opcode == 5 || opcode == 7 ? stream.readString() : stream.readInt();
				values.put((long) key, value);
			}
		}
	}


	private InterfaceScript() {
		defaultStringValue = "null";
	}
}