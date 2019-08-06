package cn.daniellee.plugin.se.command;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.core.GemCore;
import cn.daniellee.plugin.se.menu.*;
import cn.daniellee.plugin.se.model.GemInfo;
import cn.daniellee.plugin.se.model.PlayerData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ExpertCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		// 玩家无参数使用则打开菜单
		if (strings.length == 0 && commandSender instanceof Player) {
			Player player = (Player) commandSender;
			player.openInventory(MainMenu.generate(player));
		} else if (strings.length != 0) {
			// 显示装备GUI
			if (strings[0].equalsIgnoreCase("equip") && commandSender instanceof Player) {
				Player player = (Player) commandSender;
				player.openInventory(EquipMenu.generate(player));
			// 显示升级GUI
			} else if (strings[0].equalsIgnoreCase("upgrade") && commandSender instanceof Player) {
				Player player = (Player) commandSender;
				player.openInventory(UpgradeMenu.generate());
			// 显示排行GUI
			} else if (strings[0].equalsIgnoreCase("ranking") && commandSender instanceof Player) {
				Player player = (Player) commandSender;
				new BukkitRunnable() {
					@Override
					public void run() {
						player.openInventory(RankingMenu.generate(player));
					}
				}.runTask(SurvivalExpert.getInstance());
			// 显示展示GUI
			} else if (strings[0].equalsIgnoreCase("enum") && commandSender instanceof Player) {
				Player player = (Player) commandSender;
				player.openInventory(EnumMenu.generate());
			// 给予宝石
			} else if (strings[0].equalsIgnoreCase("give") && strings.length > 3 && commandSender.hasPermission("expert.command.give")) {
				Player targetPlayer = Bukkit.getPlayer(strings[1]);
				if (targetPlayer != null) {
					int targetLevel = Integer.valueOf(strings[3]);
					if (targetLevel > 0) {
						GemInfo gemInfo = new GemInfo();
						gemInfo.setLevel(targetLevel);
						if ("battle".equalsIgnoreCase(strings[2])) {
							gemInfo.setType("Battle");
						} else if ("life".equalsIgnoreCase(strings[2])) {
							gemInfo.setType("Life");
						} else {
							commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.invalid-param", "&eInvalid parameter, please check and try again.")).replace("&", "§"));
							return true;
						}
						ItemStack gemItemStack = GemCore.getGemItemStack(gemInfo);
						int number = 1;
						if (strings.length > 4) {
							number = Integer.valueOf(strings[4]);
							if (number < 1) number = 1;
							if (number > 64) number = 64;
						}
						gemItemStack.setAmount(number);
						if (targetPlayer.getInventory().addItem(gemItemStack).isEmpty()) {
							commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.give-success", "&eSuccessfully give the gem to the target player.")).replace("&", "§"));
							targetPlayer.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.give-gem", "&eYou have got &b{number} &elevels &b{level} {type} &egems.").replace("{number}", Integer.toString(number)).replace("{level}", Integer.toString(targetLevel)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type." + gemInfo.getType().toLowerCase(), "Battle".equals(gemInfo.getType()) ? "&dBattle" : "&aLife"))).replace("&", "§"));
						} else{
							commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.give-failed", "&eTarget player's inventory is full, execution failed.")).replace("&", "§"));
							targetPlayer.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.inventory-full", "&eYour inventory is full and you can't get the target item.")).replace("&", "§"));
						}
					} else commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.invalid-param", "&eInvalid parameter, please check and try again.")).replace("&", "§"));
				} else commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.invalid-player", "&eTarget player is not online.")).replace("&", "§"));
			} else if (strings[0].equalsIgnoreCase("point") && strings.length > 3 && commandSender.hasPermission("expert.command.point")) {
				Player targetPlayer = Bukkit.getPlayer(strings[1]);
				if (targetPlayer != null) {
					int points = Integer.valueOf(strings[3]);
					if ("battle".equalsIgnoreCase(strings[2])) {
						PlayerData playerData = SurvivalExpert.getInstance().getStorage().getPlayerData(targetPlayer.getName());
						int current = playerData.getBattleTotal();
						int used = playerData.getBattleUsed();
						int result = current + points > 0 ? current + points : 0; // Math.max(current + points, 0); —— 754503921
						SurvivalExpert.getInstance().getStorage().updatePlayerData(targetPlayer.getName(), "battle_total", result);
						if (result < used) SurvivalExpert.getInstance().getStorage().updatePlayerData(targetPlayer.getName(), "battle_used", result);
						commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.modify-success", "&eOperation completed successfully.")).replace("&", "§"));
						targetPlayer.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.points-modify", "&eYour {type} &epoints have been modified to &b{number}.").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{number}", Integer.toString(result))).replace("&", "§"));
					} else if ("life".equalsIgnoreCase(strings[2])) {
						PlayerData playerData = SurvivalExpert.getInstance().getStorage().getPlayerData(targetPlayer.getName());
						int current = playerData.getLifeTotal();
						int used = playerData.getLifeUsed();
						int result = current + points > 0 ? current + points : 0;
						SurvivalExpert.getInstance().getStorage().updatePlayerData(targetPlayer.getName(), "life_total", result);
						if (result < used) SurvivalExpert.getInstance().getStorage().updatePlayerData(targetPlayer.getName(), "life_used", result);
						commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.modify-success", "&eOperation completed successfully.")).replace("&", "§"));
						targetPlayer.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.points-modify", "&eYour {type} &epoints have been modified to &b{number}.").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife")).replace("{number}", Integer.toString(result))).replace("&", "§"));
					} else {
						commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.invalid-param", "&eInvalid parameter, please check and try again.")).replace("&", "§"));
						return true;
					}
				} else commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.invalid-player", "&eTarget player is not online.")).replace("&", "§"));
				// 重载插件
			} else if (strings[0].equalsIgnoreCase("reload") && commandSender.hasPermission("expert.command.reload")) {
				SurvivalExpert.getInstance().reloadConfig();
				SurvivalExpert.getInstance().loadConfig();
				commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.reload-success", "&eReload has been successful.")).replace("&", "§"));
				// 发送帮助
			} else sendHelp(commandSender);
		} else commandSender.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.player-only", "&eThe console cannot open the GUI menu.")).replace("&", "§"));
		return true;
	}

	private void sendHelp(CommandSender commandSender) {
		commandSender.sendMessage(("&m&6---&m&a--------&3 : " + SurvivalExpert.getInstance().getConfig().getString("prompt-prefix", "&6SurvivalExpert") + "&3 : &m&a--------&m&6---").replace("&", "§"));

		String equipText =  SurvivalExpert.getInstance().getConfig().getString("help.equip", "&eOpen the gem equipment GUI menu.").replace("&", "§");
		TextComponent equipHelp = new TextComponent("/se equip  " + equipText);
		equipHelp.setColor(ChatColor.GRAY);
		equipHelp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/se equip"));
		equipHelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(equipText).color(ChatColor.BLUE).create()));
		commandSender.spigot().sendMessage(equipHelp);

		String upgradeText =  SurvivalExpert.getInstance().getConfig().getString("help.upgrade", "&eOpen the gem upgrade GUI menu.").replace("&", "§");
		TextComponent upgradeHelp = new TextComponent("/se upgrade  " + upgradeText);
		upgradeHelp.setColor(ChatColor.GRAY);
		upgradeHelp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/se upgrade"));
		upgradeHelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(upgradeText).color(ChatColor.BLUE).create()));
		commandSender.spigot().sendMessage(upgradeHelp);

		String rankingText =  SurvivalExpert.getInstance().getConfig().getString("help.ranking", "&eOpen the points ranking GUI menu.").replace("&", "§");
		TextComponent rankingHelp = new TextComponent("/se ranking  " + rankingText);
		rankingHelp.setColor(ChatColor.GRAY);
		rankingHelp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/se ranking"));
		rankingHelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(rankingText).color(ChatColor.BLUE).create()));
		commandSender.spigot().sendMessage(rankingHelp);

		String enumText =  SurvivalExpert.getInstance().getConfig().getString("help.enum", "&eOpen the ore/crop enum GUI menu.").replace("&", "§");
		TextComponent enumHelp = new TextComponent("/se enum  " + enumText);
		enumHelp.setColor(ChatColor.GRAY);
		enumHelp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/se enum"));
		enumHelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(enumText).color(ChatColor.BLUE).create()));
		commandSender.spigot().sendMessage(enumHelp);

		String giveText =  SurvivalExpert.getInstance().getConfig().getString("help.give", "&eGiving the specified player the type, level, and number of gems.").replace("&", "§");
		TextComponent giveHelp = new TextComponent("/se give <player> <type> <level> [number]  " + giveText);
		giveHelp.setColor(ChatColor.GRAY);
		giveHelp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/se give "));
		giveHelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(giveText).color(ChatColor.BLUE).create()));
		commandSender.spigot().sendMessage(giveHelp);

		String pointText =  SurvivalExpert.getInstance().getConfig().getString("help.point", "&eAdjust the player's battle or life points.").replace("&", "§");
		TextComponent pointHelp = new TextComponent("/se point <player> <type> <points>  " + pointText);
		pointHelp.setColor(ChatColor.GRAY);
		pointHelp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/se point"));
		pointHelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(pointText).color(ChatColor.BLUE).create()));
		commandSender.spigot().sendMessage(pointHelp);

		String reloadText = SurvivalExpert.getInstance().getConfig().getString("help.reload", "Reload configuration.").replace("&", "§");
		TextComponent reloadHelp = new TextComponent("/se reload  " + reloadText);
		reloadHelp.setColor(ChatColor.GRAY);
		reloadHelp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/se reload"));
		reloadHelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(reloadText).color(ChatColor.BLUE).create()));
		commandSender.spigot().sendMessage(reloadHelp);
	}

}
