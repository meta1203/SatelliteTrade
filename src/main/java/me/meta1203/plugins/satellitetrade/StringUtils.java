package me.meta1203.plugins.satellitetrade;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StringUtils {
	public static String serializeInv(Inventory inv) {
		short num = 1;
		String ret = "";
		
		for (ItemStack current : inv.getContents()) {
			if (num % 4 == 0) {
				ret += "\n";
			}
			if (current == null) {
				continue;
			}
			System.out.println(current.getType().name());
			System.out.println(current.getAmount());
			ret += current.getAmount() + "x " + current.getType().name() + " | ";
		}
		
		return ret;
	}
}
