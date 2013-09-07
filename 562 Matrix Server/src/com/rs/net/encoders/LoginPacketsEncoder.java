package com.rs.net.encoders;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.game.player.Player;
import com.rs.io.OutputStream;
import com.rs.net.Session;

public final class LoginPacketsEncoder extends Encoder {

	public LoginPacketsEncoder(Session connection) {
		super(connection);
	}
	
	public final void sendStartUpPacket() {
		OutputStream stream = new OutputStream(9);
		stream.writeByte(0);
		stream.writeLong(((long) (java.lang.Math.random() * 99999999D) << 32) + (long) (java.lang.Math.random() * 99999999D));
		session.write(stream);
	}

	public final void sendClientPacket(int opcode) {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(opcode);
		ChannelFuture future = session.write(stream);
		if(future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		}else{
			session.getChannel().close();
		}
	}
	
	public final void sendLoginDetails(Player player) {
		OutputStream stream = new OutputStream(12);
		stream.writeByte(2);
		stream.writeByte(player.getRights());
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(1);
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeShort(player.getIndex());
		stream.writeByte(1);
		stream.writeByte(1);
		session.write(stream);
	}
	

}
