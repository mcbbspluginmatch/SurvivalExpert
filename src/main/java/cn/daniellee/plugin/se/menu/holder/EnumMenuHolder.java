package cn.daniellee.plugin.se.menu.holder;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class EnumMenuHolder implements InventoryHolder {

	@Override
	public Inventory getInventory() {
		return Bukkit.createInventory(null, 45);
	}

}
