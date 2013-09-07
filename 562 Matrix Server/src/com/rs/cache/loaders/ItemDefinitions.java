package com.rs.cache.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;

import com.rs.cache.Cache;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.io.InputStream;
import com.rs.utils.Utils;

@SuppressWarnings("unused")
public final class ItemDefinitions {

	private static final ItemDefinitions[] itemsDefinitions;

	static { // that's why this is here
		itemsDefinitions = new ItemDefinitions[Utils.getItemDefinitionsSize()];
	}

	public int id;
	private boolean loaded;

	private int modelId;
	private String name;

	// model size information
	private int modelZoom;
	private int modelRotation1;
	private int modelRotation2;
	private int modelOffset1;
	private int modelOffset2;

	// extra information
	private int stackable;
	private int value;
	private boolean membersOnly;

	// wearing model information
	private int maleEquip1;
	private int femaleEquip1;
	private int maleEquip2;
	private int femaleEquip2;

	// options
	private String[] groundOptions;
	public String[] inventoryOptions;

	// model information
	private short[] originalModelColors;
	private short[] modifiedModelColors;
	private short[] textureColour1;
	private short[] textureColour2;
	private byte[] unknownArray1;
	private int[] unknownArray2;
	// extra information, not used for newer items
	private boolean unnoted;

	private int colourEquip1;
	private int colourEquip2;
	private int unknownInt1;
	private int unknownInt2;
	private int unknownInt3;
	private int unknownInt4;
	private int unknownInt5;
	private int unknownInt6;
	private int certId;
	private int certTemplateId;
	private int[] stackIds;
	private int[] stackAmounts;
	private int unknownInt7;
	private int unknownInt8;
	private int unknownInt9;
	private int unknownInt10;
	private int unknownInt11;
	private int teamId;
	private int lendId;
	private int lendTemplateId;
	private int unknownInt12;
	private int unknownInt13;
	private int unknownInt14;
	private int unknownInt15;
	private int unknownInt16;
	private int unknownInt17;
	private int unknownInt18;
	private int unknownInt19;
	private int unknownInt20;
	private int unknownInt21;
	private int unknownInt22;
	private int unknownInt23;
	private int equipType;

	// extra added
	private boolean noted;

	private HashMap<Integer, Object> clientScriptData;

	public static final ItemDefinitions getItemDefinitions(int itemId) {
		if (itemId < 0 || itemId >= itemsDefinitions.length)
			return null;
		ItemDefinitions def = itemsDefinitions[itemId];
		if (def == null)
			itemsDefinitions[itemId] = def = new ItemDefinitions(itemId);
		return def;
	}

	public static final void clearItemsDefinitions() {
		for (int i = 0; i < itemsDefinitions.length; i++)
			itemsDefinitions[i] = null;
	}

