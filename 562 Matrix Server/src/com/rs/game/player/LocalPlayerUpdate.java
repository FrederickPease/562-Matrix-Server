package com.rs.game.player;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.io.OutputStream;
import com.rs.utils.huffman.Huffman;



public final class LocalPlayerUpdate {

	private Player player;
	private Player[] localPlayers;
	private int localPlayersSize;
	private ArrayList<Integer> addedIndexes;
	private byte [][] cachedAppearencesHashes;
	private int totalRenderDataSentLength;
	
	public LocalPlayerUpdate(Player player) {
		this.player = player;
		addedIndexes = new ArrayList<Integer>();
		localPlayers = new Player[Settings.LOCAL_PLAYERS_LIMIT];
		cachedAppearencesHashes = new byte[Settings.PLAYERS_LIMIT][];
	}
	
	public OutputStream createPacketAndProcess() {
		totalRenderDataSentLength = 0;
		OutputStream stream = new OutputStream();
		OutputStream updateBlockData = new OutputStream();
		stream.writePacketVarShort(107);
		processLocalPlayersInform(stream, updateBlockData);
		stream.writeBytes(updateBlockData.getBuffer(), 0, updateBlockData.getOffset());
		stream.endPacketVarShort();
		return stream;
	}

	private void processLocalPlayersInform(OutputStream stream, OutputStream updateBlockData) {
		stream.initBitAccess();
		processThisPlayer(stream, updateBlockData);
		processInScreenPlayers(stream, updateBlockData);
		addInScreenPlayers(stream, updateBlockData);
		if(updateBlockData.getOffset() > 0)
			stream.writeBits(11, 2047);
		stream.finishBitAccess();
	}

	public boolean needAppearenceUpdate(int index, byte[] hash) {
		if(totalRenderDataSentLength > ((Settings.PACKET_SIZE_LIMIT - 500) /2) || hash == null)
			return false;
		return cachedAppearencesHashes[index] == null || !MessageDigest.isEqual(cachedAppearencesHashes[index], hash);
	}
	
	private void processThisPlayer(OutputStream stream, OutputStream updateBlockData) {
		boolean needAppearenceUpdate = needAppearenceUpdate(player.getIndex(), player.getAppearence().getMD5AppeareanceDataHash());
		boolean needUpdate = needAppearenceUpdate || player.needMasksUpdate();
		boolean movementUpdate = !player.getDontUpdateMyPlayer() && ((player.hasTeleported() || player.getNextWalkDirection() != -1));
		stream.writeBits(1, needUpdate ||  movementUpdate ? 1 : 0);
		if(movementUpdate) {
			stream.writeBits(2, player.hasTeleported() ? 3 : (player.getNextRunDirection() == -1 ? 1 : 2));
			if(player.hasTeleported()) {
				stream.writeBits(1, needUpdate ? 1 : 0);
				stream.writeBits(2, player.getPlane());
				stream.writeBits(1, 1);
				stream.writeBits(7, player.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()));
				stream.writeBits(7, player.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
			}else{
				if(player.getNextRunDirection() != -1)
					stream.writeBits(1, 1);
				stream.writeBits(3, player.getNextWalkDirection());
				if(player.getNextRunDirection() != -1)
					stream.writeBits(3, player.getNextRunDirection());
				stream.writeBits(1, needUpdate ? 1 : 0);
			}
		}else if(needUpdate)
			stream.writeBits(2, 0);
		if(needUpdate)
			appendUpdateBlock(player, updateBlockData, needAppearenceUpdate, false);
	}
	private void processInScreenPlayers(OutputStream stream, OutputStream updateBlockData) {
		int size = localPlayersSize;
		localPlayersSize = 0;
		stream.writeBits(8, size);
		for (int index = 0; index < size; index++) {
			Player p = localPlayers[index];
			if(p == null || p.hasFinished() || !player.withinDistance(p) || p.hasTeleported()) { //teleported :p
				stream.writeBits(1, 1);
				stream.writeBits(2, 3);
				if (p != null)
					addedIndexes.remove((Object) p.getIndex());
				continue;
			}
			localPlayers[localPlayersSize++] = p;
			boolean needAppearenceUpdate = needAppearenceUpdate(p.getIndex(), p.getAppearence().getMD5AppeareanceDataHash());
			boolean needUpdate = (updateBlockData.getOffset()+stream.getOffset() < (Settings.PACKET_SIZE_LIMIT - 500))  && (needAppearenceUpdate || p.needMasksUpdate());
			boolean walkUpdate = p.getNextWalkDirection() != -1;
			stream.writeBits(1, (needUpdate ||  walkUpdate) ? 1 : 0);
			if(walkUpdate) {
				stream.writeBits(2, p.getNextRunDirection() == -1 ? 1 : 2);
				if(p.getNextRunDirection() != -1)
					stream.writeBits(1, 1);
				stream.writeBits(3, p.getNextWalkDirection());
				if(p.getNextRunDirection() != -1)
					stream.writeBits(3, p.getNextRunDirection());
				stream.writeBits(1, needUpdate ? 1 : 0);
			}else if(needUpdate)
				stream.writeBits(2, 0);
			if(needUpdate)
				appendUpdateBlock(p, updateBlockData, needAppearenceUpdate, false);
		}
	}
	
