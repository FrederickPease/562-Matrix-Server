package com.rs.net.encoders;


import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;


import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.DynamicRegion;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.npc.NPC;
import com.rs.game.player.HintIcon;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.io.OutputStream;
import com.rs.net.Session;
import com.rs.utils.Logger;
import com.rs.utils.MapContainersXteas;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;

public class WorldPacketsEncoder extends Encoder {

	private int ID;
	private Player player;
	private short interPacketsCount;
	
	
	public WorldPacketsEncoder(Session session, Player player) {
		super(session);
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public short getInterPacketsCount() {
		return interPacketsCount;
	}
	
	public void sendHideIComponent(int interfaceId, int componentId, boolean hidden) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(3);
		stream.writeShort128(interPacketsCount++);
		stream.write128Byte(hidden ? 1 : 0);
		stream.writeIntV2(interfaceId << 16 | componentId);
		session.write(stream);
	}
	public void sendRemoveGroundItem(FloorItem item) {
		sendWorldTile(item.getTile());
		OutputStream stream = new OutputStream(4);
		stream.writePacket(221);
		stream.write128Byte(0); //hash for make right spawn coord, used for big regions
		stream.writeShort(item.getId());
		session.write(stream);
	}
	
	public void sendGroundItem(FloorItem item) {
		sendWorldTile(item.getTile());
		OutputStream stream = new OutputStream(6);
		stream.writePacket(22);
		stream.writeShortLE128(item.getAmount());
		stream.writeShort(item.getId());
		stream.writeByteC(0);//hash for make right spawn coord, used for big regions
		session.write(stream);
	}
	
	
	public void sendProjectile2(Entity receiver, WorldTile startTile, WorldTile endTile, int gfxId, int startHeight, int endHeight, int speed, int delay,  int curve, int startDistanceOffset, int creatorSize) {
		sendWorldTile(startTile);
		OutputStream stream = new OutputStream();
		stream.writePacket(223);
		int x = startTile.getX() - (startTile.getRegionX() << 3);
		int y = startTile.getY() - (startTile.getRegionY() << 3);
		stream.writeByte((x & 0x7) << 3 | y & 0x7);
		stream.writeByte(endTile.getX()-startTile.getX());
		stream.writeByte(endTile.getY()-startTile.getY());
		stream.writeShort(receiver == null ? 0 : (receiver instanceof Player ? -(receiver.getIndex()+1) : receiver.getIndex()+1));
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		int duration = (Utils.getDistance(startTile.getX(), startTile.getY(), endTile.getX(), endTile.getY()) * 30 / ((speed / 10) < 1 ? 1 : (speed / 10))) + delay;
		stream.writeShort(duration);
		stream.writeByte(curve);
		stream.writeByte(64 + startDistanceOffset * 64);
		session.write(stream);
	}
	
	public void sendProjectile(Entity receiver, WorldTile startTile, WorldTile endTile, int gfxId, int startHeight, int endHeight, int speed, int delay,  int curve, int startDistanceOffset, int creatorSize) {
		sendWorldTile(startTile);
		OutputStream stream = new OutputStream();
		stream.writePacket(223);
		int x = startTile.getX()-startTile.getRegionX() << 3;
		int y = startTile.getY()-startTile.getRegionY() << 3;
		stream.writeByte((x & 0x7) << 3 | y & 0x7);
		stream.writeByte(endTile.getX()-startTile.getX());
		stream.writeByte(endTile.getY()-startTile.getY());
		stream.writeShort(receiver == null ? 0
				: (receiver instanceof Player ? -(receiver.getIndex() + 1)
						: receiver.getIndex() + 1));
		//stream.writeShort(receiver == null ? 0 : (receiver instanceof Player ? -(receiver.getIndex()+1) : receiver.getIndex()+1));
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		int duration = (Utils.getDistance(startTile.getX(), startTile.getY(),
				endTile.getX(), endTile.getY()) * 30 / ((speed / 10) < 1 ? 1
				: (speed / 10)))
				+ delay;
		stream.writeShort(duration);
		stream.writeByte(curve);
		stream.writeByte(creatorSize * 64 + startDistanceOffset * 64);
		session.write(stream);
	}
	
