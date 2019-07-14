package cn.daniellee.plugin.se.listener;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.menu.holder.GemMenuHolder;
import cn.daniellee.plugin.se.model.GemInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

    /**
     * 主要实现：背包点击部分
     * @param e 事件
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        Inventory menu = e.getInventory();
        if (menu.getHolder() != null) {
            if (menu.getHolder() instanceof GemMenuHolder) {
                if (e.getRawSlot() == 1 || e.getRawSlot() == 2 || e.getRawSlot() == 3) {
                    e.setCancelled(true);
                } else {
                    // 如果点击的是插槽且手里的不是宝石
                    GemInfo gemInfo = SurvivalExpert.getInstance().getGemInfoByItemStack(e.getCursor());
                    if (e.getRawSlot() == 0) {
                        // 如果是空手点的，就是要卸下
                        if ("AIR".equals(e.getCursor().getType().toString())) {
                            // 如果不是插槽占位符就给卸下
                            if (!SurvivalExpert.getInstance().isSlot(e.getCurrentItem())) {
                                SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".battle.gem", null);
                                SurvivalExpert.getInstance().savePlayerData();
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.un-equip", "&eYou removed your gem, the bonus will be removed.")).replace("&", "§"));
                            } else e.setCancelled(true);
                        // 如果是拿着宝石点的
                        } else if (gemInfo != null && "Battle".equals(gemInfo.getType()) && e.getCursor().getAmount() == 1) {
                            if (SurvivalExpert.getInstance().isSlot(e.getCurrentItem())) {
                                e.setCurrentItem(null);
                            }
                            SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".battle.gem", gemInfo.getLevel());
                            SurvivalExpert.getInstance().savePlayerData();
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.gem-equip", "&eSuccessfully equipped &e{type} &egem, level &b{level}.").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{level}", Integer.toString(gemInfo.getLevel()))).replace("&", "§"));
                        } else e.setCancelled(true);
                    } else if (e.getRawSlot() == 4) {
                        // 如果是空手点的，就是要卸下
                        if ("AIR".equals(e.getCursor().getType().toString())) {
                            // 如果不是插槽占位符就给卸下
                            if (!SurvivalExpert.getInstance().isSlot(e.getCurrentItem())) {
                                SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".life.gem", null);
                                SurvivalExpert.getInstance().savePlayerData();
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.un-equip", "&eYou removed your gem, the bonus will be removed.")).replace("&", "§"));
                                // 立即设置最大生命值
                                player.setHealthScale(20);
                            } else e.setCancelled(true);
                            // 如果是拿着宝石点的
                        } else if (gemInfo != null && "Life".equals(gemInfo.getType()) && e.getCursor().getAmount() == 1) {
                            if (SurvivalExpert.getInstance().isSlot(e.getCurrentItem())) {
                                e.setCurrentItem(null);
                            }
                            SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".life.gem", gemInfo.getLevel());
                            SurvivalExpert.getInstance().savePlayerData();
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.gem-equip", "&eSuccessfully equipped &e{type} &egem, level &b{level}.").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife")).replace("{level}", Integer.toString(gemInfo.getLevel()))).replace("&", "§"));
                            // 立即设置最大生命值
                            player.setHealthScale(20 + 5 * gemInfo.getLevel());
                        } else e.setCancelled(true);
                    }
                }
            }
        }
    }

}
