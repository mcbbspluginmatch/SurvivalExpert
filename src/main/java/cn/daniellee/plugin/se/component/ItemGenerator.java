package cn.daniellee.plugin.se.component;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/**
 * 物品生成实用类
 */
public class ItemGenerator {

	public static ItemStack getItem(String name, List<String> lore, String m, int durability) {
		Material material = Material.getMaterial(m.toUpperCase());
		if (material == null) material = Material.STONE;
		ItemStack itemStack = new ItemStack(material);
		itemStack.setDurability((short) durability);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name.replace("&", "§"));
		if (lore != null && !lore.isEmpty()) {
			for (int i = 0; i < lore.size(); i++) lore.set(i, lore.get(i).replace("&", "§"));
			itemMeta.setLore(lore);
		}
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack getSkullItem(String owner, String name, List<String> lore) {
		Material material = Material.getMaterial("PLAYER_HEAD");
		if (material == null) material = Material.getMaterial("SKULL_ITEM");
		ItemStack itemStack = new ItemStack(material);
		itemStack.setDurability((short) 3);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		skullMeta.setOwner(owner);
		skullMeta.setDisplayName(name.replace("&", "§"));
		if (lore != null && !lore.isEmpty()) {
			for (int i = 0; i < lore.size(); i++) lore.set(i, lore.get(i).replace("&", "§"));
			skullMeta.setLore(lore);
		}
		itemStack.setItemMeta(skullMeta);
		return itemStack;
	}
}