	private void addInScreenPlayers(OutputStream stream, OutputStream updateBlockData) {
		int addedPlayersCount = 0;
		for(int regionId : player.getMapRegionsIds()) {
			CopyOnWriteArrayList<Integer> indexes = World.getRegion(regionId).getPlayerIndexes();
			if(indexes == null)
				continue;
			for(int playerIndex :  indexes) {
				if (localPlayersSize > (localPlayers.length-1) || addedPlayersCount > 125 || (updateBlockData.getOffset()+stream.getOffset() > (Settings.PACKET_SIZE_LIMIT-500) ))
					break;
				Player p = World.getPlayers().get(playerIndex);
				if (p == null || p == player || !p.hasStarted() || !p.isRunning() || p.hasFinished() || addedIndexes.contains(p.getIndex()) || !player.withinDistance(p) || (p.hasTeleported() && p.getRemoveEntityOnTele()))
					continue;
				stream.writeBits(11, p.getIndex());
				int y = p.getY() - player.getY();
				if (y < 15)
					y += 32;
				int x = p.getX() - player.getX();
				if (x < 15)
					x += 32;
				stream.writeBits(5, y);
				stream.writeBits(5, x);
				boolean needAppearenceUpdate = needAppearenceUpdate(p.getIndex(), p.getAppearence().getMD5AppeareanceDataHash());
				boolean needUpdate = needAppearenceUpdate || p.needMasksUpdate();
				stream.writeBits(1, 0); //reset path when entering dungeon, just set 0 for all so doesnt reset
				stream.writeBits(1, needUpdate ? 1 : 0);
				stream.writeBits(3, p.getDirection());
				localPlayers[localPlayersSize++] = p;
				addedPlayersCount++;
				addedIndexes.add(p.getIndex());
				if(needUpdate)
					appendUpdateBlock(p, updateBlockData, needAppearenceUpdate, true);
			}
		}
	}
	
	private void appendUpdateBlock(Player p, OutputStream data, boolean needAppearenceUpdate, boolean checkLastFaceEntity) {
		int maskData = 0;
		/*if (p.getNextForceTalk() != null)
			maskData |= 0x20;*/
		if(p.getNextHit1() != null)
			maskData |= 0x4;
		if(p.getNextFaceEntity() != -2 || p.getLastFaceEntity() != -1)
			maskData |= 0x80;
		if(needAppearenceUpdate)
			maskData |= 0x10;
		if(p.getNextFaceWorldTile() != null)
			maskData |= 0x2;
		if(p.getNextAnimation() != null)
			maskData |= 0x8;
		if(p.getNextGraphics() != null)
			maskData |= 0x100;
		if(p.getNextForceMovement() != null)
			maskData |= 0x1000;
		if(p.getNextHit2() != null)
			maskData |= 0x400;
		if(p.getNextPublicChatMessage() != null)
			maskData |= 0x40;
		if (p.getNextForceTalk() != null)
			maskData |= 0x20;
		if (maskData >= 256)
			maskData |= 0x1;
		data.writeByte(maskData);
		if (maskData >= 256)
			data.writeByte(maskData >> 8);
		if(p.getNextHit1() != null)
			applyHit1Mask(p, data);
		if(p.getNextFaceEntity() != -2 || p.getLastFaceEntity() != -1)
			applyFaceEntityMask(p, data);
		if(needAppearenceUpdate) 
			applyAppearanceMask(p, data);
		if(p.getNextFaceWorldTile() != null)
			applyFaceWorldTileMask(p, data);
		if(p.getNextAnimation() != null)
			applyAnimationMask(p, data);
		if(p.getNextGraphics() != null)
			applyGraphicsMask(p, data);
		if(p.getNextForceMovement() != null)
			applyForceMovementMask(p, data);
		if(p.getNextHit2() != null)
			applyHit2Mask(p, data);
		if(p.getNextPublicChatMessage() != null)
			applyPublicChatMessageMask(p, data);
		if (p.getNextForceTalk() != null)
			applyForceTalkMask(p, data);
	}
	
