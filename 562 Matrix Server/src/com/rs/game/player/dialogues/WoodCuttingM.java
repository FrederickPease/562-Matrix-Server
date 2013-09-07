package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Skills;

public class WoodCuttingM extends Dialogue {

    /**
     * Starts the dialogue
     */
    public WoodCuttingM() {
    }

    @Override
    public void start() {
        npcId = ((Integer) parameters[0]).intValue();
    	sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Hello, are you here to buy a Woodcutting skillcape?"
				}, IS_NPC, npcId, 9827);
    	}

    /**
     * Runs the dialogue
     */
    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == -1) {
            stage = 0;
            sendDialogue(SEND_2_OPTIONS,"What would you like to say?", "Yes, I would like to buy a WoodCutting skillcape.", "Nope, came to say hi.");
        } else if (stage == 0) {
            if (componentId == 1) {
                if (player.getSkills().getLevel(Skills.WOODCUTTING) == 99) {
                    if (player.getInventory().containsItem(995, 99000)) {
                    	stage = 2;
                        sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(8274).name, "Great, you even braught the 99000 coins with you!"}, (byte) 1, 8274, 9827);
                    } else {
                        sendEntityDialogue((short) 243, new String[]{NPCDefinitions.getNPCDefinitions(8274).name, "You need 99000 coins."}, (byte) 1, 8274, 9827);
                    }
                } else {
                    sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "Sorry, you need to have 99 WoodCutting to talk to me."}, (byte) 1, npcId, 9827);
                }

            } else if (componentId == 2) {
                end();
            } else if (stage == 2) {
    			stage = 3;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Anybody who has spent as much time cutting trees as" +
						"you deserves the right to own one. That'll be 99000" +
						"coins please. "
						}, IS_NPC, npcId, 9827);
            } else if (stage == 3) {
    			stage = 4;
    			sendDialogue(SEND_2_OPTIONS,
    					"What would you like to say?",
    					"99000! that's too much for me."
    					,"no problem.");
            } else if (stage == 4) {
    			stage = 5;
    			if(componentId == 1) {
    				stage = 6;
    				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
    						player.getDisplayName(),
    						"99000! that's too much for me."
    						}, IS_PLAYER, player.getIndex(), 9827);
    			}else if (componentId == 2) {
    				stage = 7;
    				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
    						player.getDisplayName(),
    						"no problem."
    						}, IS_PLAYER, player.getIndex(), 9827);
    			}else
    				end();
            } else if (stage == 6) {
    			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Well, go chop down few more trees and sell the",
						"logs, then you will be able to afford one."
						}, IS_NPC, npcId, 9827);
            } else if (stage == 7) {
            	 if (player.getSkills().getLevel(Skills.WOODCUTTING) == 99) {
                     if (player.getInventory().containsItem(995, 99000)) {
                     	stage = 2;
                         sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(8274).name, "Great, you even braught the 99000 coins with you!"}, (byte) 1, 8274, 9827);
                     } else {
                         sendEntityDialogue((short) 243, new String[]{NPCDefinitions.getNPCDefinitions(8274).name, "You need 99000 coins."}, (byte) 1, 8274, 9827);
                     }
                 } else {
                     sendEntityDialogue((short) 241, new String[]{NPCDefinitions.getNPCDefinitions(npcId).name, "You need to have 99000 coins with you."}, (byte) 1, npcId, 9827);
                 }
            }else {
                end();
            }
        }
    }

    @Override
    public void finish() {
    }
    /**
     * Declares the npc ID
     */
    private int npcId;
}