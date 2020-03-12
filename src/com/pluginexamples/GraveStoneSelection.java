package com.pluginexamples;

import com.rs.cache.loaders.EnumDefinitions;
import com.rs.game.player.Player;
import com.rs.plugin.annotations.ButtonClickHandler;
import com.rs.plugin.annotations.PluginEventHandler;
import com.rs.plugin.events.ButtonClickEvent;
import com.rs.utils.Utils;

@PluginEventHandler
public class GraveStoneSelection {

	/**
	 * Opens the gravestone selection interface.
	 * @param player
	 */
	public static void openSelectionInterface(Player player) {
		player.getInterfaceManager().sendInterface(652);
		//Enables the interface's target parameters for right click options 0 and 1
		//on component 31 from slot 0 to slot 78.
		player.getPackets().setIFRightClickOps(652, 31, 0, 78, 0, 1);
		player.getPackets().setIFRightClickOps(652, 34, 0, 13, 0, 1);
		//Highlights the player's current gravestone on the interface.
		player.getVars().setVar(1146, player.getGraveStone() | 262112);
	}

	@ButtonClickHandler(ids = { 652 })
	public static void handleSelectionInterface(ButtonClickEvent e) {
		/*
		 * When the player clicks one of the gravestones, it saves the selected stone
		 * as a temporary variable on the player. Temporary variables are not saved
		 * when the player logs out.
		 */
		if (e.getComponentId() == 31)
			e.getPlayer().setTempI("graveSelection", e.getSlotId());
		else if (e.getComponentId() == 34)
			confirmSelection(e.getPlayer());
	}
	
	private static String getStoneName(int slot) {
		//As you can see in the dev-resources folder, there are some dumps from the game's cache you can view.
		//If you look for enum id 1099, you will see it contains each gravestone name mapped to the interface's slot id
		return EnumDefinitions.getEnum(1099).getStringValue(slot);
	}
	
	private static int getStonePrice(int slot) {
		//Same thing as the stone name but for the price of the gravestone
		return EnumDefinitions.getEnum(1101).getIntValue(slot);
	}

	public static void confirmSelection(Player player) {
		//Gets the temporary selected slot id (which is multiplied by 6 when the interface is loaded in the CS2 scripts)
		//and we divide it by 6 to get the real index of the gravestone.
		int slot = player.getTempI("graveSelection", -1) / 6;
		int price = getStonePrice(slot);
		String name = getStoneName(slot);
		if (slot != -1) {
			if (player.getInventory().getAmountOf(995) < price) {
				player.sendMessage("You need " + Utils.formatNumber(price) + " coins to purchase " + Utils.addArticle(name) + ".");
				return;
			}
			player.getInventory().deleteItem(995, price);
			player.setGraveStone(slot);
			player.closeInterfaces();
		}
	}
}