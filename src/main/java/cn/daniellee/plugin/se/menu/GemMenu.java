package cn.daniellee.plugin.se.menu;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.menu.holder.GemMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GemMenu {

	public static Inventory generate(Player player) {
		Inventory menu = Bukkit.createInventory(new GemMenuHolder(), InventoryType.HOPPER, SurvivalExpert.getInstance().getConfig().getString("menu.gem.title", "&eGem equip").replace("&", "§"));

		// 填充角
		ItemStack corner = ItemGenerator.getItem(" ", null, SurvivalExpert.getInstance().getConfig().getString("menu.common.corner.material", "MAGENTA_STAINED_GLASS_PANE"),  SurvivalExpert.getInstance().getConfig().getInt("menu.common.corner.durability", 2));
		int[] cornerPosition = new int[]{2};
		for (int i : cornerPosition) menu.setItem(i, corner);
		// 填充边
		ItemStack border = ItemGenerator.getItem(" ", null, SurvivalExpert.getInstance().getConfig().getString("menu.common.border.material", "BLUE_STAINED_GLASS_PANE"),  SurvivalExpert.getInstance().getConfig().getInt("menu.common.border.durability", 11));
		int[] borderPosition = new int[]{1, 3};
		for (int i : borderPosition) menu.setItem(i, border);

		// 战斗宝石
		ItemStack battleItem;
		int battleGem = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.gem", 0);
		if (battleGem == 0) {
			List<String> slotLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.gem.slot.battle.lore");
			slotLore.add(0, SurvivalExpert.SLOT_LORE);
			battleItem = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.gem.slot.battle.name", "&dBattle &bgem slot"), slotLore, SurvivalExpert.getInstance().getConfig().getString("menu.gem.slot.battle.item.material", "WHITE_STAINED_GLASS_PANE"), SurvivalExpert.getInstance().getConfig().getInt("menu.gem.slot.battle.item.durability", 0));
		} else {
			List<String> gemLore = SurvivalExpert.getInstance().getConfig().getStringList("gem.battle.lore");
			gemLore.add(0, SurvivalExpert.GEM_LORE.replace("{type}", "Battle").replace("{level}", Integer.toString(battleGem)));
			battleItem = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("gem.battle.name", "&7[&6SurvivalExpert&7]&dBattle&bGem&7[&eLv.{level}&7]").replace("{level}", Integer.toString(battleGem)), gemLore, SurvivalExpert.getInstance().getConfig().getString("gem.battle.item.material", "FIRE_CHARGE"), SurvivalExpert.getInstance().getConfig().getInt("gem.battle.item.durability", 0));
		}
		menu.setItem(0, battleItem);

		// 生存宝石
		ItemStack lifeItem;
		int lifeGem = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.gem", 0);
		if (lifeGem == 0) {
			List<String> slotLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.gem.slot.life.lore");
			slotLore.add(0, SurvivalExpert.SLOT_LORE);
			lifeItem = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.gem.slot.life.name", "&dLife &bgem slot"), slotLore, SurvivalExpert.getInstance().getConfig().getString("menu.gem.slot.life.item.material", "WHITE_STAINED_GLASS_PANE"), SurvivalExpert.getInstance().getConfig().getInt("menu.gem.slot.life.item.durability", 0));
		} else {
			List<String> gemLore = SurvivalExpert.getInstance().getConfig().getStringList("gem.life.lore");
			gemLore.add(0, SurvivalExpert.GEM_LORE.replace("{type}", "Life").replace("{level}", Integer.toString(lifeGem)));
			lifeItem = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("gem.life.name", "&7[&6SurvivalExpert&7]&aLife&bGem&7[&eLv.{level}&7]").replace("{level}", Integer.toString(lifeGem)), gemLore, SurvivalExpert.getInstance().getConfig().getString("gem.life.item.material", "SLIME_BALL"), SurvivalExpert.getInstance().getConfig().getInt("gem.life.item.durability", 0));
		}
		menu.setItem(4, lifeItem);

		return menu;
	}

}
