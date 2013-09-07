package com.rs.net.encoders;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.cache.Cache;
import com.rs.io.OutputStream;
import com.rs.net.Session;
import com.rs.utils.Utils;

public final class GrabPacketsEncoder extends Encoder {
	
	private static byte[] UKEYS_FILE;
	public static final OutputStream getUkeysFile() {
		if(UKEYS_FILE == null)
			UKEYS_FILE = Cache.generateUkeysContainer();
		return getContainerPacketData(255, 255, 0, UKEYS_FILE);
		
	}
	public GrabPacketsEncoder(Session connection) {
		super(connection);
	}
	
	public final void sendOutdatedClientPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(6);
		ChannelFuture future = session.write(stream);
		if(future != null) 
			future.addListener(ChannelFutureListener.CLOSE);
		else
			session.getChannel().close();
	}
	
	public final void sendStartUpPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(0);
		session.write(stream);
	}
	
	public final void sendCacheContainer(int indexId, int containerId, boolean priority) {
		if(indexId == 255 && containerId == 255)
			session.write(getUkeysFile());
		else {
			OutputStream stream = getContainerPacketData(indexId, containerId, priority);
			if(stream == null)
				return;
			//session.write(stream);
		}
	}
	
	public final OutputStream getContainerPacketData(int indexFileId, int containerId, boolean priority) {
		if(!priority) //just for now
			return null;
		byte[] container = indexFileId == 255 ? Cache.getConstainersInformCacheFile().getContainerData(containerId) : Cache.getCacheFileManagers()[indexFileId].getCacheFile().getContainerData(containerId);
		if(container == null)
			return null;
		if(!priority)
			container[0] |= 0x80;
		OutputStream stream = new OutputStream(container.length+3);
		stream.writeByte(indexFileId);
		stream.writeShort(containerId);
		for(int index = 0; index < 5; index++)
			stream.writeByte(container[index]);
		int offset = 8;
		for(int index = 5; index < container.length; index++) {
			 if(offset == 512) {
				 session.write(stream);
				 stream = new OutputStream();
				 stream.writeByte(-1);
                 offset = 1;
             }
			 stream.writeByte(container[index]);
			 offset++;
		}
		 session.write(stream);
		return stream;
	}
	
	/*
	* only using for ukeys atm, doesnt allow keys encode
	*/
	public static final OutputStream getContainerPacketData(int indexFileId, int containerId, int compression, byte[] unpackedContainer) {
		OutputStream stream = new OutputStream(unpackedContainer.length+4);
		stream.writeByte(indexFileId);
		stream.writeShort(containerId);
		byte[] container = Utils.packContainer(unpackedContainer, compression);
		for(int index = 0; index < 5; index++)
			stream.writeByte(container[index]);
		int offset = 8;
		for(int index = 5; index < container.length; index++) {
			 if(offset == 512) {
				 stream.writeByte(-1);
                 offset = 1;
             }
			 stream.writeByte(container[index]);
			 offset++;
		}
		return stream;
	}

}
