package me.meta1203.plugins.satellitetrade;

import me.meta1203.plugins.satoshis.Satoshis;
import me.meta1203.plugins.satoshis.SatoshisEconAPI;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Econ {
	private boolean useVault = false;
	private Economy vault = null;
	private SatoshisEconAPI satoshis = null;
	
	public SatoshisEconAPI getSatoshisEconomy() {
		  Plugin p = Bukkit.getServer().getPluginManager().getPlugin("Satoshis");
		  if (p == null || !(p instanceof Satoshis)) {
		    return null;
		  }
		  return Satoshis.econ;
	}
	
	public Econ() {
		useVault = getSatoshisEconomy() == null;
		if (useVault) {
			Satellitetrade.log.info("Linking into Vault!");
			RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (economyProvider != null) {
	            vault = economyProvider.getProvider();
	        } else {
	        	Satellitetrade.log.severe("No economy plugin found! Disabling!");
	        	Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("SatelliteTrade"));
	        }
		} else {
			Satellitetrade.log.info("Linking into Satoshis!");
			satoshis = getSatoshisEconomy();
		}
		Satellitetrade.log.info("Linked into " + (useVault ? "Vault." : "Satoshis."));
	}
	
	public String format(double value) {
		if (useVault) {
			return vault.format(value);
		} else {
			return satoshis.formatValue(value, false);
		}
	}
	
	public boolean transact(Player from, Player to, double amount) {
		if (amount < 0) {
			from.sendMessage(ChatColor.RED + "Amount cannot be less than zero!");
			return false;
		}
		if (useVault) {
			if (!vault.has(from.getName(), amount)) {
				from.sendMessage(ChatColor.RED + "You do not have that much money!");
				return false;
			}
			vault.withdrawPlayer(from.getName(), amount);
			vault.depositPlayer(to.getName(), amount);
			from.sendMessage(ChatColor.GREEN + "Sent " + format(amount) + " from " + from.getName() + " to " + to.getName());
			to.sendMessage(ChatColor.GREEN + "Sent " + format(amount) + " from " + from.getName() + " to " + to.getName());
			return true;
		} else {
			if (!satoshis.hasMoney(from.getName(), amount + (satoshis.buyerorseller ? satoshis.priceOfTax(amount) : 0.0))) {
				from.sendMessage(ChatColor.RED + "You do not have that much money!" + (satoshis.buyerorseller ? " [Tax is: " + format(satoshis.priceOfTax(amount)) + "]" : ""));
				return false;
			}
			satoshis.transact(from.getDisplayName(), to.getName(), amount);
			from.sendMessage(ChatColor.GREEN + "Sent " + format(amount) + " from " + from.getName() + " to " + to.getName());
			to.sendMessage(ChatColor.GREEN + "Sent " + format(amount) + " from " + from.getName() + " to " + to.getName());
			return true;
		}
	}
}
