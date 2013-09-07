package com.rs.game.player;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.io.OutputStream;

public final class LocalNPCUpdate {
	
	private Player player;
	private NPC[] localNPCS;
	private int localNPCsSize;
	private ArrayList<Integer> addedIndexes;
	
	public LocalNPCUpdate(Player player) {
		this.player = player;
		localNPCS = new NPC[Settings.LOCAL_NPCS_LIMIT];
		addedIndexes = new ArrayList<Integer>();
	}
	
	public OutputStream createPacketAndProcess() {
		OutputStream stream = new OutputStream();
		OutputStream updateBlockData = new OutputStream();
		stream.writePacketVarShort(250);
		processLocalNPCsInform(stream, updateBlockData);
		stream.writeBytes(updateBlockData.getBuffer(), 0, updateBlockData.getOffset());
		stream.endPacketVarShort();
		return stream;
	}
	
	private void processLocalNPCsInform(OutputStream stream, OutputStream updateBlockData) {
		stream.initBitAccess();
		processInScreenNPCs(stream, updateBlockData);
		addInScreenNPCs(stream, updateBlockData);
		if(updateBlockData.getOffset() > 0)
			stream.writeBits(15, 32767);
		stream.finishBitAccess();
	}
	
	private void processInScreenNPCs(OutputStream stream, OutputStream updateBlockData) {
		int size = localNPCsSize;
		localNPCsSize = 0;
		stream.writeBits(8, size);
		for (int index = 0; index < size; index++) {
			NPC n = localNPCS[index];
			if(n == null || n.hasFinished() || !player.withinDistance(n) || n.hasTeleported()) {
				stream.writeBits(1, 1);
				stream.writeBits(2, 3);
				if (n != null)
					addedIndexes.remove((Object) n.getIndex());
				continue;
			}
			localNPCS[localNPCsSize++] = n;
			boolean needUpdate = (updateBlockData.getOffset()+stream.getOffset() < (Settings.PACKET_SIZE_LIMIT - 500))  && n.needMasksUpdate();
			boolean walkUpdate = n.getNextWalkDirection() != -1;
			stream.writeBits(1, (needUpdate ||  walkUpdate) ? 1 : 0);
			if(walkUpdate) {
				stream.writeBits(2, n.getNextRunDirection() == -1 ? 1 : 2);
				if(n.getNextRunDirection() != -1)
					stream.writeBits(1, 1);
				stream.writeBits(3, n.getNextWalkDirection());
				if(n.getNextRunDirection() != -1)
					stream.writeBits(3, n.getNextRunDirection());
				stream.writeBits(1, needUpdate ? 1 : 0);
			}else if(needUpdate)
				stream.writeBits(2, 0);
			if(needUpdate)
				appendUpdateBlock(n, updateBlockData);
		}
	}
	
	private void addInScreenNPCs(OutputStream stream, OutputStream updateBlockData) {
		int addedNPCsCount = 0;
		for(int regionId : player.getMapRegionsIds()) {
			CopyOnWriteArrayList<Integer> indexes = World.getRegion(regionId).getNPCsIndexes();
			if(indexes == null)
				continue;
			for(int npcIndex :  indexes) {
				if (localNPCsSize > (localNPCS.length-1) || addedNPCsCount > 125 || (updateBlockData.getOffset()+stream.getOffset() > (Settings.PACKET_SIZE_LIMIT-500) ))
					break;
				NPC n = World.getNPCs().get(npcIndex);
				if (n == null || n.hasFinished() || addedIndexes.contains(n.getIndex()) || !player.withinDistance(n) || (n.hasTeleported() && n.getRemoveEntityOnTele()))
					continue;
				stream.writeBits(15, n.getIndex());
				stream.writeBits(1, 0); //reset path when entering dungeon, just set 0 for all so doesnt reset
				boolean needUpdate = n.needMasksUpdate();
				stream.writeBits(1, needUpdate ? 1 : 0);
				int y = n.getY() - player.getY();
				if (y < 15)
					y += 32;
				stream.writeBits(5, y);
				stream.writeBits(14, n.getId());
				stream.writeBits(3, n.getDirection());
				int x = n.getX() - player.getX();
				if (x < 15)
					x += 32;
				stream.writeBits(5, x);
				localNPCS[localNPCsSize++] = n;
				addedNPCsCount++;
				addedIndexes.add(n.getIndex());
				if(needUpdate)
					appendUpdateBlock(n, updateBlockData);
			}
		}
	}

