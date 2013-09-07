package com.rs.net.decoders.handlers;

import java.util.ArrayList;
import com.rs.io.InputStream;
import com.rs.utils.Utils;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
//import com.rs.game.player.skills.Mining;

/**
 * Simplifies the event which occurs when a certain object is clicked
 * (OBJECT_ClICK_1 packet) so that the end-user of the source will not need to
 * deal with the hassle of adding ifs and other code in WorldPacketsDecoder. <br />
 * I haven't taken the time to copy all the implemented IDs from
 * WorldPacketsDecoder, however. (Besides Mining)
 * 
 * @author Cyber Sheep
 */

public class ObjectHandler {

	/**
	 * Fix for "Nothing interesting happens" message being sent when an object
	 * is clicked
	 */


	public static void handleItemOnObject(final Player player, final WorldObject object, final int interfaceId, final Item item) {
		final int itemId = item.getId();
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.faceObject(object);	
						System.out.println("Item on object: " + object.getId());
				
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}
}