package me.meta1203.plugins.satellitetrade;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.meta1203.plugins.satoshis.SatoshisEconAPI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Satellitetrade extends JavaPlugin implements Listener {
	public static SatoshisEconAPI satEcon = null;
	public static List<TradeChest> openChests = new ArrayList<TradeChest>();
	public static List<TradeChest> pending = new ArrayList<TradeChest>();
	public static Econ econ = null;
	public static Logger log = Logger.getLogger("minecraft");
	
    public void onDisable() {
    	for (TradeChest current : Satellitetrade.pending) {
    		current.denyFinish();
		}
    	openChests.clear();
    }

    public void onEnable() {
    	econ = new Econ();
    	getCommand("trade").setExecutor(new TradeCommand());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
    	TradeChest chest = null;
    	for (TradeChest current : openChests) {
    		if (current.match(event.getInventory())) {
    			chest = current;
    			System.out.println("Yah.mov");
    			break;
    		}
    	}
    	if (chest == null) {
    		System.out.println("Nope.avi");
    		return;
    	}
    	chest.notifyTradee();
    	openChests.remove(chest);
    	pending.add(chest);
    }
}

