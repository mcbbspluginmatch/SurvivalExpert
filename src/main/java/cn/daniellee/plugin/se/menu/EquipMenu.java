package cn.daniellee.plugin.se.menu;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.core.GemCore;
import cn.daniellee.plugin.se.model.GemInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EquipMenu {

	public static Inventory generate(Player player) {
		Inventory menu = Bukkit.createInventory(new EquipMenuHolder(), InventoryType.HOPPER, SurvivalExpert.getInstance().getConfig().getString("menu.equip.title", "&6&lGem equip").replace("&", "§"));

		// 填充角
		int[] cornerPosition = new int[]{2};
		for (int i : cornerPosition) menu.setItem(i, Common.getCorner());
		// 填充边
		int[] borderPosition = new int[]{1, 3};
		for (int i : borderPosition) menu.setItem(i, Common.getBorder());

		// 战斗宝石
		ItemStack battleItem;
		int battleGem = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.gem", 0);
		if (battleGem == 0) {
			battleItem = getSlot("Battle");
		} else {
			battleItem = GemCore.getGemItemStack(new GemInfo("Battle", battleGem));
		}
		menu.setItem(0, battleItem);

		// 生存宝石
		ItemStack lifeItem;
		int lifeGem = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.gem", 0);
		if (lifeGem == 0) {
			lifeItem = getSlot("Life");
		} else {
			lifeItem = GemCore.getGemItemStack(new GemInfo("Life", lifeGem));
		}
		menu.setItem(4, lifeItem);

		return menu;
	}

	public static ItemStack getSlot(String type) {
		List<String> slotLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.equip.slot." + type.toLowerCase() + ".lore");
		slotLore.add(0, GemCore.SLOT_LORE);
		return ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.equip.slot." + type.toLowerCase() + ".name", ("Battle".equals(type) ? "&d" : "&a") + type + " &bgem slot"), slotLore, SurvivalExpert.getInstance().getConfig().getString("menu.equip.slot." + type.toLowerCase() + ".item.material", "WHITE_STAINED_GLASS_PANE"), SurvivalExpert.getInstance().getConfig().getInt("menu.equip.slot." + type.toLowerCase() + ".item.durability", 0));
	}

	public static class EquipMenuHolder implements InventoryHolder {
		@Override
		public Inventory getInventory() {
			return Bukkit.createInventory(null, InventoryType.HOPPER);
		}
	}
}
