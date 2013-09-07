package com.rs.game.player.content.grandexchange;

/**
 * @author Taylor Moon
 * 
 * @version Elixrr 2 | update 3
 */
public enum GrandExchangeOfferType {

	/**
	 * If the offer was aborted
	 */
	ABORTED(-3),

	/**
	 * If the GE expected a reset
	 */
	RESET_GE(0),

	/**
	 * Resets after buy
	 */
	RESET_AFTER_BUY(8),

	/**
	 * Resets after sell
	 */
	RESET_AFTER_SELL(16),

	/**
	 * Submitting a buy offer
	 */
	SUBMITTING_BUY_OFFER(1),

	/**
	 * Buying progress 1
	 */
	BUYING_PROGRESS(2),

	/**
	 * Buying progress 2
	 */
	BUYING_PROGRESS_2(3),

	/**
	 * Buying progress 3
	 */
	BUYING_PROGRESS_3(4),

	/**
	 * Finished buying an item
	 */
	FINISHED_BUYING(5),

	/**
	 * Buying progress 5
	 */
	BUYING_PROGRESS_5(6),

	/**
	 * Buying progress 7
	 */
	BUYING_PROGRESS_6(7),

	/**
	 * Submitting a selling offer
	 */
	SUBMIT_SELL_OFFER(9),

	/**
	 * Selling progress 1
	 */
	SELLING_PROGRESS(10),

	/**
	 * Selling progress 2
	 */
	SELLING_PROGRESS_2(11),

	/**
	 * Selling progress 3
	 */
	SELLING_PROGRESS_3(13),

	/**
	 * Selling progress 4
	 */
	SELLING_PROGRESS_4(14),

	/**
	 * Selling progress 5
	 */
	SELLING_PROGRESS_5(15),

	/**
	 * Finished selling an item
	 */
	FINISHED_SELLING(13);

	/**
	 * The opcode
	 */
	private int opcode;

	/**
	 * Constructs the GE offer type
	 * 
	 * @param opcode
	 *            The opcode
	 */
	GrandExchangeOfferType(int opcode) {
		this.opcode = opcode;
	}

	/**
	 * Returns the opcode
	 * 
	 * @return The opcode;
	 */
	public int getOpcode() {
		return opcode;
	}
}