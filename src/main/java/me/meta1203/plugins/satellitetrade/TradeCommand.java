package me.meta1203.plugins.satellitetrade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TradeCommand implements CommandExecutor {

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		if (!(arg0 instanceof Player)) {
			error("Command must be run as a player!", arg0);
			return true;
		}
		if (arg3.length < 1) {
			error("Usage: /trade [<player name> <value>] | <accept|deny>", arg0);
			return true;
		}
		Player p = (Player)arg0;
		
		if (arg3.length == 2) {
			try {
				double amount = Double.parseDouble(arg3[1]);
				Player to = Bukkit.getPlayer(arg3[0]);
				
				if (to == null) {
					error("Could not find player " + arg3[0] + "!", arg0);
					return true;
				}
				
				TradeChest chest = new TradeChest(p, to, amount);
				progress("Opening trading inventory for " + p.getName(), arg0);
				Satellitetrade.openChests.add(chest);
				chest.attachToPlayer();
			} catch (NumberFormatException e) {
				error("Usage: /trade <player name> <value>", arg0);
				error("Value must be a number!", arg0);
				return true;
			}
			return true;
		}
		
		if (arg3.length == 1 && arg3[0].equals("accept")) {
			TradeChest toFinish = null;
			for (TradeChest current : Satellitetrade.pending) {
				if (current.linkedToTradee(p)) {
					toFinish = current;
					break;
				}
			}
			if (toFinish == null) {
				error("You have no pending trades!", arg0);
				return true;
			}
			toFinish.acceptFinish();
			return true;
		} else if (arg3.length == 1 && arg3[0].equals("deny")) {
			TradeChest toFinish = null;
			for (TradeChest current : Satellitetrade.pending) {
				if (current.linkedToTradee(p)) {
					toFinish = current;
					break;
				}
			}
			if (toFinish == null) {
				error("You have no pending trades!", arg0);
				return true;
			}
			toFinish.denyFinish();
			return true;
		}
		
		error("Usage: /trade [<player name> <value>] | <accept|deny>", arg0);
		return true;
	}
	
	public void error(String msg, CommandSender receiver) {
		receiver.sendMessage(ChatColor.RED + msg);
	}
	
	public void progress(String msg, CommandSender receiver) {
		receiver.sendMessage(ChatColor.GREEN + msg);
	}


}
