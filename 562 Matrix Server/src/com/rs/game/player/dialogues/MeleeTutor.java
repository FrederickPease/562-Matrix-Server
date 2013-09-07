package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Skills;

public class MeleeTutor extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Good day, How may I help you?"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "I would like to buy a defence skillcape." },
					IS_PLAYER, player.getIndex(), 9827);
		}else if(stage == 0) {
                if (player.getSkills().getLevel(Skills.DEFENCE) == 99) {
                	stage = 1;
                	sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Anybody who has spent as much time defending", 
							"himself deserves the right to own one. That'll be",
							"99000 coins please. "
							}, IS_NPC, npcId, 9827);;
                } else {
                	stage = 100;
                	sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Looks like you don't have a level of 99 defence,",
							"you need a level of 99 defence to buy one."
							}, IS_NPC, npcId, 9827);;
                }
		}else if(stage == 100) {
end();
		}else if (stage == 1) {
			stage = 2;
			sendDialogue(SEND_2_OPTIONS,
					"What would you like to say?",
					"99000!? that's too much for me."
					,"No problem.");
		} else if (stage == 2) {
			stage = 3;
			if(componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"99000!? that's too much for me."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 4;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No problem."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else
				end();
			}else if (stage == 3) {
				stage = 100;
					sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Well, go kill monsters untill you like you",
							"are able to afford one."
							}, IS_NPC, npcId, 9827);
			}else if (stage == 4) {
				stage = 100;
				if(!player.getInventory().containsItem(995, 99000)) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Looks like you do not have 99000 coins."
						}, IS_NPC, npcId, 9827);
				}else{
					sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Execellent, wear that cape with pride my friend."
							}, IS_NPC, npcId, 9827);
					player.getInventory().deleteItem(995, 99000);
				//	for(int skill = 0; skill < 24; skill++)
					//if(player.getSkills().getLevel(Skills.WOODCUTTING) >
				//player.getSkills().getLevel(skill)){
				player.getInventory().addItem(9753,1);
				player.getInventory().addItem(9755,1);
				//}else{
					//player.getInventory().addItem(10660,1);
					//player.getInventory().addItem(9809,1);
					//}
				}
				}else
				end();
		
	}

	@Override
	public void finish() {
		
	}

}
