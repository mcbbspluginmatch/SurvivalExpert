package cn.daniellee.plugin.se.listener;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.core.GemCore;
import cn.daniellee.plugin.se.menu.*;
import cn.daniellee.plugin.se.model.GemInfo;
import cn.daniellee.plugin.se.model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
            if (menu.getHolder() instanceof MainMenu.MainMenuHolder) {
                e.setCancelled(true);
                if (e.getRawSlot() == 11) {
                    if (e.getClick().isLeftClick()) {
                        player.openInventory(EquipMenu.generate(player));
                    } else if (e.getClick().isRightClick()) {
                        List<String> shortcutLore = SurvivalExpert.getInstance().getConfig().getStringList("gem.shortcut.lore");
                        shortcutLore.add(0, GemCore.SHORTCUT_LORE);
                        ItemStack shortcut = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("gem.shortcut.name", "&7[&6SurvivalExpert&7]&bGem equipment bar"), shortcutLore, SurvivalExpert.getInstance().getConfig().getString("gem.shortcut.item.material", "SNOWBALL"), SurvivalExpert.getInstance().getConfig().getInt("gem.shortcut.item.durability", 0));
                        if (player.getInventory().addItem(shortcut).isEmpty()){
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.get-shortcut", "&eSuccessfully add shortcut to your inventory.")).replace("&", "§"));
                        } else {
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.inventory-full", "&eYour inventory is full and you can't get the target item.")).replace("&", "§"));
                        }
                    }
                } else if (e.getRawSlot() == 13) {
                    player.openInventory(UpgradeMenu.generate());
                } else if (e.getRawSlot() == 15) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.openInventory(RankingMenu.generate(player));
                        }
                    }.runTask(SurvivalExpert.getInstance());
                } else if (e.getRawSlot() == 29) {
                    int exchangeRatio = SurvivalExpert.getInstance().getConfig().getInt("setting.point-exchange-ratio", 100);
                    PlayerData playerData = SurvivalExpert.getInstance().getStorage().getPlayerData(player.getName());
                    int battleTotal = playerData.getBattleTotal();
                    int battleUsed = playerData.getBattleUsed();
                    int canExchange = (battleTotal - battleUsed) / exchangeRatio;
                    if (canExchange > 0) {
                        ItemStack battleGem = GemCore.getGemItemStack(new GemInfo("Battle", 1));
                        if (e.getClick().isLeftClick()) {
                            if (player.getInventory().addItem(battleGem).isEmpty()) {
                                SurvivalExpert.getInstance().getStorage().updatePlayerData(player.getName(), "battle_used", battleUsed + exchangeRatio);
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.exchage-success", "&eSuccessfully exchange &b{number} {type} &egems.").replace("{number}", Integer.toString(1)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle"))).replace("&", "§"));
                            } else player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.inventory-full", "&eYour inventory is full and you can't get the target item.")).replace("&", "§"));
                        } else if (e.getClick().isRightClick()) {
                            battleGem.setAmount(canExchange);
                            if (player.getInventory().addItem(battleGem).isEmpty()) {
                                SurvivalExpert.getInstance().getStorage().updatePlayerData(player.getName(), "battle_used", battleUsed + exchangeRatio * canExchange);
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.exchage-success", "&eSuccessfully exchange &b{number} {type} &egems.").replace("{number}", Integer.toString(canExchange)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle"))).replace("&", "§"));
                            } else player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.inventory-full", "&eYour inventory is full and you can't get the target item.")).replace("&", "§"));
                        }
                    } else player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.not-enough", "&eYour points are not enough to exchange.")).replace("&", "§"));
                } else if (e.getRawSlot() == 31) {
                    player.openInventory(EnumMenu.generate());
                } else if (e.getRawSlot() == 33) {
                    int exchangeRatio = SurvivalExpert.getInstance().getConfig().getInt("setting.point-exchange-ratio", 100);
                    PlayerData playerData = SurvivalExpert.getInstance().getStorage().getPlayerData(player.getName());
                    int lifeTotal = playerData.getLifeTotal();
                    int lifeUsed = playerData.getLifeUsed();
                    int canExchange = (lifeTotal - lifeUsed) / exchangeRatio;
                    if (canExchange > 0) {
                        ItemStack lifeGem = GemCore.getGemItemStack(new GemInfo("Life", 1));
                        if (e.getClick().isLeftClick()) {
                            if (player.getInventory().addItem(lifeGem).isEmpty()) {
                                SurvivalExpert.getInstance().getStorage().updatePlayerData(player.getName(), "life_used",  lifeUsed + exchangeRatio);
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.exchage-success", "&eSuccessfully exchange &b{number} {type} &egems.").replace("{number}", Integer.toString(1)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife"))).replace("&", "§"));
                            } else player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.inventory-full", "&eYour inventory is full and you can't get the target item.")).replace("&", "§"));
                        } else if (e.getClick().isRightClick()) {
                            lifeGem.setAmount(canExchange);
                            if (player.getInventory().addItem(lifeGem).isEmpty()) {
                                SurvivalExpert.getInstance().getStorage().updatePlayerData(player.getName(), "life_used",  lifeUsed + exchangeRatio * canExchange);
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.exchage-success", "&eSuccessfully exchange &b{number} {type} &egems.").replace("{number}", Integer.toString(canExchange)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife"))).replace("&", "§"));
                            } else player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.inventory-full", "&eYour inventory is full and you can't get the target item.")).replace("&", "§"));
                        }
                    } else player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.not-enough", "&eYour points are not enough to exchange.")).replace("&", "§"));
                }
            } else if (menu.getHolder() instanceof EnumMenu.EnumMenuHolder || menu.getHolder() instanceof RankingMenu.RankingMenuHolder) {
                e.setCancelled(true);
            } else if (menu.getHolder() instanceof EquipMenu.EquipMenuHolder) {
                if (Arrays.asList(new Integer[]{1, 2, 3}).contains(e.getRawSlot())) {
                    e.setCancelled(true);
                } else {
                    if (e.getRawSlot() == 0) {
                        // 如果是空手点的，就是要卸下
                        if (e.getCursor() == null || "AIR".equals(e.getCursor().getType().toString())) {
                            // 如果不是插槽占位符就给卸下
                            if (!GemCore.isSlot(e.getCurrentItem())) {
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.un-equip", "&eYou removed your gem, the bonus will be removed.")).replace("&", "§"));
                            } else e.setCancelled(true);
                        // 如果是拿着宝石点的
                        } else {
                            GemInfo gemInfo = GemCore.getGemInfoByItemStack(e.getCursor());
                            if (gemInfo != null && "Battle".equals(gemInfo.getType()) && e.getCursor().getAmount() == 1) {
                                if (GemCore.isSlot(e.getCurrentItem())) {
                                    e.setCurrentItem(null);
                                }
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.gem-equip", "&eSuccessfully equipped {type} &egem, level &b{level}.").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{level}", Integer.toString(gemInfo.getLevel()))).replace("&", "§"));
                            } else e.setCancelled(true);
                        }
                    } else if (e.getRawSlot() == 4) {
                        // 如果是空手点的，就是要卸下
                        if (e.getCursor() == null || "AIR".equals(e.getCursor().getType().toString())) {
                            // 如果不是插槽占位符就给卸下
                            if (!GemCore.isSlot(e.getCurrentItem())) {
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.un-equip", "&eYou removed your gem, the bonus will be removed.")).replace("&", "§"));
                            } else e.setCancelled(true);
                        // 如果是拿着宝石点的
                        } else {
                            GemInfo gemInfo = GemCore.getGemInfoByItemStack(e.getCursor());
                            if (gemInfo != null && "Life".equals(gemInfo.getType()) && e.getCursor().getAmount() == 1) {
                                if (GemCore.isSlot(e.getCurrentItem())) {
                                    e.setCurrentItem(null);
                                }
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.gem-equip", "&eSuccessfully equipped {type} &egem, level &b{level}.").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife")).replace("{level}", Integer.toString(gemInfo.getLevel()))).replace("&", "§"));
                            } else e.setCancelled(true);
                        }
                    }
                }
            } else if (menu.getHolder() instanceof UpgradeMenu.UpgradeMenuHolder) {
                if (Arrays.asList(new Integer[]{1, 3, 4, 5, 6, 8}).contains(e.getRawSlot())) {
                    e.setCancelled(true);
                } else {
                    if (e.getRawSlot() == 0 || e.getRawSlot() == 2) {
                        if (e.getCursor() == null || "AIR".equals(e.getCursor().getType().toString())) {
                            if (!GemCore.isSlot(e.getCurrentItem())) {
                                menu.setItem(4, UpgradeMenu.getRateItemStack("0"));
                                menu.setItem(7, null);
                            } else e.setCancelled(true);
                        } else {
                            GemInfo gemInfo = GemCore.getGemInfoByItemStack(e.getCursor());
                            if (gemInfo != null && e.getCursor().getAmount() == 1) {
                                if (GemCore.isSlot(e.getCurrentItem())) {
                                    e.setCurrentItem(null);
                                }
                                GemInfo otherSideGemInfo = GemCore.getGemInfoByItemStack(menu.getItem(2 - e.getRawSlot()));
                                if (otherSideGemInfo != null && gemInfo.getType().equals(otherSideGemInfo.getType()) && gemInfo.getLevel() == otherSideGemInfo.getLevel() && gemInfo.getLevel() < 10) {
                                    BigDecimal range = new BigDecimal(SurvivalExpert.getInstance().getConfig().getDouble("setting.success-rate-base", 0.8)).pow(gemInfo.getLevel()).multiply(new BigDecimal(100));
                                    menu.setItem(4, UpgradeMenu.getRateItemStack(range.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%"));
                                    menu.setItem(7, GemCore.getGemItemStack(new GemInfo(gemInfo.getType(), gemInfo.getLevel() + 1)));
                                } else {
                                    menu.setItem(4, UpgradeMenu.getRateItemStack("0"));
                                    menu.setItem(7, null);
                                }
                            } else e.setCancelled(true);
                        }
                    } else if (e.getRawSlot() == 7) {
                        if (e.getCursor() == null || "AIR".equals(e.getCursor().getType().toString())) {
                            GemInfo gemInfo = GemCore.getGemInfoByItemStack(e.getCurrentItem());
                            if (gemInfo != null) {
                                BigDecimal range = new BigDecimal(SurvivalExpert.getInstance().getConfig().getDouble("setting.success-rate-base", 0.8)).pow(gemInfo.getLevel() - 1).multiply(new BigDecimal(SurvivalExpert.RANDOM_CALC_BASE));
                                int random = new Random().nextInt(SurvivalExpert.RANDOM_CALC_BASE);
                                if (random >= range.intValue()) {
                                    gemInfo.setLevel(gemInfo.getLevel() - 1);
                                    e.setCurrentItem(GemCore.getGemItemStack(gemInfo));
                                    player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.up-failed", "&eOps! The upgrade failed, you lost half of the gems.")).replace("&", "§"));
                                } else {
                                    player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.up-success", "&eCongratulations! Gem upgrade is successful!")).replace("&", "§"));
                                    Bukkit.broadcastMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.boardcast.upgrade", "&c{name} &ehas successfully combined &b{level} &elevel {type} &egems!").replace("{name}", player.getName()).replace("{level}", Integer.toString(gemInfo.getLevel())).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type." + gemInfo.getType().toLowerCase(), "Battle".equals(gemInfo.getType()) ? "&dBattle" : "&aLife"))).replace("&", "§"));
                                }
                                menu.setItem(0, null);
                                menu.setItem(2, null);
                                menu.setItem(4, UpgradeMenu.getRateItemStack("0"));
                            }
                        } else e.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * 关闭的时候如果有没拿的宝石就扔到地上，宝石装备在关闭时才保存进配置文件
     * @param e 事件
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player player = (Player) e.getPlayer();
        Inventory menu = e.getInventory();
        if (menu.getHolder() != null) {
            if (menu.getHolder() instanceof UpgradeMenu.UpgradeMenuHolder) {
                ItemStack itemStack1 = menu.getItem(0);
                if (itemStack1 != null && !GemCore.isSlot(itemStack1)) {
                    player.getWorld().dropItem(player.getLocation(), itemStack1);
                }
                ItemStack itemStack2 = menu.getItem(2);
                if (itemStack2 != null && !GemCore.isSlot(itemStack2)) {
                    player.getWorld().dropItem(player.getLocation(), itemStack2);
                }
            } else if (menu.getHolder() instanceof EquipMenu.EquipMenuHolder) {
                GemInfo battleGem = GemCore.getGemInfoByItemStack(menu.getItem(0));
                SurvivalExpert.getInstance().getStorage().updatePlayerData(player.getName(), "battle_gem", battleGem != null ? battleGem.getLevel() : 0);
                GemInfo lifeGem = GemCore.getGemInfoByItemStack(menu.getItem(4));
                SurvivalExpert.getInstance().getStorage().updatePlayerData(player.getName(), "life_gem", lifeGem != null ? lifeGem.getLevel() : 0);
            }
        }
    }

}
