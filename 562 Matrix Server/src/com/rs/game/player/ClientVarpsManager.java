package com.rs.game.player;

import com.rs.cache.loaders.ClientVarpBitDefinitions;

public class ClientVarpsManager {


	private static final int[] MASK = new int[32];

	static {
		int i = 2;
		for (int i2 = 0; i2 < 32; i2++) {
			MASK[i2] = i - 1;
			i += i;
		}
	}
	
	
	private int[] values;
	private Player player;

	public ClientVarpsManager(Player player) {
		this.player = player;
		//values = new int[Cache.getCacheFileManagers()[2].getLastFileId(16)];
	}
	
	public void sendClientVarp(int id, int value) {
		sendClientVarp(id, value, false);
	}
	
	public void forceSClientVarp(int id, int value) {
		sendClientVarp(id, value, true);
	}
	
	private void sendClientVarp(int id, int value, boolean force) {
		if(id < 0 || id >= values.length) //temporarly
			return;
		if(force || values[id] == value)
			return;
		setClientVarp(id, value);
		sendClientVarp(id);
	}
	
	public void setClientVarp(int id, int value) {
		if(id == -1) //temporarly
			return;
		values[id] = value;
	}
	
	public void forceSendClientVarpBit(int id, int value) {
		setClientVarpBit(id, value, 0x1 | 0x2);
	}
	
	public void sendClientVarpBit(int id, int value) {
		setClientVarpBit(id, value, 0x1);
	}
	
	public void setClientVarpBit(int id, int value) {
		setClientVarpBit(id, value, 0);
	}
	
	private void setClientVarpBit(int id, int value, int flag) {
		if(id == -1) //temporarly
			return;
		ClientVarpBitDefinitions defs = ClientVarpBitDefinitions.getClientVarpBitDefinitions(id);
		int mask = MASK[defs.tillBitshift - defs.fromBitshift];
		if (value < 0 || value > mask)
			value = 0;
		mask <<= defs.fromBitshift;
		int varpValue = (values[defs.varpId] & (mask ^ 0xffffffff) | value << defs.fromBitshift & mask);
		if((flag & 0x2) != 0 || varpValue != values[defs.varpId]) {
			setClientVarp(defs.varpId, varpValue);
			if((flag & 0x1) != 0)
				sendClientVarp(defs.varpId);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void sendClientVarp(int id) {
		//player.getPackets().sendClientVarp(id, values[id]);
	}
}