	public ItemDefinitions(int id) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		loadItemDefinitions();
	}

	public boolean isLoaded() {
		return loaded;
	}

	private final void loadItemDefinitions() {
		byte[] data = Cache.getCacheFileManagers()[19].getFileData(
				getContainerId(), getFileId());
		if (data == null) {
			// System.out.println("Failed loading Item " + id+".");
			return;
		}
		readOpcodeValues(new InputStream(data));
		if (certTemplateId != -1)
			toNote();
		if (lendTemplateId != -1)
			toLend();
		loaded = true;
	}

	private void toNote() {
		// ItemDefinitions noteItem; //certTemplateId
		ItemDefinitions realItem = getItemDefinitions(certId);
		membersOnly = realItem.membersOnly;
		value = realItem.value;
		name = realItem.name;
		stackable = 1;
		noted = true;
	}

	private void toLend() {
		// ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getItemDefinitions(lendId);
		originalModelColors = realItem.originalModelColors;
		colourEquip1 = realItem.colourEquip1;
		colourEquip2 = realItem.colourEquip2;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		equipType = realItem.equipType;
	}

	public int getContainerId() {
		return id >>> 8;
	}

	public int getFileId() {
		return 0xff & id;
	}

	public boolean isDestroyItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("destroy"))
				return true;
		}
		return false;
	}

	public boolean isWearItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("wield")
					|| option.equalsIgnoreCase("wear"))
				return true;
		}
		return false;
	}

	public boolean hasSpecialBar() {
		if (clientScriptData == null)
			return false;
		Object specialBar = clientScriptData.get(686);
		if (specialBar != null && specialBar instanceof Integer)
			return (Integer) specialBar == 1;
		return false;
	}

	public int getRenderAnimId() {
		if (clientScriptData == null)
			return 1426;
		Object animId = clientScriptData.get(644);
		if (animId != null && animId instanceof Integer)
			return (Integer) animId;
		return 1426;
	}

	public int getQuestId() {
		if (clientScriptData == null)
			return -1;
		Object questId = clientScriptData.get(861);
		if (questId != null && questId instanceof Integer)
			return (Integer) questId;
		return -1;
	}

	public HashMap<Integer, Integer> getWearingSkillRequiriments() {
		if (clientScriptData == null)
			return null;
		HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
		int nextLevel = -1;
		int nextSkill = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if (value instanceof String)
				continue;
			if (key == 23) {
				skills.put(4, (Integer) value);
				skills.put(11, 61);
			} else if (key >= 749 && key < 797) {
				if (key % 2 == 0)
					nextLevel = (Integer) value;
				else
					nextSkill = (Integer) value;
				if (nextLevel != -1 && nextSkill != -1) {
					skills.put(nextSkill, nextLevel);
					nextLevel = -1;
					nextSkill = -1;
				}
			}

		}
		return skills;
	}

	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}

	private void setDefaultsVariableValues() {
		name = "null";
		maleEquip1 = -1;
		maleEquip2 = -1;
		femaleEquip1 = -1;
		femaleEquip2 = -1;
		modelZoom = 2000;
		lendId = -1;
		lendTemplateId = -1;
		certId = -1;
		certTemplateId = -1;
		unknownInt9 = 128;
		value = 1;
		colourEquip1 = -1;
		colourEquip2 = -1;
		equipType = -1;
	}

	private final void readValues(InputStream stream, int opcode) {
		if (opcode == 1)
			modelId = stream.readUnsignedShort();
		else if (opcode == 2)
			name = stream.readString();
		else if (opcode == 4)
			modelZoom = stream.readUnsignedShort();
		else if (opcode == 5)
			modelRotation1 = stream.readUnsignedShort();
		else if (opcode == 6)
			modelRotation2 = stream.readUnsignedShort();
		else if (opcode == 7) {
			modelOffset1 = stream.readUnsignedShort();
			if (modelOffset1 > 32767)
				modelOffset1 -= 65536;
			modelOffset1 <<= 0;

		} else if (opcode == 8) {
			modelOffset2 = stream.readUnsignedShort();
			if (modelOffset2 > 32767)
				modelOffset2 -= 65536;
			modelOffset2 <<= 0;
		} else if (opcode == 11)
			stackable = 1;
		else if (opcode == 12)
			value = stream.readInt();
		else if (opcode == 14)
			equipType = stream.readUnsignedByte();
		else if (opcode == 16)
			membersOnly = true;
		else if (opcode == 23)
			maleEquip1 = stream.readUnsignedShort();
		else if (opcode == 24)
			femaleEquip1 = stream.readUnsignedShort();
		else if (opcode == 25)
			maleEquip2 = stream.readUnsignedShort();
		else if (opcode == 26)
			femaleEquip2 = stream.readUnsignedShort();
		else if (opcode >= 30 && opcode < 35)
			groundOptions[opcode - 30] = stream.readString();
		else if (opcode >= 35 && opcode < 40)
			inventoryOptions[opcode - 35] = stream.readString();
		else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new short[length];
			modifiedModelColors = new short[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = (short) stream.readUnsignedShort();
				modifiedModelColors[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			textureColour1 = new short[length];
			textureColour2 = new short[length];
			for (int index = 0; index < length; index++) {
				textureColour1[index] = (short) stream.readUnsignedShort();
				textureColour2[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			unknownArray1 = new byte[length];
			for (int index = 0; index < length; index++)
				unknownArray1[index] = (byte) stream.readByte();
		} else if (opcode == 65)
			unnoted = true;
		else if (opcode == 78)
			colourEquip1 = stream.readUnsignedShort();
		else if (opcode == 79)
			colourEquip2 = stream.readUnsignedShort();
		else if (opcode == 90)
			unknownInt1 = stream.readUnsignedShort();
		else if (opcode == 91)
			unknownInt2 = stream.readUnsignedShort();
		else if (opcode == 92)
			unknownInt3 = stream.readUnsignedShort();
		else if (opcode == 93)
			unknownInt4 = stream.readUnsignedShort();
		else if (opcode == 95)
			unknownInt5 = stream.readUnsignedShort();
		else if (opcode == 96)
			unknownInt6 = stream.readUnsignedByte();
		else if (opcode == 97)
			certId = stream.readUnsignedShort();
		else if (opcode == 98)
			certTemplateId = stream.readUnsignedShort();
		else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = stream.readUnsignedShort();
			stackAmounts[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110)
			unknownInt7 = stream.readUnsignedShort();
		else if (opcode == 111)
			unknownInt8 = stream.readUnsignedShort();
		else if (opcode == 112)
			unknownInt9 = stream.readUnsignedShort();
		else if (opcode == 113)
			unknownInt10 = stream.readByte();
		else if (opcode == 114)
			unknownInt11 = stream.readByte() * 5;
		else if (opcode == 115)
			teamId = stream.readUnsignedByte();
		else if (opcode == 121)
			lendId = stream.readUnsignedShort();
		else if (opcode == 122)
			lendTemplateId = stream.readUnsignedShort();
		else if (opcode == 125) {
			unknownInt12 = stream.readByte() << 0;
			unknownInt13 = stream.readByte() << 0;
			unknownInt14 = stream.readByte() << 0;
		} else if (opcode == 126) {
			unknownInt15 = stream.readByte() << 0;
			unknownInt16 = stream.readByte() << 0;
			unknownInt17 = stream.readByte() << 0;
		} else if (opcode == 127) {
			unknownInt18 = stream.readUnsignedByte();
			unknownInt19 = stream.readUnsignedShort();
		} else if (opcode == 128) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 129) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 130) {
			unknownInt22 = stream.readUnsignedByte();
			unknownInt23 = stream.readUnsignedShort();
		} else if (opcode == 132) {
			int length = stream.readUnsignedByte();
			unknownArray2 = new int[length];
			for (int index = 0; index < length; index++)
				unknownArray2[index] = stream.readUnsignedShort();
		} else if (opcode == 139) {
			int unknownValue = stream.readUnsignedShort();
		} else if (opcode == 140) {
			int unknownValue = stream.readUnsignedShort();
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (clientScriptData == null)
				clientScriptData = new HashMap<Integer, Object>(length);
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream
						.readInt();
				clientScriptData.put(key, value);
			}
		} else
			throw new RuntimeException("MISSING OPCODE " + opcode
					+ " FOR ITEM " + id);
	}

	private final void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public String getName() {
		return name;
	}

	public int getMaleWornModelId1() {
		return maleEquip1;
	}

	public int getMaleWornModelId2() {
		return maleEquip2;
	}

	public boolean isStackable() {
		return stackable == 1;
	}

	public boolean isNoted() {
		return noted;
	}

	public int getCertId() {
		return certId;
	}

	public int getEquipType() {
		return equipType;
	}

	public static class ItemPrice {
		private int minPrice;
		private int maxPrice;
		private int normPrice;

		public int getMinimumPrice() {
			return minPrice;
		}

		public int getMaximumPrice() {
			return maxPrice;
		}

		public int getNormalPrice() {
			return normPrice;
		}
	}

	public int getNormalPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public HashMap<Integer, Integer> getCreateItemRequirements() {
		if (clientScriptData == null)
			return null;
		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if (value instanceof String)
				continue;
			if (key >= 538 && key <= 770) {
				if (key % 2 == 0)
					requiredId = (Integer) value;
				else
					requiredAmount = (Integer) value;
				if (requiredId != -1 && requiredAmount != -1) {
					items.put(requiredAmount, requiredId);
					requiredId = -1;
					requiredAmount = -1;
				}
			}
		}
		return items;
	}

	public int getId() {
		return this.id;
	}

	public static ItemDefinitions getItemID(Player player, String name) {
		int count = 0;
		for (ItemDefinitions definition : itemsDefinitions) {
			name = name.toLowerCase();
			String output = definition.name.toLowerCase();
			int itemId = definition.getId();
			ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
			if (output.contains(name)) {
				count++;
				String results = "" + count + " - <col=007FFF>"
						+ defs.getName() + "</col> (<col=00FF3E>"
						+ definition.getId() + "</col>) Worth: "
						+ defs.getValue() + " GP";
				player.getPackets().sendPanelBoxMessage("" + results + "");
				if (count >= 500) {
					player.getPackets().sendPanelBoxMessage(
							"Found " + count + "+ results for '" + name
									+ "'. Only 500 listed.");
					return definition;
				}
				// return definition; // Will stop the method on first item
				// found >.>
			}
		}
		player.getPackets().sendPanelBoxMessage(
				"Found " + count + " results for '" + name + "'");
		return null;
	}

	public int getGEPrice() {
		try {
			for (String lines : FileUtilities
					.readFile("./data/items/grand_exchange/tipit_dump.txt")) {
				String[] data = lines.split(" - ");
				if (Integer.parseInt(data[0]) == id)
					return Integer.parseInt(data[1]);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	// hehe lil cheap shit :p
	public static class FileUtilities {

		public static final int BUFFER = 1024;

		public static boolean exists(String name) {

			File file = new File(name);

			return file.exists();

		}

		public static ByteBuffer fileBuffer(String name) throws IOException {

			File file = new File(name);

			if (!file.exists())

				return null;

			FileInputStream in = new FileInputStream(name);

			byte[] data = new byte[BUFFER];

			int read;

			try {

				ByteBuffer buffer = ByteBuffer.allocate(in.available() + 1);

				while ((read = in.read(data, 0, BUFFER)) != -1) {

					buffer.put(data, 0, read);

				}

				buffer.flip();

				return buffer;

			} finally {

				if (in != null)

					in.close();

				in = null;

			}

		}

		public static void writeBufferToFile(String name, ByteBuffer buffer)
				throws IOException {

			File file = new File(name);

			if (!file.exists())

				file.createNewFile();

			FileOutputStream out = new FileOutputStream(name);

			out.write(buffer.array(), 0, buffer.remaining());

			out.flush();

			out.close();

		}

		public static LinkedList<String> readFile(String directory)
				throws IOException {

			LinkedList<String> fileLines = new LinkedList<String>();

			BufferedReader reader = null;

			try {

				reader = new BufferedReader(new FileReader(directory));

				String string;

				while ((string = reader.readLine()) != null) {

					fileLines.add(string);

				}

			} finally {

				if (reader != null) {

					reader.close();

					reader = null;

				}

			}

			return fileLines;

		}

	}

	private transient AlchType alchType;

	public enum AlchType {
		HIGH, LOW
	}

	public AlchType getAlchType() {
		if (alchType == null)
			return null;
		return alchType;
	}

	public void setAlchType(AlchType alchType) {
		this.alchType = alchType;
	}

	public boolean containsOption(int i, String option) {
		if (inventoryOptions == null || inventoryOptions[i] == null
				|| inventoryOptions.length <= i)
			return false;
		return inventoryOptions[i].equals(option);
	}

	public boolean containsOption(String option) {
		if (inventoryOptions == null)
			return false;
		for (String o : inventoryOptions) {
			if (o == null || !o.equals(option))
				continue;
			return true;
		}
		return false;
	}

}
