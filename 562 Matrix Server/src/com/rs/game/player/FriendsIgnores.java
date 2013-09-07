package com.rs.game.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.rs.game.World;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class FriendsIgnores implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 39693097250367467L;
	private ArrayList<String> friends;
	@SuppressWarnings("unused")
	private ArrayList<String> ignores;
	private int ignoredMessagesCount;
	
	private transient Player player;
	
	public FriendsIgnores() {
		friends = new ArrayList<String>(200);
		ignores = new ArrayList<String>(100);
	}
	
	public int getNextIgnoredMessageCount() {
		if(ignoredMessagesCount > 16000000)
			ignoredMessagesCount = 1;
		return ignoredMessagesCount++;
	}
	public void setPlayer(Player player) {
		this.player = player;
		ignoredMessagesCount++;
	}
	
	public void changeFriendStatus(Player p2, boolean online) {
		if(!player.isRunning() || !friends.contains(p2.getUsername()))
			return;
		player.getPackets().sendFriend(Utils.formatPlayerNameForDisplay(p2.getUsername()), p2.getDisplayName(), 1, online, true);
	}
	
	public void sendFriendsMyStatus(boolean online) {
		for(Player p2 : World.getPlayers())
			p2.getFriendsIgnores().changeFriendStatus(player, online);
	}
	
	public void sendMessage(Player p2, String message) {
		player.getPackets().sendPrivateMessage(p2.getDisplayName(), message);
		p2.getPackets().receivePrivateMessage(player.getUsername(), player.getDisplayName(), player.getRights(), message);
	}
	
	public void addFriend(String username) {
		if (friends.size() >= 200) {
			player.getPackets().sendGameMessage("Your friends list is full.");
			return;
		}
		if(username.equals(player.getUsername())) {
			player.getPackets().sendGameMessage("You can't add yourself.");
			return;
		}
		Player p2 = World.getPlayerByDisplayName(username);
		String formatedUsername = Utils.formatPlayerNameForProtocol(username);
		if(p2 == null) {
			p2 = SerializableFilesManager.loadPlayer(formatedUsername);
			if(p2 != null)
				p2.setUsername(formatedUsername);
			else {
				player.getPackets().sendGameMessage("The friend you tried to add is invalid.");
				return;
			}
		}
		if (friends.contains(p2.getUsername())) {
			player.getPackets().sendGameMessage((username) + " is already on your friends list.");
			return;
		}
		friends.add(p2.getUsername());
		player.getPackets().sendFriend(Utils.formatPlayerNameForDisplay(p2.getUsername()), p2.getDisplayName(), 1, p2.isRunning(), p2.isRunning() ? true : false);
	}
	
	public void removeFriend(String username) {
		String formatedUsername = Utils.formatPlayerNameForProtocol(username);
		if(!friends.remove(formatedUsername)) {
			Player p2 = World.getPlayerByDisplayName(username);
			if(p2 == null)
				return;
			friends.remove(p2.getUsername());
		}
	}

	public void init() {
		player.getPackets().sendUnlockFriendList();
		List<String> removeFriends = new ArrayList<String>();
		for(String username : friends) {
			Player p2 = World.getPlayer(username);
			if(p2 == null) {
				p2 = SerializableFilesManager.loadPlayer(username);
				if(p2 != null)
					p2.setUsername(username);
				else {
					removeFriends.add(username);
					continue;
				}
			}
			player.getPackets().sendFriend(Utils.formatPlayerNameForDisplay(p2.getUsername()), p2.getDisplayName(), 1, p2.isRunning(), false);
		}
		for(String username : removeFriends) {
			friends.remove(username);
		}
		sendFriendsMyStatus(true);
	}
	
}
