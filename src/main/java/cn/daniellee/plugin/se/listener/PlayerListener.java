package cn.daniellee.plugin.se.listener;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.core.GemCore;
import cn.daniellee.plugin.se.menu.EquipMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

public class PlayerListener implements Listener {

	private Random random = new Random();

	/**
	 * 加入时刷新最大血量
	 * @param e
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		refreshMaxHealth(e.getPlayer());
	}

	/**
	 * 复活时刷新最大血量
	 * @param e
	 */
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		refreshMaxHealth(e.getPlayer());
	}

	private void refreshMaxHealth(Player player) {
		int maxHealth = 20 + GemCore.getHealthBonus(player.getName());
		player.setHealthScale(maxHealth);
		player.setMaxHealth(maxHealth);
		player.setHealth(maxHealth);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getDeathMessage() != null && e.getDeathMessage().contains("slain by ")) {
			String killerStr = e.getDeathMessage().substring(e.getDeathMessage().indexOf("slain by ") + 9);
			if (killerStr.contains(" ")) killerStr = killerStr.substring(0, killerStr.indexOf(" "));
			Player killer = Bukkit.getPlayer(killerStr);
			if (killer != null) {
				Bukkit.broadcastMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.boardcast.kill", "&c{killer} &ecarries &b{battleLevel} &elevel {battle} &egems &b{lifeLevel} &elevel {life} &egems killed &c{dead}.").replace("{killer}", killer.getName()).replace("{battleLevel}", Integer.toString(GemCore.getDamageBonus(killer.getName()) / 5)).replace("{battle}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{lifeLevel}", Integer.toString(GemCore.getHealthBonus(killer.getName()) / 5)).replace("{life}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife")).replace("{dead}", e.getEntity().getName())).replace("&", "§"));
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (GemCore.isShortcut(e.getItem())) {
			e.getPlayer().openInventory(EquipMenu.generate(e.getPlayer()));
			e.setCancelled(true);
		}
	}

	/**
	 * 玩家破坏矿物方块或作物方块时根据比率获得点数，计数的优先级放在最后
	 * @param e 事件
	 */
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (e.isCancelled()) return;
		String itemType = e.getBlock().getType().name();
		// 是否需要方块年龄检查
		Integer age = SurvivalExpert.getInstance().getAgeCheckBlock().get(itemType);
		if (age != null) {
			String blockData = e.getBlock().getBlockData().getAsString();
			if (!Integer.valueOf(blockData.substring(blockData.indexOf("age=") + 4, blockData.indexOf("age=") + 5)).equals(age)) {
				// 如果方块Age没达到要求
				return;
			}
		}
		if (SurvivalExpert.getInstance().getOreBlocks().contains(itemType)) {
			if (random.nextInt(100) < SurvivalExpert.getInstance().getOrePointRange()) {
				String path = player.getName() + ".battle.total";
				int targetPoints = SurvivalExpert.getInstance().getPlayerData().getInt(path, 0) + 1;
				SurvivalExpert.getInstance().getPlayerData().set(path, targetPoints);
				SurvivalExpert.getInstance().savePlayerData();
				player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.earn-point", "&eCongratulations on getting &b1 {type}&e points, total points &b{total}").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{total}", Integer.toString(targetPoints))).replace("&", "§"));
			}
		}
		if (SurvivalExpert.getInstance().getCropBlocks().contains(itemType)) {
			if (random.nextInt(100) < SurvivalExpert.getInstance().getCropPointRange()) {
				String path = player.getName() + ".life.total";
				int targetPoints = SurvivalExpert.getInstance().getPlayerData().getInt(path, 0) + 1;
				SurvivalExpert.getInstance().getPlayerData().set(path, targetPoints);
				SurvivalExpert.getInstance().savePlayerData();
				player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.earn-point", "&eCongratulations on getting &b1 {type}&e points, total points &b{total}").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&dLife")).replace("{total}", Integer.toString(targetPoints))).replace("&", "§"));
			}
		}
	}

	/**
	 * 在打出伤害时根据装备的宝石进行伤害加成
	 * @param e 事件
	 */
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player)) {
			Player damager = (Player) e.getDamager();
			e.setDamage(e.getDamage() + GemCore.getDamageBonus(damager.getName()));
		}
	}

}
