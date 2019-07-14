package cn.daniellee.plugin.se.command;

import cn.daniellee.plugin.se.menu.GemMenu;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExpertCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		// 玩家无参数使用则打开菜单
		if (strings.length == 0 && commandSender instanceof Player) {
			Player player = (Player) commandSender;
			player.openInventory(GemMenu.generate(player));
		}
//		} else if (strings.length != 0) {
//			// 显示排行GUI
//			if (strings[0].equalsIgnoreCase("conver") && commandSender instanceof Player) {
//				Player player = (Player) commandSender;
//				LeaderboardMenu.asyncGenerate(player);
//				// 选择模式
//			} else if (strings[0].equalsIgnoreCase("select") && commandSender instanceof Player && commandSender.hasPermission("qualifying.command.select")) {
//				Player player = (Player) commandSender;
//				boolean open = Qualifying.getInstance().getSelectMap().get(player.getName()) != null;
//				if (open) Qualifying.getInstance().getSelectMap().remove(player.getName());
//				else Qualifying.getInstance().getSelectMap().put(player.getName(), new Venue());
//				player.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.select-model", "&eSelection mode: {status}").replace("{status}", !open ? Qualifying.getInstance().getConfig().getString("message.status-open", "Open") : Qualifying.getInstance().getConfig().getString("message.status-close", "Close"))).replace("&", "§"));
//				// 列出场地
//			} else if (strings[0].equalsIgnoreCase("venues")) {
//				synchronized (BattleCore.LOCK) {
//					List<String> venues = new ArrayList<>();
//					BattleCore.venues.forEach(item -> venues.add(item.getName()));
//					commandSender.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.venue-list", "&eThe current venue is: {venues}").replace("{venues}", StringUtils.join(venues, ", "))).replace("&", "§"));
//				}
//				// 观战比赛
//			} else if (strings[0].equalsIgnoreCase("watch") && strings.length == 2 && commandSender instanceof Player) {
//				Player player = (Player) commandSender;
//				Venue venue = BattleCore.getVenueByName(strings[1]);
//				if (venue != null) {
//					if (player.teleport(venue.getObserver())) {
//						player.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.watch-success", "&eSuccessfully joined the battle.")).replace("&", "§"));
//					}
//				} else player.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.invalid-venue", "&eInvalid venue name.")).replace("&", "§"));
//				// 选择模式
//			} else if (strings[0].equalsIgnoreCase("remove") && strings.length == 2 && commandSender.hasPermission("qualifying.command.remove")) {
//				synchronized (BattleCore.LOCK) {
//					Venue venue = BattleCore.getVenueByName(strings[1]);
//					if (venue != null) {
//						BattleCore.venues.remove(venue);
//						Qualifying.getInstance().getConfig().set("venue." + venue.getName(), null);
//						Qualifying.getInstance().saveConfig();
//						commandSender.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.venue-removed", "&eVenue [{venue}] removed successfully.").replace("{venue}", venue.getName())).replace("&", "§"));
//					} else commandSender.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.invalid-venue", "&eInvalid venue name.")).replace("&", "§"));
//				}
//				// 开关队列
//			} else if (strings[0].equalsIgnoreCase("toggle") && commandSender.hasPermission("qualifying.command.toggle")) {
//				boolean closeQueue = Qualifying.getInstance().isCloseQueue();
//				Qualifying.getInstance().setCloseQueue(!closeQueue);
//				commandSender.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.toggle-queue", "&eCurrent queue status: {status}").replace("{status}", closeQueue ? Qualifying.getInstance().getConfig().getString("message.status-open", "Open") : Qualifying.getInstance().getConfig().getString("message.status-close", "Close"))).replace("&", "§"));
//				// 设置玩家分数
//			} else if (strings[0].equalsIgnoreCase("score") && strings.length == 3 && commandSender.hasPermission("qualifying.command.score")) {
//				Player player = Bukkit.getPlayer(strings[1]);
//				if (player == null) {
//					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
//					if (offlinePlayer == null) {
//						commandSender.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.not-exist", "&ePlayer not exist.")).replace("&", "§"));
//						return true;
//					}
//				}
//				int score = Integer.valueOf(strings[2]);
//				if (score < 0) score = 0;
//				Qualifying.getInstance().getPlayerData().set(strings[1] + ".score", score);
//				Qualifying.getInstance().getPlayerData().set(strings[1] + ".segment", BattleCore.scoreToSegment(score));
//				// 清空晋级赛
//				Qualifying.getInstance().getPlayerData().set(strings[1] + ".promotion", null);
//				Qualifying.getInstance().savePlayerData();
//				commandSender.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.score-set", "&eScore set successfully.")).replace("&", "§"));
//				// 重载插件
//			} else if (strings[0].equalsIgnoreCase("reload") && commandSender.hasPermission("qualifying.command.reload")) {
//				// 将队列和进行中的比赛清空
//				synchronized (BattleCore.LOCK) {
//					for (Contestant contestant : BattleCore.contestants) {
//						Player player = Bukkit.getPlayer(contestant.getName());
//						player.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.clear-queue", "&ePlugin reloading, you have been removed from the queue.")).replace("&", "§"));
//					}
//					for (Game game : BattleCore.games) {
//						for (Contestant contestant : game.getContestants()) {
//							Player player = Bukkit.getPlayer(contestant.getName());
//							player.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.clear-game", "&ePlugin reloading, your game will not be recorded.")).replace("&", "§"));
//						}
//					}
//					BattleCore.contestants.clear();
//					BattleCore.games.clear();
//				}
//				Qualifying.getInstance().reloadConfig();
//				Qualifying.getInstance().loadConfig();
//				commandSender.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.reload-success", "&eReload has been successful.")).replace("&", "§"));
//				// 发送帮助
//			} else sendHelp(commandSender);
//		} else commandSender.sendMessage((Qualifying.getInstance().getPrefix() + Qualifying.getInstance().getConfig().getString("message.player-only", "&eThe console cannot open the GUI menu.")).replace("&", "§"));


		return true;
	}

}