	private void applyForceMovementMask(Player p, OutputStream data) {
        data.writeByteC(p.getNextForceMovement().getToFirstTile().getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()));
        data.writeByte(p.getNextForceMovement().getToFirstTile().getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
        data.writeByteC(p.getNextForceMovement().getToSecondTile() == null ? 0 : p.getNextForceMovement().getToSecondTile().getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()));
        data.writeByte(p.getNextForceMovement().getToSecondTile() == null ? 0 : p.getNextForceMovement().getToSecondTile().getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
        data.writeShort((p.getNextForceMovement().getFirstTileTicketDelay()*600)/20+(p.getNextForceMovement().getToSecondTile() != null ? 0 : 5)); //ammount cycles plus 100ms cuz of tele
        data.writeShort128(p.getNextForceMovement().getToSecondTile() == null  ? 0 : ((p.getNextForceMovement().getSecondTileTicketDelay()*600)/20)+5);//ammount cycles plus 100ms cuz of tele
        data.writeByte128(p.getNextForceMovement().getDirection());
	}

	private void applyForceTalkMask(Player p, OutputStream data) {
		data.writeString(p.getNextForceTalk().getText());
	}
	
	private void applyFaceEntityMask(Player p, OutputStream data) {
		data.writeShort(p.getNextFaceEntity() == -2 ?  p.getLastFaceEntity()  : p.getNextFaceEntity());
	}

	private void applyFaceWorldTileMask(Player p, OutputStream data) {
		data.writeShortLE(p.getNextFaceWorldTile().getX() * 2 + 1);
		data.writeShortLE128(p.getNextFaceWorldTile().getY() * 2 + 1);
	}
	private void applyPublicChatMessageMask(Player p, OutputStream data) {
		data.writeShortLE128(p.getNextPublicChatMessage().getEffects());
		data.writeByte128(p.getRights());
		if(p.getNextPublicChatMessage() instanceof QuickChatMessage) {
			QuickChatMessage message = (QuickChatMessage) p.getNextPublicChatMessage();
			data.write128Byte(message.getMessage() == null ? 2 : message.getMessage().getBytes().length+2);
			data.writeShort(message.getFileId());
			if(message.getMessage() != null)
				data.writeBytes(message.getMessage().getBytes());
		}else {
			byte[] chatStr = new byte[256];
			chatStr[0] = (byte) p.getNextPublicChatMessage().getMessage().length();
			int offset = 1+Huffman.encryptMessage(1, p.getNextPublicChatMessage().getMessage().length(), chatStr, 0, p.getNextPublicChatMessage().getMessage().getBytes());//1 + Misc.encryptPlayerChat(chatStr, 0, 1, p.getNextPublicChatMessage().getMessage().length(), p.getNextPublicChatMessage().getMessage().getBytes());
			data.write128Byte(offset);
			for(int index = 0; index < offset; index++)
				data.writeByte(chatStr[index]);
		}
	}
	
	private void applyHit1Mask(Player p, OutputStream data) {
		data.writeSmart(p.getNextHit1().getDamage());
		data.writeByte128(p.getNextHit1().getMark(player, p));
		int Amthp = p.getHitpoints();
		int maxHp = p.getMaxHitpoints();
		if (Amthp > maxHp)
			Amthp = maxHp;
		data.writeByteC(Amthp * 255 / maxHp);
		//data.writeByte(p.getNextHit1().getIcon());
	}
	
	private void applyHit2Mask(Player p, OutputStream data) {
		data.writeSmart(p.getNextHit2().getDamage());
		data.writeByte(p.getNextHit2().getMark(player, p));
	//	data.writeByte(p.getNextHit2().getIcon());
	}
	
	private void applyAnimationMask(Player p, OutputStream data) {
		data.writeShort128(p.getNextAnimation().getId());
		data.write128Byte(p.getNextAnimation().getSpeed());
	}
	
	private void applyGraphicsMask(Player p, OutputStream data) {
		data.writeShort(p.getNextGraphics().getId());
		data.writeIntV1((p.getNextGraphics().getSpeed() & 0xffff) | (p.getNextGraphics().getHeight() << 16));
	}
	
	private void applyAppearanceMask(Player p, OutputStream data) {
		byte[] renderData = p.getAppearence().getAppeareanceData();
		totalRenderDataSentLength += renderData.length;
		cachedAppearencesHashes[p.getIndex()] = p.getAppearence().getMD5AppeareanceDataHash();
		data.writeByte(renderData.length);
        for (int index = 0; index < renderData.length; index++)
        	data.writeByte(-128 + renderData[index]);
	}
}
