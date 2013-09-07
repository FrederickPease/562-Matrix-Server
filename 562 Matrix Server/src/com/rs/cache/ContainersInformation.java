package com.rs.cache;

import java.util.zip.CRC32;

import com.rs.io.InputStream;
import com.rs.utils.Utils;

public final class ContainersInformation {

	private Container informationContainer;
	private int protocol;
	private int revision;
	private int[] containersIndexes;
	private FilesContainer[] containers;
	private boolean filesNamed;
	private boolean whirpool;
	
	public ContainersInformation(byte[] informationContainerPackedData) {
		informationContainer = new Container();
		informationContainer.setVersion((informationContainerPackedData[informationContainerPackedData.length - 2] << 8 & 0xff00) + (informationContainerPackedData[-1 + informationContainerPackedData.length] & 0xff));
		CRC32 crc32 = new CRC32();
		crc32.update(informationContainerPackedData);
		informationContainer.setCrc((int) crc32.getValue());
		decodeContainersInformation(Utils.unpackCacheContainer(informationContainerPackedData));
	}
	
	public int[] getContainersIndexes() {
		return containersIndexes;
	}
	
	public FilesContainer[] getContainers() {
		return containers;
	}
	public Container getInformationContainer() {
		return informationContainer;
	}
	
	public int getRevision() {
		return revision;
	}

	public void decodeContainersInformation(byte[] data) {
		InputStream stream = new InputStream(data);
		protocol = stream.readUnsignedByte();
		if (protocol != 5 && protocol != 6)
			throw new RuntimeException();
		revision = protocol < 6 ? 0 : stream.readInt();
		
		int nameHash = stream.readUnsignedByte();
		filesNamed = (0x1 & nameHash) != 0;
		whirpool = (0x2 & nameHash) != 0;
		containersIndexes = new int[stream.readUnsignedShort()];
		int lastIndex = -1;
		for (int index = 0; index < containersIndexes.length; index++) {
			containersIndexes[index] = stream.readShort()/*stream.readUnsignedShort()*/ + (index == 0 ? 0 : containersIndexes[index-1]);
			if (containersIndexes[index] > lastIndex)
				lastIndex = containersIndexes[index];
		}
		containers = new FilesContainer[lastIndex+1];
		for(int index = 0; index < containersIndexes.length; index++)
			containers[containersIndexes[index]] = new FilesContainer();
		if(filesNamed)
			for(int index = 0; index < containersIndexes.length; index++)
				containers[containersIndexes[index]].setNameHash(stream.readInt());
		byte[][] filesHashes = null;
		if(whirpool) {
			filesHashes = new byte[containers.length][];
			for(int index = 0; index < containersIndexes.length; index++) {
				filesHashes[containersIndexes[index]] = new byte[64];	
				stream.readBytes(filesHashes[containersIndexes[index]], 0, 64);
			}
		}
		for(int index = 0; index < containersIndexes.length; index++)
			containers[containersIndexes[index]].setCrc(stream.readInt());
		for(int index = 0; index < containersIndexes.length; index++)
			containers[containersIndexes[index]].setVersion(stream.readInt());
		for(int index = 0; index < containersIndexes.length; index++)
			containers[containersIndexes[index]].setFilesIndexes(new int[stream.readUnsignedShort()]);
		for (int index = 0; index < containersIndexes.length; index++) {
			int lastFileIndex = -1;
			for(int fileIndex = 0; fileIndex < containers[containersIndexes[index]].getFilesIndexes().length; fileIndex++) {
				containers[containersIndexes[index]].getFilesIndexes()[fileIndex] = stream.readShort()/*stream.readUnsignedShort()*/ + (fileIndex == 0 ? 0 : containers[containersIndexes[index]].getFilesIndexes()[fileIndex-1]);
				if (containers[containersIndexes[index]].getFilesIndexes()[fileIndex] > lastFileIndex)
					lastFileIndex = containers[containersIndexes[index]].getFilesIndexes()[fileIndex];
			}
			containers[containersIndexes[index]].setFiles(new Container[lastFileIndex+1]);
			for(int fileIndex = 0; fileIndex < containers[containersIndexes[index]].getFilesIndexes().length; fileIndex++)
				containers[containersIndexes[index]].getFiles()[containers[containersIndexes[index]].getFilesIndexes()[fileIndex]] = new Container();
		}
		if(whirpool)
			for(int index = 0; index < containersIndexes.length; index++)
				for(int fileIndex = 0; fileIndex < containers[containersIndexes[index]].getFilesIndexes().length; fileIndex++)
					containers[containersIndexes[index]].getFiles()[containers[containersIndexes[index]].getFilesIndexes()[fileIndex]].setVersion(filesHashes[containersIndexes[index]][containers[containersIndexes[index]].getFilesIndexes()[fileIndex]]);
		if(filesNamed)
			for (int index = 0; index < containersIndexes.length; index++)
				for(int fileIndex = 0; fileIndex < containers[containersIndexes[index]].getFilesIndexes().length; fileIndex++)
					containers[containersIndexes[index]].getFiles()[containers[containersIndexes[index]].getFilesIndexes()[fileIndex]].setNameHash(stream.readInt());
	}
	
	public boolean isWhirpool() {
		return whirpool;
	}
	
}