	public void sendProjectile12(Entity receiver, WorldTile startTile, WorldTile endTile, int gfxId, int startHeight, int endHeight, int speed, int delay,  int curve, int startDistanceOffset, int creatorSize) {
		sendWorldTile(startTile);
		OutputStream stream = new OutputStream();
		//OutputStream stream = createWorldTileStream(startTile);
		stream.writePacket(223);
		int localX = startTile.getLocalX(player.getLastLoadedMapRegionTile(),
				player.getMapSize());
		int localY = startTile.getLocalY(player.getLastLoadedMapRegionTile(),
				player.getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX & 0x7) << 3 | offsetY & 0x7);
		//stream.writeByte((offsetX << 3) | offsetY);
		stream.writeByte(endTile.getX() - startTile.getX());
		stream.writeByte(endTile.getY() - startTile.getY());
		stream.writeShort(receiver == null ? 0
				: (receiver instanceof Player ? -(receiver.getIndex() + 1)
						: receiver.getIndex() + 1));
		//stream.writeShort(receiver == null ? 0 : (receiver instanceof Player ? -(receiver.getIndex()+1) : receiver.getIndex()+1));
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		int duration = (Utils.getDistance(startTile.getX(), startTile.getY(),
				endTile.getX(), endTile.getY()) * 30 / ((speed / 10) < 1 ? 1
				: (speed / 10)))
				+ delay;
		stream.writeShort(duration);
		stream.writeByte(curve);
		stream.writeByte(creatorSize * 64 + startDistanceOffset * 64);
		session.write(stream);
	}
	
	public void sendProjectile1(Entity receiver, WorldTile startTile,
			WorldTile endTile, int gfxId, int startHeight, int endHeight,
			int speed, int delay, int curve, int startDistanceOffset,
			int creatorSize) {
		
		OutputStream stream = new OutputStream();
		stream.writePacket(223);
		int x = startTile.getX() - (startTile.getRegionX() - 6) * 8;
		int y = startTile.getY() - (startTile.getRegionY() - 6) * 8;
		int distance = Utils.getDistance(startTile.getX(), startTile.getY(), endTile.getX(), endTile.getY());
		int duration = (distance * 30 / ((speed / 10) < 1 ? 1 : (speed / 10))) + delay;
		stream.writeByte(player.getPlane());
		stream.writeByteC(y >> 3);
		stream.writeByte128(x >> 3);
		stream.writeByte(13); // projectile subopcode
		x = startTile.getX() - (startTile.getRegionX() << 3);
		y = startTile.getX() - (startTile.getRegionY() << 3);
		stream.writeByte((x & 0x7) << 3 | y & 0x7);
		stream.writeByte((startTile.getX() - endTile.getX()) * -1);
		stream.writeByte((startTile.getY() - endTile.getY()) * -1);
		stream.writeShort(receiver != null ? (receiver instanceof Player ? -(receiver.getIndex() + 1) 
				: (receiver.getIndex() + 1)) : -1);
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		stream.writeShort(duration);
		stream.writeByte(curve);
		stream.writeShort(64 + startDistanceOffset * 64);
		session.write(stream);
	}
	
	public void sendUnlockIComponentOptionSlots(int interfaceId, int componentId, int fromSlot, int toSlot, int... optionsSlots) {
		int settingsHash = 0;
		for(int slot : optionsSlots)
			settingsHash |= 2 << slot;
		sendIComponentSettings(interfaceId, componentId, fromSlot, toSlot, settingsHash);
	}
	
	public void sendIComponentSettings(int interfaceId, int componentId, int fromSlot, int toSlot, int settingsHash) {
		OutputStream stream = new OutputStream(15);
		stream.writePacket(113);
		stream.writeInt(settingsHash);
		stream.writeShortLE(toSlot);
		stream.writeShortLE(fromSlot);
		stream.writeShortLE128(interPacketsCount++);
		stream.writeIntLE(interfaceId << 16 | componentId);
		session.write(stream);
	}
	
	public void sendRunScript(int scriptId, Object... params) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(70);
		stream.writeShort(interPacketsCount++);
		 String parameterTypes = "";
		 if(params != null) {
			 for(int count = params.length-1; count >= 0; count--) {
				 if(params[count] instanceof String)
					 parameterTypes += "s"; //string
				 else
					 parameterTypes += "i"; //integer
			 }
		 }
		 stream.writeString(parameterTypes);
		 if(params != null) {
			 int index = 0;
			 for (int count = parameterTypes.length() - 1;count >= 0;count--) {
				 if (parameterTypes.charAt(count) == 's') 
					 stream.writeString((String) params[index++]);
				 else
					 stream.writeInt((Integer) params[index++]);
			 }
		 }
		stream.writeInt(scriptId);
		stream.endPacketVarShort();
		session.write(stream);
	}
	
	public void sendButtonConfig(int id, int value) {
		if(value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			OutputStream stream = new OutputStream(9);
			stream.writePacket(21);
			stream.writeShortLE128(id);
			stream.writeIntV1(value);
			stream.writeShort(interPacketsCount++);
			session.write(stream);
		}else {
			OutputStream stream = new OutputStream(6);
			stream.writePacket(164);
			stream.writeShortLE128(interPacketsCount++);
			stream.writeByte(value);
			stream.writeShortLE(id);
			session.write(stream);
		}
	}
	
	public void sendConfig(int id, int value) {
		if(value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendConfig2(id, value);
		else
			sendConfig1(id, value);
	}
	public void sendConfig1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(186);
		stream.write128Byte(value);
		stream.writeShortLE128(id);
		session.write(stream);
	}
	
	public void sendConfig2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(151);
		stream.writeIntLE(value);
		stream.writeShort(id);
		session.write(stream);
	}
	
	public void sendRunEnergy() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(63);
		stream.writeByte(player.getRunEnergy());
		session.write(stream);
	}
	
	public void sendIComponentText(int interfaceId, int componentId, String text) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(106);
		stream.writeShortLE128(interPacketsCount++);
		stream.writeString(text);
		stream.writeIntV2(interfaceId << 16 | componentId);
		stream.endPacketVarShort();
		session.write(stream);
	}
	
    public void sendIComponentAnimation(int emoteId, int interfaceId, int componentId) {
    	OutputStream stream = new OutputStream(9);
    	stream.writePacket(61);
    	stream.writeIntV2(interfaceId << 16 | componentId);
    	stream.writeShortLE(emoteId);
    	stream.writeShortLE128(interPacketsCount++);
        session.write(stream);
    }
	
    public void sendItemOnIComponent(int interfaceid, int componentId, int id, int amount) {
    	OutputStream stream = new OutputStream(13);
    	stream.writePacket(145);
    	stream.writeInt(interfaceid << 16 | componentId);
    	stream.writeIntV1(amount);
    	stream.writeShortLE128(interPacketsCount++);
    	stream.writeShortLE128(id);
    	session.write(stream);
    }
    
	public void sendEntityOnIComponent(boolean isPlayer, int entityId, int interfaceId, int componentId) {
		if(isPlayer)
			sendPlayerOnIComponent(interfaceId, componentId);
		else 
			sendNPCOnIComponent(interfaceId, componentId, entityId);
	}
	
	public void sendWorldTile(WorldTile tile) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(207);
		stream.write128Byte(tile.getLocalY(player.getLastLoadedMapRegionTile(),
				player.getMapSize()));
		stream.writeByte(tile.getLocalX(player.getLastLoadedMapRegionTile(),
				player.getMapSize()));
		session.write(stream);
	}
	
	public OutputStream createWorldTileStream(WorldTile tile) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(207);
		stream.writeByte128(tile.getLocalY(player.getLastLoadedMapRegionTile(),
				player.getMapSize()) >> 3);
		//stream.writeByteC(tile.getPlane());
		stream.writeByte(tile.getLocalX(player.getLastLoadedMapRegionTile(),
				player.getMapSize()) >> 3);
		return stream;
	}

	public void sendSpawnedObject(WorldObject object) {
		sendWorldTile(object);
		OutputStream stream = new OutputStream(5);
		stream.writePacket(115);
		stream.writeByteC(0); //the hash for coords, useless
		stream.writeShortLE128(object.getId());
		stream.writeByteC((object.getType() << 2) + (object.getRotation() & 0x3));
		session.write(stream);
	}
	public void sendPlayerOnIComponent(int interfaceId, int componentId) {
		OutputStream stream = new OutputStream(7);
        stream.writePacket(219);
        stream.writeShort128(interPacketsCount++);
        stream.writeIntV1(interfaceId << 16 | componentId);
        session.write(stream);
   }

	
	public void sendNPCOnIComponent(int interfaceId, int componentId, int npcId) {
		OutputStream stream = new OutputStream(10);
		stream.writePacket(158);
		stream.writeShortLE128(npcId);
		stream.writeShort(interPacketsCount++);
		stream.writeIntV2(interfaceId << 16 | componentId);
		session.write(stream);
	}
	
	
	public void sendRandomOnIComponent(int interfaceId, int componentId, int id) {
		OutputStream stream = new OutputStream();
		stream.writePacket(235);
		stream.writeShort(id);
		stream.writeIntV1(interfaceId << 16 | componentId);
		stream.writeShort(interPacketsCount++);
		session.write(stream);
	}
	
	public void sendFaceOnIComponent(int interfaceId, int componentId, int look1, int look2, int look3) {
		OutputStream stream = new OutputStream();
		stream.writePacket(192);
		stream.writeIntV2(interfaceId << 16 | componentId);
		stream.writeShortLE128(interPacketsCount++);
		stream.writeShortLE128(look1);
		stream.writeShortLE128(look2); 
		stream.writeShort128(look2); 
		session.write(stream);
	}
	
	public void sendFriend(String Username, String displayName, int world, boolean putOnline, boolean WarnMessage) {
		if (displayName.equals(Username))
			Username = "";
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(20);
		stream.writeByte(WarnMessage ? 0 : 1);
		stream.writeString(displayName);
		stream.writeString(Username);
		stream.writeShort(putOnline ? 1 : 0);
		stream.writeByte(world);
		if (putOnline) {
			stream.writeString("<col=00FF00>"+Settings.SERVER_NAME+" "+world);
			stream.writeByte(0);
		}
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendIgnore(String Username, String displayName) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(75);
		stream.writeByte(0);
		if (displayName == Username)
			Username = "";
		stream.writeString(displayName);
		stream.writeString(Username);
		stream.writeString(Username);
		stream.writeString(displayName);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendPrivateMessage(String Username, String message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(92);
		stream.writeString(Username);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void receivePrivateMessage(String Username, String displayName, int rights, String message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(226);
		stream.writeByte(Username.equals(displayName) ? 0 : 1);
		stream.writeString(displayName);
		if (!Username.equals(displayName))
			stream.writeString(Username);
		int hash = player.getFriendsIgnores().getNextIgnoredMessageCount();
		int firstHashPart = hash >> 32;
		stream.writeShort(firstHashPart);
		stream.write3Bytes(hash -  (firstHashPart << 32));
		stream.writeByte(rights);
		Huffman.sendEncryptMessage(stream, message);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendUnlockIgnoreList() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(0);
		session.write(stream);
	}

	public void sendUnlockFriendList() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(5);
		stream.writeByte(2);
		session.write(stream);
	}
	
	/*
	 * dynamic map region
	 */
	public void sendDynamicMapRegion() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(146);
		stream.write128Byte(player.isForceNextMapLoadRefresh() ? 1 : 0);  //force refresh
		int regionX = player.getRegionX();
		int regionY = player.getRegionY();
		stream.writeShort(regionY);
		stream.writeByte128(player.getMapSize());
		stream.writeShortLE128(player.getLocalX(player, player.getMapSize()));
		stream.writeShort128(player.getLocalY(player, player.getMapSize()));
		stream.writeByteC(player.getPlane());
		stream.writeShort(regionX);
		stream.initBitAccess();
		int mapHash = Settings.MAP_SIZES[player.getMapSize()] >> 4;
		int[] realRegionIds = new int[4 * mapHash * mapHash];
		int realRegionIdsCount = 0;
		for(int plane = 0; plane < 4; plane++) {
			for (int thisRegionX = (regionX - mapHash) ; thisRegionX <= ((regionX + mapHash) ); thisRegionX++) { //real x calcs
				for (int thisRegionY = (regionY - mapHash) ; thisRegionY <= ((regionY + mapHash)); thisRegionY++) { //real y calcs
					int regionId = (((thisRegionX / 8) << 8) + (thisRegionY / 8));
					Region region = World.getRegion(regionId);
					int realRegionX;
					int realRegionY;
					int realPlane;
					int rotation;
					if(region instanceof DynamicRegion) { //generated map
						DynamicRegion dynamicRegion = (DynamicRegion) region;
						int[] regionCoords = dynamicRegion.getRegionCoords()[plane][thisRegionX - ((thisRegionX/8) * 8)][thisRegionY - ((thisRegionY/8) * 8)];
						realRegionX = regionCoords[0];
						realRegionY = regionCoords[1];
						realPlane = regionCoords[2];
						rotation = regionCoords[3];
					}else { //real map
						//base region + difference * 8 so gets real region coords
						realRegionX = thisRegionX;
						realRegionY = thisRegionY;
						realPlane = plane;
						rotation = 0;//no rotation
					}
					//invalid region, not built region
					if(realRegionX == 0 || realRegionY == 0)
						stream.writeBits(1, 0);
					else {
						stream.writeBits(1, 1);
						stream.writeBits(26,(rotation << 1) | (realPlane << 24) | (realRegionX << 14) | (realRegionY << 3));
						int realRegionId = (((realRegionX / 8) << 8) + (realRegionY / 8));
						boolean found = false;
						for(int index = 0; index < realRegionIdsCount; index++)
							if(realRegionIds[index] == realRegionId) {
								found = true;
								break;
							}
						if(!found)
							realRegionIds[realRegionIdsCount++] = realRegionId;
					}
					
				}
			}
		}
		stream.finishBitAccess();
		for(int index = 0; index < realRegionIdsCount; index++) {
			int[] xteas = MapContainersXteas.getMapContainerXteas(realRegionIds[index]);
			if(xteas == null)
				xteas = new int[4];
			for(int keyIndex = 0; keyIndex < 4; keyIndex++)
				stream.writeInt(xteas[keyIndex]);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}
	
	/*
	 * normal map region
	 */
	public void sendMapRegion() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(76);
		stream.writeShortLE128(player.getRegionX());
		stream.writeShort(player.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		stream.writeByte(player.getMapSize());
		stream.writeByte128(player.isForceNextMapLoadRefresh() ? 1 : 0); //force refresh
		for(int regionId : player.getMapRegionsIds()) {
			int[] xteas = MapContainersXteas.getMapContainerXteas(regionId);
			if(xteas == null)
				xteas = new int[4];
			for(int index = 0; index < 4; index++)
				stream.writeInt(xteas[index]);
		}
		stream.writeByte128(player.getPlane());
		stream.writeShort(player.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		stream.writeShort(player.getRegionY());
		stream.endPacketVarShort();
		session.write(stream);
	}
	
	/*
	 * normal map region
	 *//*
public void sendMapRegion() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(76);
		stream.writeShortLE128(player.getRegionX());
		stream.writeShort(player.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		stream.writeByte(player.getMapSize());
		stream.writeByte128(player.isForceNextMapLoadRefresh() ? 1 : 0); //force refresh
		for(int regionId : player.getMapRegionsIds()) {
			int[] xteas = MapContainersXteas.getMapContainerXteas(regionId);
			if(xteas == null)
				xteas = new int[4];
			for(int index = 0; index < 4; index++)
				stream.writeInt(xteas[index]);
		}
		stream.writeByte128(player.getPlane());
		stream.writeShort(player.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		stream.writeShort(player.getRegionY());
		stream.endPacketVarShort();
		session.write(stream);
	}*/
	/*
	 * normal map region
	 *//*
	public void sendMapRegion() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(76);
		//stream.writeShort(player.getChunkX());
		//stream.writeShort(player.getChunkY());
		//stream.writeByte128(player.getPlane());
		stream.writeShortLE128(player.getChunkX());
		stream.writeShort(player.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		stream.writeByte(player.getMapSize());
		stream.writeByte128(player.isForceNextMapLoadRefresh() ? 1 : 0); //force refresh
		for(int regionId : player.getMapRegionsIds()) {
			int[] xteas = MapContainersXteas.getMapContainerXteas(regionId);
			if(xteas == null)
				xteas = new int[4];
			for(int index = 0; index < 4; index++)
				stream.writeInt(xteas[index]);
		}
		stream.writeByte128(player.getPlane());
		stream.writeShort(player.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		stream.writeShort(player.getChunkY());
		stream.endPacketVarShort();
		session.write(stream);
	}*/
/*
	public void sendMapRegion() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(76);
		//stream.writeShortLE128(player.getRegionX());
		//stream.writeShort(player.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		stream.writeShort(player.getChunkX());
		stream.writeShort(player.getChunkY());
		stream.writeByte(player.getMapSize());
		stream.writeByte128(player.isForceNextMapLoadRefresh() ? 1 : 0); //force refresh
		for(int regionId : player.getMapRegionsIds()) {
			int[] xteas = MapContainersXteas.getMapContainerXteas(regionId);
			if(xteas == null)
				xteas = new int[4];
			for(int index = 0; index < 4; index++)
				stream.writeInt(xteas[index]);
		}
		stream.writeByte128(player.getPlane());
		//stream.writeShort(player.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		//stream.writeShort(player.getRegionY());
		stream.endPacketVarShort();
		session.write(stream);
	}
	*/
	/*
	 * sets the pane interface
	 */
	public void sendWindowsPane(int id, int type) {
		player.getInterfaceManager().setWindowsPane(id);
		OutputStream stream = new OutputStream(6);
		stream.writePacket(50);
		stream.writeShort128(id);
		stream.writeShort(interPacketsCount++);
		stream.write128Byte(type);
		session.write(stream);
	}
	
	public void sendPlayerOption(String option, int slot, boolean top) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(73);
		stream.writeShortLE128(65535);
		stream.writeString(option);
		stream.writeByteC(top ? 1 : 0);
		stream.write128Byte(slot);
		stream.endPacketVarByte();
		session.write(stream);
	}
	
	/*
	 * sends local players update
	 */
	public void sendLocalPlayersUpdate() {
		session.write(player.getLocalPlayerUpdate().createPacketAndProcess());
	}
	/*
	 * sends local npcs update
	 */
	public void sendLocalNPCsUpdate() {
		session.write(player.getLocalNPCUpdate().createPacketAndProcess());
	}
	
	public void sendGraphics(Graphics graphics, Object target) {
		OutputStream stream = new OutputStream(11);
		stream.writePacket(109);
		stream.writeShortLE128(graphics.getHeight());
		int hash = 0;
		if(target instanceof WorldTile) {
			WorldTile tile = (WorldTile) target;
			hash = tile.getPlane() << 28 | tile.getX() << 14 | tile.getY() & 0x3fff| 1 << 30;
		}else if (target instanceof Player) {
			Player p = (Player) target;
			hash = p.getIndex() & 0xffff | 1 << 28;
		}else{
			NPC n = (NPC) target;
			hash = n.getIndex() & 0xffff;
		}
		stream.writeIntLE(hash);
		stream.writeShort128(graphics.getId());
		stream.writeShortLE128(graphics.getSpeed());
		session.write(stream);
	}
	
	public void sendDelayedGraphics(Graphics graphics, int delay, WorldTile tile) {
		
	}

	public void closeInterface(int windowId, int windowComponentId) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(203);
		stream.writeShort(interPacketsCount++);
		stream.writeInt(player.getInterfaceManager().getTabWindow(windowComponentId) << 16 | windowComponentId);
		session.write(stream);
		player.getInterfaceManager().removeTab(windowComponentId);
	}
	
	public void closeInterface(int windowComponentId) {
		closeInterface(
				player.getInterfaceManager().getTabWindow(windowComponentId),
				windowComponentId);
		player.getInterfaceManager().removeTab(windowComponentId);
	}
	
	public void sendInterface(boolean cliped, int windowId, int windowComponentId, int interfaceId) {
		//currently fixes the inter engine.. not ready for same component ids(tabs), different inters
		if(!(windowId == 752 && windowComponentId == 8 && interfaceId == 137)) { //if chatbox 
		if (player.getInterfaceManager().containsInterface(windowComponentId, interfaceId))
			closeInterface(windowComponentId);
		if (!player.getInterfaceManager().addInterface(windowId, windowComponentId, interfaceId)) {
			Logger.log(this, "Error adding interface: " + windowId + " , "	+ windowComponentId + " , " + interfaceId);
			return;
		}
		}
		OutputStream stream = new OutputStream(10);
		stream.writePacket(56);
		stream.writeShort(interfaceId);
		stream.write128Byte(cliped ? 1 : 0);
		stream.writeShort(interPacketsCount++);
		stream.writeIntLE(windowId << 16 | windowComponentId);
		session.write(stream);
	}
	
	public void sendSystemUpdate(int delay) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(231);
		stream.writeShortLE((int) ((delay + 3) * 1.6));
		session.write(stream);
  	}/*
	public void sendUpdateItems(int key, boolean negativeKey, Item[] items,
			int... slots) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(58);
		stream.writeShort(key);
		stream.writeByte(negativeKey ? 1 : 0);
		for (int slotId : slots) {
			if (slotId >= items.length)
				continue;
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			Item item = items[slotId];
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			if (id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if (amount >= 255)
					stream.writeInt(amount);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}
	public void sendUpdateItems(int interfaceId, int componentId, int key, ItemsContainer<Item> items, int... slots) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(58);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeShort(key);
		for(int slotId : slots) {
			if(slotId >= items.getSize())
				continue;
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			Item item = items.get(slotId);
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id+1);
			if(id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if(amount >= 255)
					stream.writeInt(amount);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}*/
	
	
	public void sendUpdateItems(int interfaceId, int componentId, int key, ItemsContainer<Item> items, int... slots) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(58);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeShort(key);
		for(int slotId : slots) {
			if(slotId >= items.getSize())
				continue;
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			Item item = items.get(slotId);
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id+1);
			if(id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if(amount >= 255)
					stream.writeInt(amount);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}
	
	public void sendUpdateItems(int interfaceId, int componentId, int key, Item[] items, int... slots) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(58);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeShort(key);
		for (int slotId : slots) {
			if (slotId >= items.length)
				continue;
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			Item item = items[slotId];
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id+1);
			if(id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if(amount >= 255)
					stream.writeInt(amount);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	}
	
	
	/*public void sendItems(int interfaceId, int componentId, int key, ItemsContainer<Item> items) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(120);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeShort(key);
		stream.writeShort(items.getSize());
		for(int index = 0; index < items.getSize(); index++) {
			Item item = items.get(index);
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeByteC(amount >= 255 ? 255 : amount);
			if(amount >= 255)
				stream.writeIntV1(amount);
			stream.writeShortLE(id + 1);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}*/
	
	public void sendLogout() {
		OutputStream stream = new OutputStream();
		stream.writePacket(236);
		ChannelFuture future = session.write(stream);
		if(future != null) 
			future.addListener(ChannelFutureListener.CLOSE);
		else
			session.getChannel().close();
	}
	
	public void sendGameMessage(String text) {
		sendGameMessage(text, false);
	}
	
	public void sendGameMessage(String text, boolean filter) {
		sendMessage(filter ? 22 : 0, text, null);
	}
	
	public void sendAllianceRequestMessage(String text, Player p) {
		sendMessage(21, text, p);
	}
	
	public void sendAssistMessage(String text) {
		sendMessage(13, text, null);
	}
	
	public void sendClanMessage(String text) {
		sendMessage(11, text, null);
	}
	
	public void sendPrivateMessage(String text) {
		sendMessage(5, text, null);
	}
	
	public void sendPanelBoxMessage(String text) {
		sendMessage(99, text, null);
	}
	
	public void sendTradeMessage(String text) {
		sendMessage(12, text, null);
	}
	
	public void sendClanWarsRequestMessage(Player p) {
		sendMessage(16, "", p);
	}

	public void sendDuelChallengeRequestMessage(Player p, boolean friendly) {
		sendMessage(friendly ? 15 : 14, "", p);
	}
	
	public void sendPrivateMessageTo(String text, Player p) {
		sendMessage(6, text, p);
	}
	
	public void sendPrivateMessageFrom(String text, Player p) {
		sendMessage(3, text, p);
	}
	
	public void sendPublicMessage(String text, Player p) {
		sendMessage(1, text, p);
	}
	
	public void sendTradeRequestMessage(Player p) {
		sendMessage(4, "wishes to trade with you.", p);//100
	}
	
	public void sendChallengeRequestMessage(String text, Player p) {
		sendMessage(8, text, p);
	}
	
	public void sendMessage(int type, String text, Player p) {
		int maskData = 0;
		if(type == 1 || type == 3 || type == 4 || type == 6 || type == 7 || type == 8 || type == 9 || type == 10 || type == 14 || type == 15) {
			maskData |= 0x1;
			if(p.hasDisplayName())
					maskData |= 0x2;
			
		}
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(193);
		stream.writeSmart(type);
		stream.writeInt(0); //junk, not used by client
		stream.writeByte(maskData);
		if((maskData & 0x1) != 0) {
		stream.writeString(p.getDisplayName());
			if(p.hasDisplayName())
				stream.writeString(Utils.formatPlayerNameForDisplay(p.getUsername()));
		}
		stream.writeString(text);
		stream.endPacketVarByte();
		session.write(stream);
	}

	public void sendSound(int id, int delay) {
		OutputStream stream = new OutputStream(8);
		stream.writePacket(136);
		stream.writeShort(id);
		stream.writeByte(1); 
		stream.writeShort(delay);
		stream.writeByte(255);
	//	stream.writeByte(effectType);
		session.write(stream);
	}
	
	public void sendMusic2(int musicId, int categoryId) {
		OutputStream stream = new OutputStream(8);
		stream.writePacket(30);
		stream.writeByte(255);
		stream.writeIntLE(musicId);
		stream.writeShortLE128(categoryId);
		session.write(stream);
	}	
	
	public void sendMusic(int id) {
		if (id == 3 || id == 0 || id == 76) {
			OutputStream stream = new OutputStream(4);
			stream.writePacket(188);
			stream.writeShortLE128((short) id);
			stream.write128Byte((byte) 255);
			session.write(stream);
		}
	}
	
	
	
	public void sendSkillLevel(int skill) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(38);
		stream.writeByte128(player.getSkills().getLevel(skill));
		stream.writeIntV1((int) player.getSkills().getXp(skill));
		stream.writeByteC(skill);
		session.write(stream);
	}

	public void sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, int type, String... options) {
		Object[] parameters = new Object[6+options.length];
		int index = 0;
		for(int count = options.length-1; count >= 0; count--)
			parameters[index++] = options[count];
		parameters[index++] = -1; //dunno but always this
		parameters[index++] = 0;//dunno but always this
		parameters[index++] = 4; //dunno but always this
		parameters[index++] = type; //setitems position
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(150, parameters); //scriptid 150 does that the method name says
	}

	public void sendFletchingInterface() {
		sendButtonConfig(754, 3);
		sendButtonConfig(755, 52);
		sendButtonConfig(756, 50);
		sendButtonConfig(757, 48);
		sendButtonConfig(758, 9440);
		sendChatboxInterface(513);
	}
	
	public void sendChatboxInterface(int childId) {
		sendInterface(true, 752, childId, 12);
	}
	
	public void sendItems(int type, ItemsContainer<Item> inventory, boolean split) {
		OutputStream bldr = new OutputStream();
		bldr.writePacketVarShort(113);
		bldr.writeShort(type);
		bldr.writeByte((split ? 1 : 0));
		bldr.writeShort(inventory.getSize());
		for (int i = 0; i < inventory.getSize(); i++) {
			Item item = inventory.get(i);
			int id, amt;
			if (item == null) {
				id = -1;
				amt = 0;
			} else {
				id = item.getId();
				amt = item.getAmount();
			}
			bldr.writeShortLE(id + 1);
			bldr.writeByteC(amt > 254 ? 0xff : amt);
			if (amt > 0xfe)
				bldr.writeIntV1(amt);
		}
		bldr.endPacketVarShort();
		session.write(bldr);
	}
	
	public void sendItems(int type, ItemsContainer<Item> inventory) {
		OutputStream bldr = new OutputStream();
		bldr.writePacketVarShort(113);
		bldr.writeShort(type);
		//bldr.writeByte((split ? 1 : 0));
		bldr.writeShort(inventory.getSize());
		for (int i = 0; i < inventory.getSize(); i++) {
			Item item = inventory.get(i);
			int id, amt;
			if (item == null) {
				id = -1;
				amt = 0;
			} else {
				id = item.getId();
				amt = item.getAmount();
			}
			bldr.writeShortLE(id + 1);
			bldr.writeByteC(amt > 254 ? 0xff : amt);
			if (amt > 0xfe)
				bldr.writeIntV1(amt);
		}
		bldr.endPacketVarShort();
		session.write(bldr);
	}

	public void sendItems(int interfaceId, int componentId, int key, ItemsContainer<Item> items) {
		sendItems(interfaceId, componentId, key, items.getItems());
	}
	
	
	public void sendItems(int interfaceId, int componentId, int key, Item[] items) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(120);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeShort(key);
		stream.writeShort(items.length);
		for(int index = 0; index < items.length; index++) {
			Item item = items[index];
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeByteC(amount >= 255 ? 255 : amount);
			if(amount >= 255)
				stream.writeIntV1(amount);
			stream.writeShortLE(id + 1);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void sendRemoveObject(WorldObject object) {
		sendWorldTile(object);
		OutputStream stream = new OutputStream();
		stream.writePacket(27);
		stream.writeByteC(0); //The hash for coords
		stream.writeByteC((object.getType() << 2) + (object.getRotation() & 0x3));
		session.write(stream);
	}
	public void sendDestroyObject(WorldObject object) {
		sendWorldTile(object);
		OutputStream stream = new OutputStream(3);
		stream.writePacket(27);
		stream.writeByte128(0); //the hash for coords, useless
		stream.writeByteC((object.getType() << 2) + (object.getRotation() & 0x3));
		session.write(stream);
	}
	public void sendCloseInterface() {
		//if (player.getInterfaceManager()) {
			closeInterface(746, 3);
			closeInterface(746, 8);
		
		closeInterface(548, 16);
	}
	public void sendAccessMask(int set1, int set2, int interfaceId1, int childId1, int interfaceId2, int childId2) {
		OutputStream stream = new OutputStream(15);
		stream.writePacket(113);
		stream.writeInt(interfaceId2 << 16 | childId2);
		stream.writeShortLE(set2);
		stream.writeShortLE(set1);
		stream.writeShortLE128(0);
		stream.writeIntLE(interfaceId1 << 16 | childId1);
		session.write(stream);
	}

	public void sendBlankClientScript(int id) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(70);
				stream.writeShort(id);
				stream.writeString("");
				stream.endPacketVarShort();
				session.write(stream);
	}

	
	public void sendAccessMask(int set, int inter, int child, int off, int len) {
		OutputStream stream = new OutputStream();
		stream.writePacket(113);
				stream.writeInt(set);
				stream.writeShortLE(len);
				stream.writeShortLE(off);
				stream.writeShortLE128(ID++);
				stream.writeIntLE(inter << 16 | child);
				session.write(stream);
	}
	
	public void resetGe(Player player, int slot) {
		OutputStream output = new OutputStream();
		output.writePacket(134);
		output.writeByte(slot);
		output.writeByte(0);
		output.writeShort(0);
		output.writeInt(0);
		output.writeInt(0);
		output.writeInt(0);
		output.writeInt(0);
		session.write(output);
	}
	
	
	
	public void resetGe(int slot) {
		OutputStream stream = new OutputStream();
		stream.writePacket(134);
				stream.writeByte((byte) slot);
				stream.writeByte((byte) 0);
				session.write(stream);
	}

	public void setGeSearch(Object[] o) {
		sendConfig1(1109, -1);
		sendConfig1(1112, 0);
		sendConfig1(1113, 0);
		sendInterface(true, 752, 389, 6);
		sendRunScript(570, o, "s");
	}


	public void setGeSearch1(Object[] o) {
		sendConfig1(1109, -1);
		sendConfig1(1112, 0);
		sendConfig1(1113, 0);
		sendInterface(true, 752, 389, 6);
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(70);
		stream.writeString("");
		String valstring = "s";
		int j = 0;
		for (int i = valstring.length() - 1; i >= 0; i--) {
			if (valstring.charAt(i) == 's')
				stream.writeString(((String) o[j]));
			else
				stream.writeInt((Integer) o[j]);
		}
		j++;
		stream.writeInt(570);
		stream.endPacketVarShort();
		session.write(stream);
	}

	public void setGe(int slot, int progress, int item, int price, int amount, int currentAmount) {
		OutputStream stream = new OutputStream();
		stream.writePacket(134);
				stream.writeByte((byte) slot);
				stream.writeByte((byte) progress);
				stream.writeShort(item);
				stream.writeInt(price);
				stream.writeInt(amount);
				stream.writeInt(currentAmount);
				stream.writeInt(price * currentAmount);
				session.write(stream);
	}
	
	public void setGe(Player player, int slot, int progress, int item,
			int price, int amount, int currentAmount) {
		OutputStream output = new OutputStream();
		output.writePacket(134);
		output.writeByte(slot);
		output.writeByte(progress);
		output.writeShort(item);
		output.writeInt(price);
		output.writeInt(amount);
		output.writeInt(currentAmount);
		output.writeInt(price * currentAmount);
		session.write(output);
	}
	
	public void sendItems(int interfaceId, int childId, int type, int[] itemArray, int[] itemAmt) {
		int main = interfaceId * 65536 + childId;
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(120);
		stream.writeInt(main);
		stream.writeShort(type);
		stream.writeShort(itemArray.length);
		for (int i = 0; i < itemArray.length; i++) {
			if (itemAmt[i] > 254) {
				stream.writeByteC((byte) 255);
				stream.writeIntV1(itemAmt[i]);
			} else {
				stream.writeByteC((byte) itemAmt[i]);
			}
			stream.writeShortLE(itemArray[i] + 1);
		}
		stream.endPacketVarShort();
		session.write(stream);
	}
	public void setItemSlot(int slot, int item, int amount) {
        if(amount == 0) {
        return;
        }
        switch(slot) {
        case 0:
            sendItems(-1, -1757, 523, new int[] {item}, new int[] {amount});
            break;
        case 1:
            sendItems(-1, -1758, 524, new int[] {item}, new int[] {amount});
            break;
        case 2:
            sendItems(-1, -1759, 525, new int[] {item}, new int[] {amount});
            break;
        case 3:
            sendItems(-1, -1760, 526, new int[] {item}, new int[] {amount});
            break;
        case 4:
            sendItems(-1, -1761, 527, new int[] {item}, new int[] {amount});
            break;
        case 5:
            sendItems(-1, -1762, 528, new int[] {item}, new int[] {amount});
            break;
        }
    }
	
	public void setItemSlot(int slot, int[] item, int[] amount) {
		switch (slot) {
		case 0:
			sendItems(-1, -1757, 523, item, amount);
			break;
		case 1:
			sendItems(-1, -1758, 524, item, amount);
			break;
		case 2:
			sendItems(-1, -1759, 525, item, amount);
			break;
		case 3:
			sendItems(-1, -1760, 526, item, amount);
			break;
		case 4:
			sendItems(-1, -1761, 527, item, amount);
			break;
		case 5:
			sendItems(-1, -1762, 528, item, amount);
			break;
		}
	}
	public void sendGESetOption() {
		sendItems(-1, 64209, 93, player.getInventory().getItems());
		sendAccessMask(14, 645, 16, 0, 73);
		sendBlankClientScript(676);
		Object[] tparams1 = new Object[] { "null", "null", "null", "null", "null", "null", "null", "Exchange", "Components", -1, 0, 7, 4, 93, 42205184 };
		sendRunScript(150, tparams1, "IviiiIsssssssss");
		sendAccessMask(1030, 644, 0, 0, 27);
		// GESET(35913813);
		// GESET(35913743);
	}

	public void ReturnItem() {
		player.getInterfaceManager().sendInterface(772);
		sendItems(-1, 63762, 540, player.getInventory().getItems());
		sendAccessMask(1026, 772, 10, -1, -1);
	}

	public void resetItemSlot(Player p, int slot) {
		int[] item = { -1, -1 };
		int[] amount = { 0, 0 };
		switch (slot) {
		case 0:
			sendItems(-1, -1757, 523, item, amount);
			break;
		case 1:
			sendItems(-1, -1758, 524, item, amount);
			break;
		case 2:
			sendItems(-1, -1759, 525, item, amount);
			break;
		case 3:
			sendItems(-1, -1760, 526, item, amount);
			break;
		case 4:
			sendItems(-1, -1761, 527, item, amount);
			break;
		case 5:
			sendItems(-1, -1762, 528, item, amount);
			break;
		}
	}


	public void sendInventoryInterface(int childId) {
		//if (player.isFullScreen()) {
			sendInterface(true, 746, childId, 26);
		//} else {
			sendInterface(true, 548, childId, 145);
		//}
	}

	public void sendCloseInventoryInterface() {
		//if (player.isFullScreen()) {
			closeInterface(746, 26);
		//} else {
			closeInterface(548, 145);
		//}
	}


	public WorldPacketsEncoder setInterfaceConfig(int interfaceId, int childId, boolean set) {
		OutputStream stream = new OutputStream();
		stream.writePacket(3);
		stream.writeShort128(0);
		stream.write3Bytes(set ? 1 : 0);//writebyteS
		stream.writeIntV2(interfaceId << 16 | childId);
		session.write(stream);
		return this;
	}

	public void sendHintIcon(HintIcon icon) {
		OutputStream stream = new OutputStream();
		stream.writePacket(122);
		stream.writeByte((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5));
		if (icon.getTargetType() == 0)
			stream.skip(13);
		else {
			stream.writeByte(icon.getArrowType());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				stream.writeShort(icon.getTargetIndex());
				stream.writeShort(2500); // how often the arrow flashes, 2500 ideal, 0 never
				stream.skip(4);
			} else if ((icon.getTargetType() >= 2 && icon.getTargetType() <= 6)) { // directions
				stream.writeByte(icon.getPlane()); // unknown
				stream.writeShort(icon.getCoordX());
				stream.writeShort(icon.getCoordY());
				stream.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
				stream.writeShort(-1); //distance to start showing on minimap, 0 doesnt show, -1 infinite
			}
			stream.writeInt(icon.getModelId());//int
		}
		session.write(stream);

	}

	public void sendCameraShake(int a, int b, int c, int d, int e) {
		OutputStream stream = new OutputStream();
		stream.writePacket(157);
		stream.writeShort(0);
		stream.writeByte((byte) a);
		stream.writeByte((byte) b);
		stream.writeByte((byte) c);
		stream.writeByte((byte) d);
		stream.writeShort(e);
		session.write(stream);
	}
	
	public void sendCameraRotation(int X, int Y, int Z, int speed, int speed2) {
		OutputStream stream = new OutputStream();
		stream.writePacket(44);
		stream.writeShort(121212);
		stream.writeByte(X);
		stream.writeByte(Y);
		stream.writeShort(Z);
		stream.writeByte(speed);
		stream.writeByte(speed2);
		session.write(stream);
	}
	
	public void sendCameraPlacement(int X, int Y, int Z, int speed, int speed2) {
		OutputStream stream = new OutputStream();
		stream.writePacket(121);
		stream.writeShort(121212);
		stream.writeByte(X);
		stream.writeByte(Y);
		stream.writeShort(Z);
		stream.writeByte(speed);
		stream.writeByte(speed2);
		session.write(stream);
	}

	public void blackout(int id) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(208);
		stream.writeByte((byte) id);
		session.write(stream);
	}

	public void sendStopCameraShake() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(157);
		session.write(stream);
	}

	public void sendInterSetItemsOptionsScript(int interfaceId,
			int componentId, int key, int width, int height, String... options) {
		sendInterSetItemsOptionsScript(interfaceId, componentId, key, false, width, height, options);
	}
	public void sendInterSetItemsOptionsScript(int interfaceId,
			int componentId, int key, boolean negativeKey, int width, int height, String... options) {
		Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		parameters[index++] = -1; // dunno but always this
		parameters[index++] = 0;// dunno but always this, maybe startslot?
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(negativeKey ? 695 : 150, parameters); // scriptid 150 does that the method
										// name says*/
	}

	public void sendUpdateItems(int key, ItemsContainer<Item> items,
			int... slots) {
		sendUpdateItems(key, items.getItems(), slots);
	}

	public void sendUpdateItems(int key, Item[] items, int... slots) {
		sendUpdateItems(key, items, slots);
	}

	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ,
			int speed1, int speed2) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(44);
		stream.writeByte128(viewLocalY);
		stream.writeByte(speed1);
		stream.writeByteC(viewLocalX);
		stream.writeByte(speed2);
		stream.writeShort128(viewZ >> 2);
		session.write(stream);
	}

	// instant
	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ) {
		sendCameraLook(viewLocalX, viewLocalY, viewZ, -1, -1);
	}

	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ,
			int speed1, int speed2) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(44);
		stream.writeByte128(speed2);
		stream.writeByte128(speed1);
		stream.writeByte(moveLocalY);
		stream.writeShort(moveZ >> 2);
		stream.writeByte(moveLocalX);
		session.write(stream);
	}
	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ) {
		sendCameraPos(moveLocalX, moveLocalY, moveZ, -1, -1);
	}

	/**
	 * This will blackout specified area.
	 * 
	 * @param byte area = area which will be blackout (0 = unblackout; 1 =
	 *        blackout orb; 2 = blackout map; 5 = blackout orb and map)
	 */
	public void sendBlackOut(int area) {
		OutputStream out = new OutputStream(3);
		out.writePacket(208);
		out.writeByte(area);
		session.write(out);
	}
