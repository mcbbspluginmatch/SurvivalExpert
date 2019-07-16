package cn.daniellee.plugin.se.menu;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.menu.holder.RankingMenuHolder;
import cn.daniellee.plugin.se.model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RankingMenu {

	public static Inventory generate(Player player) {
		Inventory menu = Bukkit.createInventory(new RankingMenuHolder(), 45, SurvivalExpert.getInstance().getConfig().getString("menu.ranking.title", "&6&lPoints ranking").replace("&", "§"));

		// 填充角
		int[] cornerPosition = new int[]{0, 8, 18, 26, 36, 44};
		for (int i : cornerPosition) menu.setItem(i, Common.corner);
		// 填充边
		int[] borderPosition = new int[]{9, 17, 27, 35};
		for (int i : borderPosition) menu.setItem(i, Common.border);

		List<PlayerData> playerDataList = new ArrayList<>();
		Set<String> names = SurvivalExpert.getInstance().getPlayerData().getKeys(false);
		for (String name : names) {
			PlayerData playerData = new PlayerData();
			playerData.setName(name);
			playerData.setBattleTotal(SurvivalExpert.getInstance().getPlayerData().getInt(name + ".battle.total", 0));
			playerData.setBattleGem(SurvivalExpert.getInstance().getPlayerData().getInt(name + ".battle.gem", 0));
			playerData.setLifeTotal(SurvivalExpert.getInstance().getPlayerData().getInt(name + ".life.total", 0));
			playerData.setLifeGem(SurvivalExpert.getInstance().getPlayerData().getInt(name + ".life.gem", 0));
			playerDataList.add(playerData);
		}
		List<PlayerData> battleSort = new ArrayList<>(playerDataList);
		for (int i = 0; i < battleSort.size() - 1; i++) {
			for (int j = i + 1; j < battleSort.size(); j++) {
				if (battleSort.get(i).getBattleTotal() < battleSort.get(j).getBattleTotal()) {
					PlayerData playerData = battleSort.get(i);
					battleSort.set(i, battleSort.get(j));
					battleSort.set(j, playerData);
				}
			}
		}
		List<PlayerData> lifeSort = new ArrayList<>(playerDataList);
		for (int i = 0; i < lifeSort.size() - 1; i++) {
			for (int j = i + 1; j < lifeSort.size(); j++) {
				if (lifeSort.get(i).getLifeTotal() < lifeSort.get(j).getLifeTotal()) {
					PlayerData playerData = lifeSort.get(i);
					lifeSort.set(i, lifeSort.get(j));
					lifeSort.set(j, playerData);
				}
			}
		}
		int[] battleSlot = new int[]{2, 3, 4, 5, 6, 11, 12, 13, 14, 15};
		int[] lifeSlot = new int[]{29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
		ItemStack battleEmpty = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.ranking.battle.empty.name", "&eWaiting for you"), null, SurvivalExpert.getInstance().getConfig().getString("menu.ranking.battle.empty.item.material", "PINK_STAINED_GLASS_PANE"), SurvivalExpert.getInstance().getConfig().getInt("menu.ranking.battle.empty.item.durability", 0));
		ItemStack lifeEmpty = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.ranking.life.empty.name", "&eWaiting for you"), null, SurvivalExpert.getInstance().getConfig().getString("menu.ranking.life.empty.item.material", "LIME_STAINED_GLASS_PANE"), SurvivalExpert.getInstance().getConfig().getInt("menu.ranking.life.empty.item.durability", 0));
		ItemStack tagetItem;
		int battleRanking = 0;
		int lifeRanking = 0;
		for (int i = 0; i < 10 || i < playerDataList.size(); i++) {
			if (i < playerDataList.size()) {
				if (battleSort.get(i).getName().equals(player.getName())) {
					battleRanking = i;
				}
				if (lifeSort.get(i).getName().equals(player.getName())) {
					lifeRanking = i;
				}
			}
			if (i < 10) {
				if (i < playerDataList.size()) {
					PlayerData playerData = battleSort.get(i);
					List<String> battleLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.ranking.battle.lore");
					if (battleLore != null && !battleLore.isEmpty()) for (int j = 0; j < battleLore.size(); j++) battleLore.set(j, battleLore.get(j).replace("{total}", Integer.toString(playerData.getBattleTotal())).replace("{level}", Integer.toString(playerData.getBattleGem())));
					tagetItem = ItemGenerator.getSkullItem(playerData.getName(), SurvivalExpert.getInstance().getConfig().getString("menu.ranking.battle.name", "&eBattle ace: &a{name}").replace("{name}", playerData.getName()), battleLore);
				} else tagetItem = battleEmpty;
				menu.setItem(battleSlot[i], tagetItem);
				if (i < playerDataList.size()) {
					PlayerData playerData = lifeSort.get(i);
					List<String> lifeLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.ranking.life.lore");
					if (lifeLore != null && !lifeLore.isEmpty()) for (int j = 0; j < lifeLore.size(); j++) lifeLore.set(j, lifeLore.get(j).replace("{total}", Integer.toString(playerData.getLifeTotal())).replace("{level}", Integer.toString(playerData.getLifeGem())));
					tagetItem = ItemGenerator.getSkullItem(playerData.getName(), SurvivalExpert.getInstance().getConfig().getString("menu.ranking.life.name", "&eLife ace: &a{name}").replace("{name}", playerData.getName()), lifeLore);
				} else tagetItem = lifeEmpty;
				menu.setItem(lifeSlot[i], tagetItem);
			}
		}
		List<String> mineLore = SurvivalExpert.getInstance().getConfig().getStringList("menu.ranking.mine.lore");
		if (mineLore != null && !mineLore.isEmpty()) for (int i = 0; i < mineLore.size(); i++) mineLore.set(i, mineLore.get(i).replace("{battleTotal}", Integer.toString(SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.total", 0))).replace("{battleRank}", Integer.toString(battleRanking)).replace("{lifeTotal}", Integer.toString(SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.total", 0))).replace("{lifeRank}", Integer.toString(lifeRanking)));
		menu.setItem(22, ItemGenerator.getSkullItem(player.getName(), SurvivalExpert.getInstance().getConfig().getString("menu.ranking.mine.name", "&bMy ranking"), mineLore));

		return menu;
	}


}
