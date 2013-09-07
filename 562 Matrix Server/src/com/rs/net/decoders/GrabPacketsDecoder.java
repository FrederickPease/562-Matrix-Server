package com.rs.net.decoders;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.Cache;
import com.rs.io.InputStream;
import com.rs.net.Session;

public final class GrabPacketsDecoder extends Decoder {

	public GrabPacketsDecoder(Session connection) {
		super(connection);
		requestedPriorityFiles = new CopyOnWriteArrayList<String>();
		requestedFiles = new CopyOnWriteArrayList<String>();
		
	}

	private CopyOnWriteArrayList<String> requestedPriorityFiles;
	private CopyOnWriteArrayList<String> requestedFiles;
	
	public CopyOnWriteArrayList<String> getRequestedPriorityFiles() {
		return requestedPriorityFiles;
	}
	
	public CopyOnWriteArrayList<String> getRequestedFiles() {
		return requestedFiles;
	}
	
	
	@Override
	public final void decode(InputStream stream) {
		while(stream.getRemaining() > 0 && session.getChannel().isConnected()) {
			int packetId = stream.readUnsignedByte();
		//	Logger.log(this, "PacketId "+packetId);
			if (packetId == 0 || packetId == 1)
				decodeRequestCacheContainer(stream, packetId == 1);
			else
				decodeOtherPacket(stream, packetId);
		}
	}
	
	private final void decodeRequestCacheContainer(InputStream stream, boolean priority) {
		int indexId = stream.readUnsignedByte();
		int containerId = stream.readUnsignedShort();
		if(indexId != 255) {
			if (Cache.getCacheFileManagers().length <= indexId
					|| Cache.getCacheFileManagers()[indexId] == null
					|| !Cache.getCacheFileManagers()[indexId].validContainer(containerId))
			return;
		}else if (containerId != 255)
			if (Cache.getCacheFileManagers().length <= containerId
					|| Cache.getCacheFileManagers()[containerId] == null)
				return;
		String fileName = indexId+"_"+containerId;
		/*if(requestedPriorityFiles.contains(fileName))
			return;*/
		if(requestedFiles.contains(fileName)) {
			if(priority) {
				requestedFiles.remove(fileName);
				requestedPriorityFiles.add(fileName);
			}
			return;
		}
		//if(priority) {
		//	requestedPriorityFiles.add(fileName);
			session.getGrabPackets().sendCacheContainer(indexId, containerId, priority);
		/*}else
			requestedFiles.add(fileName);*/
		//session.getGrabPackets().sendCacheContainer(indexId, containerId, priority);
			
		
	}

	private final void decodeOtherPacket(InputStream stream, int packetId) {
		if (stream.getRemaining() < 3 || packetId == 7) {
			session.getChannel().close();
			return;
		}
		stream.skip(3);
	}

	
}