/*
	public void sendGlobalString(int id, String string) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(131);
		stream.writeShort(id);
		stream.writeString(string);
		stream.writeShortLE128(ID++);
		stream.endPacketVarByte();
		session.write(stream);

	}*/
	public void sendGlobalString(int SpaceId, String string) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(131);
		stream.writeString(string);
		stream.writeShort(SpaceId);
		//stream.writeShortLE128(ID++);
		stream.endPacketVarByte();
		session.write(stream);
	}
	/*
	public void sendGlobalString(int id, String string) {
		OutputStream stream = new OutputStream();
		if(string.length() > 253) {
			stream.writePacketVarShort(31);
			stream.writeString(string);
			stream.writeShort(id);
			stream.endPacketVarShort();
		}else{
			stream.writePacketVarByte(131);
			stream.writeShort(id);
			stream.writeString(string);
			stream.endPacketVarByte();
		}
		session.write(stream);
	}*/
/*
	public void sendObjectAnimation(WorldObject object, Animation animation) {
		OutputStream stream = new OutputStream(8);
		stream.writePacket(110);
		stream.writeIntLE(object.get30BitsLocationHash());
		stream.writeShort128(animation.getIds()[0]);
		stream.writeShortLE128((object.getType() << 1894042274 ^ 0xffffffff)
				+ (object.getRotation() & 0x3));
		session.write(stream);
	}*/
	/*public void newObjectAnimation(Location loc, int anim) {
		sendCoords(Location.location(loc.getX(), loc.getY(), player.getLocation().getZ()));
		int type = 10;
		int x = loc.getX();
		int face = 0;
		if (anim == 497) { // Agility ropeswing
			face = x == 2551 ? 4 : x == 3005 ? 2 : 0;
		}
		player.getSession().write(new StaticPacketBuilder().setId(20)
		.addByteS(0)
		.addByteS((byte) ((type << 2) + (face & 3)))
		.addLEShort(anim).toPacket());
	}*/
	
	public void sendObjectAnimation(WorldObject object, Animation animation) {
		OutputStream stream = new OutputStream(10);
		stream.writePacket(109);
		stream.writeInt(animation.getIds()[0]);
		stream.writeByteC((object.getType() << 2)
				+ (object.getRotation() & 0x3));
		stream.writeShortLE128(object.getTileHash());
		session.write(stream);
	}
	
	/*public void sendObjectAnimation(WorldObject object, Animation animation) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(109);
		stream.writeShort128(animation.getIds()[0]);
		stream.writeIntLE( object.getType() <<  -1894042274 ^ 0xffffffff);
		stream.writeShortLE128(object.getRotation() & 0x3 >> 443535132);
		stream.writeShortLE128(object.getTileHash());
		session.write(stream);
	}*/

	public void sendResetCamera() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(147);
		session.write(stream);
	}

	public void sendInterFlashScript(int interfaceId,
			int componentId, int width, int height, int slot) {
		Object[] parameters = new Object[4];
		int index = 0;
		parameters[index++] = slot;
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(143, parameters); 
	}

	public void sendIComponentText(String string, int componentId, int interfaceId) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(106);
		stream.writeShortLE128(interPacketsCount++);
		stream.writeString(string);
		stream.writeIntV2(interfaceId << 16 | componentId);
		stream.endPacketVarShort();
		session.write(stream);
		
	}
	public void sendClientVarp1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(110);
		stream.writeShortLE128(id);
		stream.writeByte128(value);
		session.write(stream);
	}

	public void sendClientVarp2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(56);
		stream.writeShort128(id);
		stream.writeIntLE(value);
		session.write(stream);
	}

	public void sendClientVarpBit1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(111);
		stream.writeShort128(id);
		stream.writeByteC(value);
		session.write(stream);
	}

	public void sendClientVarpBit2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(81);
		stream.writeIntV1(value);
		stream.writeShort128(id);
		session.write(stream);
	}

	public void sendSkillLevels() {
		try {
		for(int i = 0; i < Skills.SKILL_COUNT; i++) {
			sendSkillLevel(i);
		}
		} catch(Exception e) {
		}
	}
	/*
	public void sendClanChat(Player p2, String from, String clanName, String message) {
		int id = messageCounter;
		if(id > 16000000) {
	    			id = 1;
		}
		byte[] bytes = new byte[256];
		bytes[0] = (byte) message.length();
		int len = Misc.encryptPlayerChat(bytes, 0, 1, message.length(), message.getBytes())+1;
		StaticPacketBuilder spb = new StaticPacketBuilder().setId(137).setSize(Size.VariableByte);
		spb.addByte((byte) 1);
		spb.addString(Misc.upper(from).replaceAll("_", " "));
		spb.addString(Misc.upper(from).replaceAll("_", " "));
		spb.addLong(Misc.stringToLong(clanName));
		spb.addShort(p2.getIndex());
		spb.addByte((byte) ((id << 16) & 0xFF));
		spb.addByte((byte) ((id << 8) & 0xFF));
		spb.addByte((byte) ((id) & 0xFF));;
		spb.addByte((byte) p2.getRights());
		spb.addBytes(bytes, 0, len);
		messageCounter++;
		player.getSession().write(spb.toPacket());
    }
	*/
}
