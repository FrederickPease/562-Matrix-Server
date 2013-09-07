package com.rs.game.player.controlers;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.item.Item;
import com.rs.game.player.content.Pots;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public class SafePortal extends Controler {

private boolean showingSkull;

@Override
public void start() {
//Not sure :D:D
}

@Override
public boolean login() {
moved();
return false;
}

@Override
public boolean keepCombating(Entity target) {
if (target instanceof NPC)
return true;
if (!canAttack(target))
return false;
if (target.getAttackedBy() != player
&& player.getAttackedBy() != target)
player.setWildernessSkull();
return true;
}

@Override
public boolean canAttack(Entity target) {
if (target instanceof Player) {
Player p2 = (Player) target;
if (canHit(target))
return true;
if(!isAtArea(p2))
return false;
}
return true;
}

@Override
public boolean canHit(Entity target) {
if(!isAtArea(target))
return false;
if (target instanceof NPC)
return true;
Player p2 = (Player) target;
return true;
}


@Override
public boolean sendDeath() {
removeControler();
return true; // TODO custom dead without graves
}

@Override
public void moved() {
boolean isAtArea = isAtArea(player);
boolean isAtWildSafe = isAtWildSafe();
if (isAtArea) {
player.setCanPvp(true);
player.getAppearence().generateAppearenceData();
player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 19, 789);
//player.getPackets().sendGameMessage("Just to make sure it works.");
}
if (!isAtArea) {
player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 19, 789);
player.setCanPvp(false);
player.getAppearence().generateAppearenceData();
}
}



@Override
public boolean logout() {
return false; // so doesnt remove script
}

@Override
public void forceClose() {
player.setCanPvp(false);
}

public boolean isAtWildSafe() {
return (player.getX() >= 2940 && player.getX() <= 3395
&& player.getY() <= 3524 && player.getY() >= 3523);
}

public static final boolean isAtArea(WorldTile tile) {
return (tile.getX() >= 2756 && tile.getX() <= 2875
&& tile.getY() >= 5512 && tile.getY() <= 5627);
}


/*<swX>2755</swX>
<swY>5505</swY>
<nwX>2875</nwX>
<nwY>5511</nwY>*/


} 