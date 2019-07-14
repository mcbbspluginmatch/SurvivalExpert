package cn.daniellee.plugin.se.menu.holder;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GemMenuHolder implements InventoryHolder {

	@Override
	public Inventory getInventory() {
		return Bukkit.createInventory(null, InventoryType.HOPPER);
	}

}
