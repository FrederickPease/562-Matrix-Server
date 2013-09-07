package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;

public class Talk extends Dialogue {
	
	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"Hello there adventurer, do you need help?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_2_OPTIONS, "Need help?",
					"Yes, please.", "No, thank you.");
			stage = 0;
		} else if (stage == 0) {
			if (componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Yes, please." },
						IS_PLAYER, player.getIndex(), 9827);
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "No, thank you." },
						IS_PLAYER, player.getIndex(), 9827);
				end();
			}
		//	System.out.println(componentId);
			stage = 1;
		} else if (stage == 1) {
				sendDialogue(SEND_4_OPTIONS, "What do you want to know?",
						"How do I make money?",
						"How do I get my levels up?", 
						"How do I become a moderator?",
						"Who is the owner?");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"There is a lot of way of making money, find your own way." }, IS_NPC, npcId, 9827);
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Train your way up from small to large monsters." }, IS_NPC, npcId, 9827);
			} else if (componentId == 3) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Becoming a moderator depends on how helpful you are!" }, IS_NPC, npcId, 9827);
			} else if (componentId == 4) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Wa3ad is the owner and founder." }, IS_NPC, npcId, 9827);
			}
		//	System.out.println(componentId);
			stage = 3;
		} else if (stage == 3) {
			end();
		}
	}

	@Override
	public void finish() {
		
	}
	

}
