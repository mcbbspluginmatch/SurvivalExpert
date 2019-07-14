package cn.daniellee.plugin.se.listener;

import cn.daniellee.plugin.se.SurvivalExpert;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.BlockDataMeta;

import java.util.Random;

public class PlayerListener implements Listener {

	private Random random = new Random();

	/**
	 * 玩家破坏矿物方块或作物方块时根据比率获得点数
	 * @param e 事件
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		String itemType = e.getBlock().getType().name();
		if (SurvivalExpert.getInstance().getOreBlocks().contains(itemType)) {
			if (random.nextInt(SurvivalExpert.getInstance().getOrePointRange()) == 0) {
				String path = player.getName() + ".battle.total";
				int targetPoints = SurvivalExpert.getInstance().getPlayerData().getInt(path, 0) + 1;
				SurvivalExpert.getInstance().getPlayerData().set(path, targetPoints);
				SurvivalExpert.getInstance().savePlayerData();
				player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.earn-point", "&eCongratulations on getting &b1 &e{type}&e points, total points &b{total}").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{total}", Integer.toString(targetPoints))).replace("&", "§"));
			}
		} else if (SurvivalExpert.getInstance().getCropBlocks().contains(itemType)) {
			Integer age = SurvivalExpert.getInstance().getAgeCheckBlock().get(itemType);
			if (age != null) {
				String blockData = e.getBlock().getBlockData().getAsString();
				if (!Integer.valueOf(blockData.substring(blockData.indexOf("age=") + 4, blockData.indexOf("age=") + 5)).equals(age)) {
					// 如果方块Age没达到要求
					return;
				}
			}
			if (random.nextInt(SurvivalExpert.getInstance().getCropPointRange()) == 0) {
				String path = player.getName() + ".life.total";
				int targetPoints = SurvivalExpert.getInstance().getPlayerData().getInt(path, 0) + 1;
				SurvivalExpert.getInstance().getPlayerData().set(path, targetPoints);
				SurvivalExpert.getInstance().savePlayerData();
				player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.earn-point", "&eCongratulations on getting &b1 &e{type}&e points, total points &b{total}").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&dLife")).replace("{total}", Integer.toString(targetPoints))).replace("&", "§"));
			}
		}
	}

	/**
	 * 在打出伤害时根据装备的宝石进行伤害加成
	 * @param e
	 */
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player)) {
			Player player = (Player) e.getDamager();


//			e.setDamage(e.getDamage());
		}
	}

}