	private void appendUpdateBlock(NPC n, OutputStream data) {
		int maskData = 0;
		if (n.getNextForceTalk() != null) {
			maskData |= 0x4;
		}
		if (n.getNextTransformation() != null) {
			maskData |= 0x40;
	}
		if(n.getNextGraphics() != null)
			maskData |= 0x10;
		if(n.getNextHit2() != null)
			maskData |= 0x2;
		if(n.getNextFaceWorldTile() != null)
			maskData |= 0x400;
		if(n.getNextAnimation() != null)
			maskData |= 0x80;
		if(n.getNextFaceEntity() != -2 || n.getLastFaceEntity() != -1)
			maskData |= 0x1;
		if(n.getNextHit1() != null)
			maskData |= 0x8;
		if (maskData >= 256)
			maskData |= 0x20;
		data.writeByte(maskData);
		if (maskData >= 256)
			data.writeByte(maskData >> 8);
		if (n.getNextForceTalk() != null) {
			applyForceTalkMask(n, data);
		}
		if (n.getNextTransformation() != null) {
			applyTransformationMask(n, data);
	}
		if(n.getNextGraphics() != null)
			applyGraphicsMask(n, data);
		if(n.getNextHit2() != null)
			applyHit2Mask(n, data);
		if(n.getNextFaceWorldTile() != null)
			applyFaceWorldTileMask(n, data);
		if(n.getNextAnimation() != null)
			applyAnimationMask(n, data);
		if(n.getNextFaceEntity() != -2 || n.getLastFaceEntity() != -1)
			applyFaceEntityMask(n, data);
		if(n.getNextHit1() != null)
			applyHit1Mask(n, data);
			
	}
	
	private void applyTransformationMask(NPC n, OutputStream data) {
		data.writeShortLE128(n.getNextTransformation().getToNPCId());
	}

	private void applyForceTalkMask(NPC n, OutputStream data) {
		data.writeString(n.getNextForceTalk().getText());
	}

	private void applyFaceWorldTileMask(NPC n, OutputStream data) {
		data.writeShort128(n.getNextFaceWorldTile().getX() * 2 + 1);
		data.writeShort(n.getNextFaceWorldTile().getY() * 2 + 1);
	}
	
	private void applyHit1Mask(NPC n, OutputStream data) {
		data.writeSmart(n.getNextHit1().getDamage());
		data.writeByte(n.getNextHit1().getMark(player, n));
		int Amthp = n.getHitpoints();
		int maxHp = n.getMaxHitpoints();
		if (Amthp > maxHp)
			Amthp = maxHp;
		data.writeByte(Amthp * 255 / maxHp);
	//	data.writeByte(n.getNextHit1().getIcon());
	}
	
	private void applyFaceEntityMask(NPC n, OutputStream data) {
		data.writeShortLE(n.getNextFaceEntity() == -2 ?  n.getLastFaceEntity()  : n.getNextFaceEntity());
	}
	
	private void applyAnimationMask(NPC n, OutputStream data) {
		data.writeShortLE128(n.getNextAnimation().getId());
		data.writeByte128(n.getNextAnimation().getSpeed());
	}

	private void applyHit2Mask(NPC n, OutputStream data) {
		data.writeSmart(n.getNextHit2().getDamage());
		data.writeByteC(n.getNextHit2().getMark(player, n));
	//	data.writeByte(n.getNextHit2().getIcon());
	}

	private void applyGraphicsMask(NPC n, OutputStream data) {
		data.writeShortLE(n.getNextGraphics().getId());
		data.writeIntLE((n.getNextGraphics().getSpeed() & 0xffff) | (n.getNextGraphics().getHeight() << 16));
	}
	
}
