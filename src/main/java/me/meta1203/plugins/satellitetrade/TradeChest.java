package me.meta1203.plugins.satellitetrade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeChest {
	private Inventory inv;
	private Player owner;
	private Player tradee;
	private double worth;
	
	public TradeChest(Player owner, Player tradee, double worth) {
		this.owner = owner;
		this.tradee = tradee;
		this.worth = worth;
		inv = Bukkit.getServer().createInventory(owner, 27, "Trade");
	}
	
	public void attachToPlayer() {
		owner.openInventory(inv);
	}
	
	public boolean match(Player isOwner) {
		return isOwner.equals(owner);
	}
	
	public boolean linkedToTradee(Player tradee) {
		return tradee.equals(this.tradee);
	}
	
	public void notifyTradee() {
		owner.sendMessage(ChatColor.GREEN + "Sent trade request to " + tradee.getName());
		tradee.sendMessage(ChatColor.GREEN + owner.getName() + " wants to trade the following for " + Satellitetrade.econ.format(worth) + ":");
		tradee.sendMessage(ChatColor.AQUA + StringUtils.serializeInv(inv));
		tradee.sendMessage("Accept with '/trade accept', or deny with '/trade deny'");
	}
	
	public void returnItems() {
		for (ItemStack current : inv.getContents()) {
			if (current == null) {
				continue;
			}
			owner.getInventory().addItem(current);
		}
	}
	
	public void sendItems() {
		for (ItemStack current : inv.getContents()) {
			if (current == null) {
				continue;
			}
			tradee.getInventory().addItem(current);
		}
	}
	
	public void acceptFinish() {
		if (Satellitetrade.econ.transact(tradee, owner, worth)) {
			sendItems();
			owner.sendMessage(ChatColor.GREEN + tradee.getName() + " accepted your transaction!");
			Satellitetrade.pending.remove(this);
		} else {
			denyFinish();
		}
	}
	
	public void denyFinish() {
		returnItems();
		owner.sendMessage(ChatColor.RED + tradee.getName() + " denied your transaction!");
		Satellitetrade.pending.remove(this);
	}
}
