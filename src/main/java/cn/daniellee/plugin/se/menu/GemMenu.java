package cn.daniellee.plugin.se.menu;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.menu.holder.GemMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GemMenu {

	public static Inventory generate(Player player) {
		Inventory menu = Bukkit.createInventory(new GemMenuHolder(), InventoryType.HOPPER, SurvivalExpert.getInstance().getConfig().getString("menu.gem.title", "&eGem equip").replace("&", "§"));

		// 填充角
		ItemStack corner = ItemGenerator.getItem(" ", null, SurvivalExpert.getInstance().getConfig().getString("menu.common.corner.material", "STAINED_GLASS_PANE"),  SurvivalExpert.getInstance().getConfig().getInt("menu.common.corner.durability", 2));
		int cornerPosition[] = new int[]{ 2 };
		for (int i : cornerPosition) menu.setItem(i, corner);
		// 填充边
		ItemStack border = ItemGenerator.getItem(" ", null, SurvivalExpert.getInstance().getConfig().getString("menu.common.border.material", "STAINED_GLASS_PANE"),  SurvivalExpert.getInstance().getConfig().getInt("menu.common.border.durability", 11));
		int borderPosition[] = new int[]{ 1, 3 };
		for (int i : borderPosition) menu.setItem(i, border);

		// 战斗宝石
		menu.setItem(0, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.gem.button.accept.name", "&aAccept"), null, SurvivalExpert.getInstance().getConfig().getString("menu.confirm.button.accept.item.material", "LIME_CONCRETE"), SurvivalExpert.getInstance().getConfig().getInt("menu.confirm.button.accept.item.durability", 0)));

		// 生存宝石
		menu.setItem(4, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.gem.button.refuse.name", "&cRefuse"), null, SurvivalExpert.getInstance().getConfig().getString("menu.confirm.button.refuse.item.material", "RED_CONCRETE"), SurvivalExpert.getInstance().getConfig().getInt("menu.confirm.button.refuse.item.durability", 0)));

		return menu;
	}

}
