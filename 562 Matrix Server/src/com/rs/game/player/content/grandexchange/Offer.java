package com.rs.game.player.content.grandexchange;

import java.io.Serializable;
import java.util.Date;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;

/**
 * 
 * @author Taylor Moon
 * 
 * @version Elixrr 2 | update 3
 */
public final class Offer implements Serializable {
	public transient Offer[] offer = new Offer[6];
	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = 8872511602665689138L;

	/**
	 * The owner of this offer
	 */
	private Player owner;

	/**
	 * The price
	 */
	private int price;

	/**
	 * The item ID
	 */
	private int id;

	/**
	 * The item amount
	 */
	private int amount;

	/**
	 * The creation date
	 */
	private Date date;

	/**
	 * The progress
	 */
	private int progress;

	/**
	 * If the player has yet to collected price offset
	 */
	private boolean needsMoneyCollected;

	/**
	 * The item box
	 */
	private int box;

	/**
	 * The amount sold/bought
	 */
	private int amountTransacted;

	/**
	 * The player instance
	 */
	private transient Player player;

	/**
	 * The type of exchange
	 */
	private GrandExchangeType type;

	/**
	 * If the offer is aborted
	 */
	private boolean aborted;

	/**
	 * If the offer was accepted or not
	 */
	private boolean accepted;

	/**
	 * If this offer needs to be collected
	 */
	private boolean needsCollected = false;

	/**
	 * Constructs the offer
	 * 
	 * @param Owner
	 *            The owner of this offer
	 * @param box
	 *            The box corresponding to the offer
	 * @param price
	 *            The price
	 * @param id
	 *            The item id
	 * @param amount
	 *            The amount
	 * @param progress
	 *            The progress
	 * @param buying
	 *            If it's buying or selling
	 */
	public Offer(Player owner, int box, int price, int id, int amount,
			int progress, GrandExchangeType type, Date date) {
		this.owner = owner;
		this.box = box;
		this.price = price;
		this.id = id;
		this.amount = amount;
		this.type = type;
		this.progress = progress;
	}

	/**
	 * Returns the owner of this offer
	 * 
	 * @return The owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Returns the price of this offer
	 * 
	 * @return The price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Returns the item ID of this offer
	 * 
	 * @return The id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the creation date
	 * 
	 * @return The creation date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Returns the item amount of this offer
	 * 
	 * @return The amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Returns the box of this offer
	 * 
	 * @return The box
	 */
	public int getBox() {
		return box;
	}

	/**
	 * Returns the type of exchange
	 * 
	 * @return The exchange type
	 */
	public GrandExchangeType getExchangeType() {
		return type;
	}

	/**
	 * Returns the owner
	 * 
	 * @return The owner
	 */
	public Player getPlayer() {
		if (player == null) {
			player = owner;
		}
		return player;
	}

	/**
	 * Sets the ID of the iten
	 * 
	 * @param id
	 *            The id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		ItemDefinitions def = ItemDefinitions.getItemDefinitions(id);
		return "[" + date + "]:Transacting: " + amount + "x" + def.getName()
				+ ", Progress ID: " + progress + ", Amount Transacted: "
				+ amountTransacted + "";
	}

	/**
	 * Returns the progress of the item
	 * 
	 * @return The progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * Increases the progress of the item
	 * 
	 * @param amount
	 *            The amount to increase
	 */
	public void increaseProgress(int amount) {
		progress += amount;
	}

	/**
	 * Sets the amount transacted
	 * 
	 * @param amount
	 *            The amount to set
	 */
	public void setAmountTransacted(int amount) {
		amountTransacted = amount;
	}

	/**
	 * Checks if this offer is aborted
	 * 
	 * @return If it's aborted
	 */
	public boolean isAborted() {
		return aborted;
	}

	/**
	 * Causes this offer to abort
	 */
	public void abort() {
		aborted = true;
	}

	/**
	 * 
	 * @return The amount sold/bought
	 */
	public int getAmountTransacted() {
		return amountTransacted;
	}

	/**
	 * If this offer was acceped
	 * 
	 * @return accepted
	 */
	public boolean wasAccepted() {
		return accepted;
	}

	/**
	 * Accepts this offer
	 */
	public void accept() {
		accepted = true;
	}

	/**
	 * Sets needs collected to true
	 */
	public void needsCollected() {
		needsCollected = true;
	}

	/**
	 * If this offer needs collected
	 * 
	 * @return If this offer needs collected
	 */
	public boolean waitingCollection() {
		return needsCollected;
	}

	/**
	 * Sets needs money collected to true
	 */
	public void needsMoneyCollected() {
		needsMoneyCollected = true;
	}

	/**
	 * If the player collected their money
	 * 
	 * @return True if so; false otherwise
	 */
	public boolean moneyCollected() {
		return needsMoneyCollected;
	}
}