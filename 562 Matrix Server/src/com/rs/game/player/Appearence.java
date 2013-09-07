package com.rs.game.player;

import java.io.Serializable;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.io.OutputStream;
import com.rs.utils.Utils;

public class Appearence implements Serializable {

	private static final long serialVersionUID = 7655608569741626586L;
	private short transformedNpcId;
	private int renderEmote;
	public int[] look;
	public int gender = 0;
	public int[] colour;
	private boolean male;
	
	//private transient boolean hidePlayer;
	private transient byte[] appeareanceData;
	private transient byte[] md5AppeareanceDataHash;
	
	private transient Player player;
	
	public Appearence() {
		male = true;
		gender = 0;
		transformedNpcId = -1;
		renderEmote = -1;
		resetAppearence();
	}
	
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void transformIntoNPC(int id) {
		transformedNpcId = (short) id;
		generateAppearenceData();
	}
	
	public void generateAppearenceData() {
		OutputStream stream = new OutputStream();
		int flag = 0;
		if(!male)
			flag |= 0x1;
		if(player.hasDisplayName())
			flag |= 0x2;
		//flag |= 0x4; //the way combat lvl sent
	//	flag |= value << 3 & 0x7; //dunno
	//	flag |= (1 & 0xeb) << 6; //dunno
		stream.writeByte(flag);
		stream.writeByte(-1); // mobi arms titles
		stream.writeByte(player.hasSkull() ? 0 : -1); // pk icon
		stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
		//stream.writeByte(hidePlayer ? 1 : 0);
		//npc
		if(transformedNpcId >= 0) {
			stream.writeShort(-1); //65535 tells it a npc
			stream.writeShort(transformedNpcId);
			stream.writeByte(0);	
		}else{
			for (int index = 0; index < 4; index++) {
				Item item = player.getEquipment().getItems().get(index);
				if (item == null)
					stream.writeByte(0);
				else
					stream.writeShort(32768 + item.getEquipId());
			}
			Item item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			stream.writeShort(item == null ? 0x100 + look[2] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);
			if(item == null)
				stream.writeByte(0);
			else
				stream.writeShort(32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			if(item == null || !Equipment.isFullBody(item))
				stream.writeShort(0x100 + look[3]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
			stream.writeShort(item == null ? 0x100 + look[5] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
		/*	//tits for female, bear for male
			item = player.getEquipment().getItems().get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
			if (item == null || (male && Equipment.showBear(item)))
				stream.writeShort(0x100 + look[1]);
			else
				stream.writeByte(0);*/
			if(item == null || (!Equipment.isFullMask(item) && !Equipment.isFullHat(item)))
				stream.writeShort(0x100 + look[0]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
			stream.writeShort(item == null ? 0x100 + look[4] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
			stream.writeShort(item == null ? 0x100 + look[6] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
		/*	if(item == null || !Equipment.isFullMask(item))
				stream.writeShort(0x100 + look[1]);
			else
				stream.writeByte(0);*/
			item = player.getEquipment().getItems().get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
			if (item == null || (male && Equipment.showBear(item)))
				stream.writeShort(0x100 + look[1]);
			else
				stream.writeByte(0);
		}
		for (int index = 0; index < 5; index++)
			stream.writeByte(colour[index]);
		stream.writeShort(getRenderEmote()); 
		stream.writeString(player.getDisplayName());
		if(player.hasDisplayName())
			stream.writeString(player.getUsername());
		boolean pvpArea = World.isPvpArea(player);
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);
		stream.writeByte(-1); //higher level acc name appears in front :P
		stream.writeShort(0); // 1 for send all emotes 1by1 used for
		//done separated for safe because of sycrozination
		byte[] appeareanceData = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
		this.appeareanceData = appeareanceData;
		md5AppeareanceDataHash = md5Hash;
	}
	
	public int getSize() {
		if(transformedNpcId >= 0)
			return NPCDefinitions.getNPCDefinitions(transformedNpcId).size;
		return 1;
	}
	
	public void setRenderEmote(int id) {
		this.renderEmote = id;
		generateAppearenceData();
	}
	public int getRenderEmote() {
		if(renderEmote >= 0)
			return renderEmote;
		if(transformedNpcId >= 0)
			return NPCDefinitions.getNPCDefinitions(transformedNpcId).renderEmote;
		return player.getEquipment().getWeaponRenderEmote();
	}
	public void resetAppearence() {
		look = new int[7];
		colour = new int[10];
		male();
	}
	public void male() {
		look[0] = 3; // Hair
		look[1] = 14; // Beard
		look[2] = 18; // Torso
		look[3] = 26; // Arms
		look[4] = 34; // Bracelets
		look[5] = 38; // Legs
		look[6] = 42; // Shoes~

		colour[2] = 16;
		colour[1] = 16;
		colour[0] = 3;
		male = true;
		gender = 0;
	}

	public void female() {
		look[0] = 48; // Hair
		look[1] = 57; // Beard
		look[2] = 57; // Torso
		look[3] = 65; // Arms
		look[4] = 68; // Bracelets
		look[5] = 77; // Legs
		look[6] = 80; // Shoes

		colour[2] = 16;
		colour[1] = 16;
		colour[0] = 3;
		male = false;
		gender = 1;
	}
	
	public byte[] getAppeareanceData() {
		return appeareanceData;
	}
	public byte[] getMD5AppeareanceDataHash() {
		return md5AppeareanceDataHash;
	}


	public void setMale(boolean male) {
		this.male = male;
	}

	public void setHairStyle(int i) {
		look[0] = i;
	}
	
	public void setTopStyle(int i) {
		look[2] = i;
	}
	
	public int getTopStyle() {
		return look[2];
	}
	
	
	public void setArmsStyle(int i) {
		look[3] = i;
	}
	
	public void setWristsStyle(int i) {
		look[4] = i;
	}

	public void setLegsStyle(int i) {
		look[5] = i;
	}

	public int getHairStyle() {
		return look[0];
	}

	public void setBeardStyle(int i) {
		look[1] = i;
	}

	public int getBeardStyle() {
		return look[1];
	}

	public void setFacialHair(int i) {
		look[1] = i;
	}

	public int getFacialHair() {
		return look[1];
	}

	public void setSkinColor(int color) {
		colour[4] = (byte) color;
	}

	public int getSkinColor() {
		return colour[4];
	}

	public void setHairColor(int color) {
		colour[0] = (byte) color;
	}
	
	public void setTopColor(int color) {
		colour[1] = (byte) color;
	}
	
	public void setLegsColor(int color) {
		colour[2] = (byte) color;
	}

	public int getHairColor() {
		return colour[0];
	}
	public int getGender() {
		return gender;
	}

	public int getLook(int id) {
		return look[id];
	}

	public int getColour(int id) {
		return colour[id];
	}

	public int[] getLooks() {
		return look;
	}

	public int[] getColours() {
		return colour;
	}


	public boolean isMale() {
		return male;
	}


	/*public void switchHidden() {
		hidePlayer = !hidePlayer;
		generateAppearenceData();
	}
	
	public boolean isHidden() {
		return hidePlayer;
	}*/
	
}
