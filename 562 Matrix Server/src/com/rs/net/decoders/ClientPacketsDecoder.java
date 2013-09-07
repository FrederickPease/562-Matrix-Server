package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.io.InputStream;
import com.rs.net.Session;
import com.rs.utils.Logger;

public final class ClientPacketsDecoder extends Decoder {

	public ClientPacketsDecoder(Session connection) {
		super(connection);
	}

	@Override
	public final void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		switch(packetId) {
		case 14:
			decodeLogin(stream);
		break;
		case 15:
			decodeGrab(stream);
		break;
		default:
			Logger.log(this, "PacketId "+packetId);
			session.getChannel().close();
		break;
		}
	}
	
	private final void decodeLogin(InputStream stream) {
		if(stream.getRemaining() != 1) {
			session.getChannel().close();
			return;
		}
		session.setDecoder(2);
		((LoginPacketsDecoder)session.getDecoder()).setNameHash(stream.readUnsignedByte());
		session.setEncoder(1);
		session.getLoginPackets().sendStartUpPacket();
	}
	
	private final void decodeGrab(InputStream stream) {
		if(stream.getRemaining() != 4) {
			session.getChannel().close();
			return;
		}
		session.setEncoder(0);
		if(stream.readInt() != Settings.CLIENT_REVISION) {
			session.setDecoder(-1);
			session.getGrabPackets().sendOutdatedClientPacket();
			return;
		}
		session.setDecoder(1);
		session.getGrabPackets().sendStartUpPacket();
	}
}
