package com.pluginexamples;

import com.rs.game.player.Player;
import com.rs.game.player.content.Skillcapes;
import com.rs.game.player.content.achievements.AchievementSystemDialogue;
import com.rs.game.player.content.achievements.SetReward;
import com.rs.game.player.content.dialogue.Conversation;
import com.rs.game.player.content.dialogue.Dialogue;
import com.rs.game.player.content.dialogue.HeadE;
import com.rs.game.player.content.dialogue.Options;
import com.rs.game.player.content.skills.slayer.Master;
import com.rs.game.player.content.skills.slayer.Slayer;
import com.rs.plugin.annotations.NPCClickHandler;
import com.rs.plugin.annotations.PluginEventHandler;
import com.rs.plugin.events.NPCClickEvent;

@PluginEventHandler
public class SlayerMasterD extends Conversation {

	@NPCClickHandler(ids = { 8480, 8481, 1597, 1598, 7779, 8466, 9085 })
	public static void handleMasters(NPCClickEvent e) {
		Master master = Master.getMasterForId(e.getNPC().getId());
		switch(e.getOption()) {
		case "Talk-to":
			e.getPlayer().startConversation(new SlayerMasterD(e.getPlayer(), master));
			break;
		case "Get-task":
			e.getPlayer().getSlayer().getTaskFrom(e.getPlayer(), master);
			break;
		case "Trade":
			e.getPlayer().getSlayer().openShop(e.getPlayer(), master);
			break;
		case "Rewards":
			Slayer.openBuyInterface(e.getPlayer());
			break;
		}
	}
	
	public SlayerMasterD(Player player, Master master) {
		super(player);
		
		addNPC(master.npcId, HeadE.NO_EXPRESSION, "'Ello, and what are you after then?");
		addOptions("What would you like to say?", new Options() {
			@Override
			public void create() {
				option("I need another assignment.", new Dialogue().addPlayer(HeadE.CHEERFUL, "I need another assignment.").addNext(p -> {
					p.getSlayer().getTaskFrom(p, master);
				}));
				option("Do you have anything for trade?", new Dialogue().addPlayer(HeadE.CHEERFUL, "Do you have anything for trade?").addNext(p -> {
					p.getSlayer().openShop(p, master);
				}));
				option("I'd like to see the rewards shop please.", new Dialogue().addPlayer(HeadE.CHEERFUL, "I'd like to see the rewards shop please.").addNext(p -> {
					Slayer.openBuyInterface(p);
				}));
				if (master == Master.Kuradal)
					option("What is that cape you're wearing?", Skillcapes.Slayer.getOfferCapeDialogue(player, master.npcId));
				if (master == Master.Vannaka)
					option("About the Achievement System...", new AchievementSystemDialogue(player, master.npcId, SetReward.VARROCK_ARMOR).getStart());
			}
		});
		create();
	}
}
