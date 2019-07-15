package cn.daniellee.plugin.se.menu;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.menu.holder.MainMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MainMenu {

	public static Inventory generate(Player player) {
		Inventory menu = Bukkit.createInventory(new MainMenuHolder(), 45, SurvivalExpert.getInstance().getConfig().getString("menu.main.title", "&b&lSurvival Expert").replace("&", "§"));

		// 填充角
		int cornerPosition[] = new int[]{ 0, 4, 8, 36, 40, 44 };
		for (int i : cornerPosition) menu.setItem(i, Common.corner);
		// 填充边
		int[] borderPosition = new int[]{ 1, 3, 5, 7, 9, 17, 27, 35, 37, 39, 41, 43 };
		for (int i : borderPosition) menu.setItem(i, Common.border);

		menu.setItem(11, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.main.button.equip.name", "&bGem equip"), SurvivalExpert.getInstance().getConfig().getStringList("menu.main.button.equip.lore"), SurvivalExpert.getInstance().getConfig().getString("menu.main.button.equip.item.material", "LIME_SHULKER_BOX"), SurvivalExpert.getInstance().getConfig().getInt("menu.main.button.equip.item.durability", 0)));

		menu.setItem(13, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.main.button.upgrade.name", "&bGem upgrade"), SurvivalExpert.getInstance().getConfig().getStringList("menu.main.button.upgrade.lore"), SurvivalExpert.getInstance().getConfig().getString("menu.main.button.upgrade.item.material", "RED_SHULKER_BOX"), SurvivalExpert.getInstance().getConfig().getInt("menu.main.button.upgrade.item.durability", 0)));

		menu.setItem(15, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.main.button.ranking.name", "&bPoints ranking"), SurvivalExpert.getInstance().getConfig().getStringList("menu.main.button.ranking.lore"), SurvivalExpert.getInstance().getConfig().getString("menu.main.button.ranking.item.material", "LIGHT_BLUE_SHULKER_BOX"), SurvivalExpert.getInstance().getConfig().getInt("menu.main.button.ranking.item.durability", 0)));

		int exchangeRatio = SurvivalExpert.getInstance().getConfig().getInt("setting.point-exchange-ratio", 100);

		int battleTotal = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.total", 0);
		int battleUsed = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.used", 0);
		List<String> battleLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.main.button.battle.lore");
		if (battleLore != null && !battleLore.isEmpty()) for (int i = 0; i < battleLore.size(); i++) battleLore.set(i, battleLore.get(i).replace("{ratio}", Integer.toString(exchangeRatio)).replace("{total}", Integer.toString(battleTotal)).replace("{usable}", Integer.toString(battleTotal - battleUsed)).replace("{number}", Integer.toString((battleTotal - battleUsed) / exchangeRatio)));
		menu.setItem(29, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.main.button.battle.name", "&dBattle &bpoints gem exchange"), battleLore, SurvivalExpert.getInstance().getConfig().getString("menu.main.button.battle.item.material", "MAGMA_CREAM"), SurvivalExpert.getInstance().getConfig().getInt("menu.main.button.battle.item.durability", 0)));

		menu.setItem(31, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.main.button.desc.name", "&bWay to get points"), SurvivalExpert.getInstance().getConfig().getStringList("menu.main.button.desc.lore"), SurvivalExpert.getInstance().getConfig().getString("menu.main.button.desc.item.material", "FIRE_CHARGE"), SurvivalExpert.getInstance().getConfig().getInt("menu.main.button.desc.item.durability", 0)));

		int lifeTotal = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.total", 0);
		int lifeUsed = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.used", 0);
		List<String> lifeLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.main.button.life.lore");
		if (lifeLore != null && !lifeLore.isEmpty()) for (int i = 0; i < lifeLore.size(); i++) lifeLore.set(i, lifeLore.get(i).replace("{ratio}", Integer.toString(exchangeRatio)).replace("{total}", Integer.toString(lifeTotal)).replace("{usable}", Integer.toString(lifeTotal - lifeUsed)).replace("{number}", Integer.toString((lifeTotal - lifeUsed) / exchangeRatio)));
		menu.setItem(33, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.main.button.life.name", "&aLife &bpoints gem exchange"), lifeLore, SurvivalExpert.getInstance().getConfig().getString("menu.main.button.life.item.material", "SLIME_BALL"), SurvivalExpert.getInstance().getConfig().getInt("menu.main.button.life.item.durability", 0)));

		return menu;
	}


}
