package cn.daniellee.plugin.se.menu;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.core.GemCore;
import cn.daniellee.plugin.se.menu.holder.UpgradeMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UpgradeMenu {

	public static Inventory generate() {
		Inventory menu = Bukkit.createInventory(new UpgradeMenuHolder(), InventoryType.DROPPER, SurvivalExpert.getInstance().getConfig().getString("menu.upgrade.title", "&b&lGem upgrade").replace("&", "§"));

		// 填充角
		int[] cornerPosition = new int[]{1, 3, 5};
		for (int i : cornerPosition) menu.setItem(i, Common.corner);
		// 填充边
		int[] borderPosition = new int[]{6, 8};
		for (int i : borderPosition) menu.setItem(i, Common.border);

		List<String> slotLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.upgrade.slot.lore");
		slotLore.add(0, GemCore.SLOT_LORE);
		ItemStack slotItem = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.upgrade.slot.name", "&bGem slot"), slotLore, SurvivalExpert.getInstance().getConfig().getString("menu.upgrade.slot.item.material", "WHITE_STAINED_GLASS_PANE"), SurvivalExpert.getInstance().getConfig().getInt("menu.upgrade.slot.item.durability", 0));

		menu.setItem(0, slotItem);
		menu.setItem(2, slotItem);

		menu.setItem(4, getRateItemStack("0"));

		return menu;
	}

	public static ItemStack getRateItemStack(String rate) {
		List<String> rateLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.upgrade.rate.lore");
		return ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.upgrade.rate.name", "&bSuccess rate [ &e{rate} &b]").replace("{rate}", rate), rateLore, SurvivalExpert.getInstance().getConfig().getString("menu.upgrade.rate.item.material", "HOPPER"), SurvivalExpert.getInstance().getConfig().getInt("menu.upgrade.rate.item.durability", 0));
	}

}
