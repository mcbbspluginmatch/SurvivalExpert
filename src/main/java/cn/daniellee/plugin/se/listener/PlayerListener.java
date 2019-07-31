package cn.daniellee.plugin.se.listener;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.core.GemCore;
import cn.daniellee.plugin.se.menu.EquipMenu;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.math.BigDecimal;
import java.util.Random;

public class PlayerListener implements Listener {

	private Random random = new Random();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		SurvivalExpert.getInstance().getStorage().refreshPlayerData(e.getPlayer().getName());
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getDeathMessage() != null && e.getDeathMessage().contains("slain by ")) {
			String killerStr = e.getDeathMessage().substring(e.getDeathMessage().indexOf("slain by ") + 9);
			if (killerStr.contains(" ")) killerStr = killerStr.substring(0, killerStr.indexOf(" "));
			Player killer = Bukkit.getPlayer(killerStr);
			if (killer != null) {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Message");
				out.writeUTF("ALL");
				out.writeUTF((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.boardcast.kill", "&c{killer} &ecarries &b{battleLevel} &elevel {battle} &egems &b{lifeLevel} &elevel {life} &egems killed &c{dead}.").replace("{killer}", killer.getName()).replace("{battleLevel}", Integer.toString(SurvivalExpert.getInstance().getStorage().getPlayerData(killer.getName()).getBattleGem())).replace("{battle}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{lifeLevel}", Integer.toString(SurvivalExpert.getInstance().getStorage().getPlayerData(killer.getName()).getLifeGem())).replace("{life}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife")).replace("{dead}", e.getEntity().getName())).replace("&", "§"));
				killer.sendPluginMessage(SurvivalExpert.getInstance(), "BungeeCord", out.toByteArray());
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
			if (e.getBlock().getData() < age) {
				// 如果方块Age没达到要求
				return;
			}
		}
		if (SurvivalExpert.getInstance().getOreBlocks().contains(itemType)) {
			if (random.nextInt(SurvivalExpert.RANDOM_CALC_BASE) < SurvivalExpert.getInstance().getOrePointRange()) {
				int targetPoints = SurvivalExpert.getInstance().getStorage().getPlayerData(player.getName()).getBattleTotal() + 1;
				SurvivalExpert.getInstance().getStorage().updatePlayerData(player.getName(), "battle_total", targetPoints);
				player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.earn-point", "&eCongratulations on getting &b1 {type}&e points, total points &b{total}").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{total}", Integer.toString(targetPoints))).replace("&", "§"));
			}
		}
		if (SurvivalExpert.getInstance().getCropBlocks().contains(itemType)) {
			if (random.nextInt(SurvivalExpert.RANDOM_CALC_BASE) < SurvivalExpert.getInstance().getCropPointRange()) {
				int targetPoints = SurvivalExpert.getInstance().getStorage().getPlayerData(player.getName()).getLifeTotal() + 1;
				SurvivalExpert.getInstance().getStorage().updatePlayerData(player.getName(), "life_total", targetPoints);
				player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.earn-point", "&eCongratulations on getting &b1 {type}&e points, total points &b{total}").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&dLife")).replace("{total}", Integer.toString(targetPoints))).replace("&", "§"));
			}
		}
	}

	/**
	 * 在打出伤害时根据装备的宝石进行伤害加成
	 * @param e 事件
	 */
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();
			e.setDamage(new BigDecimal(e.getDamage()).multiply(new BigDecimal(SurvivalExpert.getInstance().getStorage().getPlayerData(damager.getName()).getBattleGem()).multiply(SurvivalExpert.getInstance().getBattleBonusPercentage()).add(new BigDecimal(1))).doubleValue());
		}
		if (e.getEntity() instanceof Player) {
			Player entity = (Player) e.getEntity();
			double damage = new BigDecimal(e.getDamage()).multiply(new BigDecimal(1).subtract(new BigDecimal(SurvivalExpert.getInstance().getStorage().getPlayerData(entity.getName()).getLifeGem()).multiply(SurvivalExpert.getInstance().getLifeBonusPercentage()))).doubleValue();
			e.setDamage(damage > 0 ? damage : 0);
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByBlockEvent e) {
		if (e.getEntity() instanceof Player) {
			Player entity = (Player) e.getEntity();
			double damage = new BigDecimal(e.getDamage()).multiply(new BigDecimal(1).subtract(new BigDecimal(SurvivalExpert.getInstance().getStorage().getPlayerData(entity.getName()).getLifeGem()).multiply(SurvivalExpert.getInstance().getLifeBonusPercentage()))).doubleValue();
			e.setDamage(damage > 0 ? damage : 0);
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageEvent e) {
		if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
			if (e.getEntity() instanceof Player) {
				Player entity = (Player) e.getEntity();
				double damage = new BigDecimal(e.getDamage()).multiply(new BigDecimal(1).subtract(new BigDecimal(SurvivalExpert.getInstance().getStorage().getPlayerData(entity.getName()).getLifeGem()).multiply(SurvivalExpert.getInstance().getLifeBonusPercentage()))).doubleValue();
				e.setDamage(damage > 0 ? damage : 0);
			}
		}
	}

}
