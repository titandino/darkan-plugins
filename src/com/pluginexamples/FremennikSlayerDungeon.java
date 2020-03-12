package com.pluginexamples;

import com.rs.game.WorldTile;
import com.rs.game.player.content.skills.agility.Agility;
import com.rs.game.player.content.world.AgilityShortcuts;
import com.rs.plugin.annotations.ObjectClickHandler;
import com.rs.plugin.annotations.PluginEventHandler;
import com.rs.plugin.events.ObjectClickEvent;

@PluginEventHandler
public class FremennikSlayerDungeon {

	/**
	 * Handles the chasm shortcut at the beginning of the dungeon.
	 * A few of the interaction handlers will have a "checkDistance" field that can be set to false optionally.
	 * This will mean that the event fires right when the player clicks the object rather than when the player
	 * finishes pathing to the object.
	 * @param The event to handle.
	 */
	@ObjectClickHandler(ids = { 44339 }, checkDistance = false)
	public static void handleChasm(ObjectClickEvent e) {
		if (!Agility.hasLevel(e.getPlayer(), 81))
			return;
		final WorldTile toTile = e.getPlayer().getX() < 2772 ? new WorldTile(2775, 10002, 0) : new WorldTile(2768, 10002, 0);
		//walkToAndExecute will pathfind the player to a tile and then run the event specified once the player reaches it.
		//this was used to override the chasm pathfinding since the chasm is technically unreachable.
		e.getPlayer().walkToAndExecute(e.getPlayer().getX() > 2772 ? new WorldTile(2775, 10002, 0) : new WorldTile(2768, 10002, 0), () -> {
			AgilityShortcuts.forceMovement(e.getPlayer(), toTile, 4721, 1, 1);
		});
	}
	
	@ObjectClickHandler(ids = { 9321 })
	public static void handleShortcut2(ObjectClickEvent e) {
		if (!Agility.hasLevel(e.getPlayer(), 62))
			return;
		AgilityShortcuts.forceMovement(e.getPlayer(), e.getPlayer().transform(e.getObject().getRotation() == 0 ? 5 : -5, 0), 3844, 1, 1);
	}

}
