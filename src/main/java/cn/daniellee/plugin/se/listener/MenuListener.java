package cn.daniellee.plugin.se.listener;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.core.GemCore;
import cn.daniellee.plugin.se.menu.EnumMenu;
import cn.daniellee.plugin.se.menu.EquipMenu;
import cn.daniellee.plugin.se.menu.RankingMenu;
import cn.daniellee.plugin.se.menu.UpgradeMenu;
import cn.daniellee.plugin.se.menu.holder.*;
import cn.daniellee.plugin.se.model.GemInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
            if (menu.getHolder() instanceof MainMenuHolder) {
                e.setCancelled(true);
                if (e.getRawSlot() == 11) {
                    if (e.getClick().isLeftClick()) {
                        player.openInventory(EquipMenu.generate(player));
                    } else if (e.getClick().isRightClick()) {
                        List<String> shortcutLore = SurvivalExpert.getInstance().getConfig().getStringList("gem.shortcut.lore");
                        shortcutLore.add(0, GemCore.SHORTCUT_LORE);
                        ItemStack shortcut = ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("gem.shortcut.name", "&7[&6SurvivalExpert&7]&bGem equipment bar"), shortcutLore, SurvivalExpert.getInstance().getConfig().getString("gem.shortcut.item.material", "SNOWBALL"), SurvivalExpert.getInstance().getConfig().getInt("gem.shortcut.item.durability", 0));
                        player.getInventory().addItem(shortcut);
                        if (player.getInventory().contains(shortcut)){
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.get-shortcut", "&eSuccessfully add shortcut to your inventory.")).replace("&", "§"));
                        } else {
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.inventory-full", "&eYour inventory is full and you can't get the target item.")).replace("&", "§"));
                        }
                    }
                } else if (e.getRawSlot() == 13) {
                    player.openInventory(UpgradeMenu.generate());
                } else if (e.getRawSlot() == 15) {
                    player.openInventory(RankingMenu.generate(player));
                } else if (e.getRawSlot() == 29) {
                    int exchangeRatio = SurvivalExpert.getInstance().getConfig().getInt("setting.point-exchange-ratio", 100);
                    int battleTotal = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.total", 0);
                    int battleUsed = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.used", 0);
                    int canExchange = (battleTotal - battleUsed) / exchangeRatio;
                    if (canExchange > 0) {
                        ItemStack battleGem = GemCore.getGemItemStack(new GemInfo("Battle", 1));
                        if (e.getClick().isLeftClick()) {
                            player.getInventory().addItem(battleGem);
                            SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".battle.used", battleUsed + exchangeRatio);
                            SurvivalExpert.getInstance().savePlayerData();
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.exchage-success", "&eSuccessfully exchange &b{number} {type} &egems.").replace("{number}", Integer.toString(1)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle"))).replace("&", "§"));
                        } else if (e.getClick().isRightClick()) {
                            battleGem.setAmount(canExchange);
                            player.getInventory().addItem(battleGem);
                            SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".battle.used", battleUsed + exchangeRatio * canExchange);
                            SurvivalExpert.getInstance().savePlayerData();
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.exchage-success", "&eSuccessfully exchange &b{number} {type} &egems.").replace("{number}", Integer.toString(canExchange)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle"))).replace("&", "§"));
                        }
                    } else player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.not-enough", "&eYour points are not enough to exchange.")).replace("&", "§"));
                } else if (e.getRawSlot() == 31) {
                    player.openInventory(EnumMenu.generate());
                } else if (e.getRawSlot() == 33) {
                    int exchangeRatio = SurvivalExpert.getInstance().getConfig().getInt("setting.point-exchange-ratio", 100);
                    int lifeTotal = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.total", 0);
                    int lifeUsed = SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.used", 0);
                    int canExchange = (lifeTotal - lifeUsed) / exchangeRatio;
                    if (canExchange > 0) {
                        ItemStack lifeGem = GemCore.getGemItemStack(new GemInfo("Life", 1));
                        if (e.getClick().isLeftClick()) {
                            player.getInventory().addItem(lifeGem);
                            SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".life.used", lifeUsed + exchangeRatio);
                            SurvivalExpert.getInstance().savePlayerData();
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.exchage-success", "&eSuccessfully exchange &b{number} {type} &egems.").replace("{number}", Integer.toString(1)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife"))).replace("&", "§"));
                        } else if (e.getClick().isRightClick()) {
                            lifeGem.setAmount(canExchange);
                            player.getInventory().addItem(lifeGem);
                            SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".life.used", lifeUsed + exchangeRatio * canExchange);
                            SurvivalExpert.getInstance().savePlayerData();
                            player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.exchage-success", "&eSuccessfully exchange &b{number} {type} &egems.").replace("{number}", Integer.toString(canExchange)).replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife"))).replace("&", "§"));
                        }
                    } else player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.not-enough", "&eYour points are not enough to exchange.")).replace("&", "§"));
                }
            } else if (menu.getHolder() instanceof EnumMenuHolder || menu.getHolder() instanceof RankingMenuHolder) {
                e.setCancelled(true);
            } else if (menu.getHolder() instanceof EquipMenuHolder) {
                if (Arrays.asList(new Integer[]{1, 2, 3}).contains(e.getRawSlot())) {
                    e.setCancelled(true);
                } else {
                    if (e.getRawSlot() == 0) {
                        // 如果是空手点的，就是要卸下
                        if ("AIR".equals(e.getCursor().getType().toString())) {
                            // 如果不是插槽占位符就给卸下
                            if (!GemCore.isSlot(e.getCurrentItem())) {
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.un-equip", "&eYou removed your gem, the bonus will be removed.")).replace("&", "§"));
                                // 立即设置伤害加成缓存
                                GemCore.damageBonusCache.put(player.getName(), 0);
                            } else e.setCancelled(true);
                            // 如果是拿着宝石点的
                        } else {
                            GemInfo gemInfo = GemCore.getGemInfoByItemStack(e.getCursor());
                            if (gemInfo != null && "Battle".equals(gemInfo.getType()) && e.getCursor().getAmount() == 1) {
                                if (GemCore.isSlot(e.getCurrentItem())) {
                                    e.setCurrentItem(null);
                                }
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.gem-equip", "&eSuccessfully equipped {type} &egem, level &b{level}.").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.battle", "&dBattle")).replace("{level}", Integer.toString(gemInfo.getLevel()))).replace("&", "§"));
                                // 立即设置伤害加成缓存
                                GemCore.damageBonusCache.put(player.getName(), 5 * gemInfo.getLevel());
                            } else e.setCancelled(true);
                        }
                    } else if (e.getRawSlot() == 4) {
                        // 如果是空手点的，就是要卸下
                        if ("AIR".equals(e.getCursor().getType().toString())) {
                            // 如果不是插槽占位符就给卸下
                            if (!GemCore.isSlot(e.getCurrentItem())) {
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.un-equip", "&eYou removed your gem, the bonus will be removed.")).replace("&", "§"));
                                // 立即设置生命加成缓存
                                GemCore.healthBonusCache.put(player.getName(), 0);
                                // 立即设置最大生命值
                                player.setHealthScale(20);
                                player.setMaxHealth(20);
                                player.setHealth(20);
                            } else e.setCancelled(true);
                            // 如果是拿着宝石点的
                        } else {
                            GemInfo gemInfo = GemCore.getGemInfoByItemStack(e.getCursor());
                            if (gemInfo != null && "Life".equals(gemInfo.getType()) && e.getCursor().getAmount() == 1) {
                                if (GemCore.isSlot(e.getCurrentItem())) {
                                    e.setCurrentItem(null);
                                }
                                player.sendMessage((SurvivalExpert.getInstance().getPrefix() + SurvivalExpert.getInstance().getConfig().getString("message.gem-equip", "&eSuccessfully equipped {type} &egem, level &b{level}.").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.life", "&aLife")).replace("{level}", Integer.toString(gemInfo.getLevel()))).replace("&", "§"));
                                // 立即设置生命加成缓存
                                GemCore.healthBonusCache.put(player.getName(), 5 * gemInfo.getLevel());
                                // 立即设置最大生命值
                                player.setHealthScale(20 + 5 * gemInfo.getLevel());
                                player.setMaxHealth(20 + 5 * gemInfo.getLevel());
                                player.setHealth(20 + 5 * gemInfo.getLevel());
                            } else e.setCancelled(true);
                        }
                    }
                }
            } else if (menu.getHolder() instanceof UpgradeMenuHolder) {
                if (Arrays.asList(new Integer[]{1, 3, 4, 5, 6, 8}).contains(e.getRawSlot())) {
                    e.setCancelled(true);
                } else {
                    if (e.getRawSlot() == 0 || e.getRawSlot() == 2) {
                        if ("AIR".equals(e.getCursor().getType().toString())) {
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
                                if (otherSideGemInfo != null && gemInfo.getType().equals(otherSideGemInfo.getType()) && gemInfo.getLevel() == otherSideGemInfo.getLevel()) {
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
                        if ("AIR".equals(e.getCursor().getType().toString())) {
                            GemInfo gemInfo = GemCore.getGemInfoByItemStack(e.getCurrentItem());
                            if (gemInfo != null) {
                                BigDecimal range = new BigDecimal(SurvivalExpert.getInstance().getConfig().getDouble("setting.success-rate-base", 0.8)).pow(gemInfo.getLevel() - 1).multiply(new BigDecimal(100));
                                int random = new Random().nextInt(100);
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
            if (menu.getHolder() instanceof UpgradeMenuHolder) {
                if (menu.getItem(0) != null) {
                    player.getWorld().dropItem(player.getLocation(), menu.getItem(0));
                }
                if (menu.getItem(2) != null) {
                    player.getWorld().dropItem(player.getLocation(), menu.getItem(2));
                }
            } else if (menu.getHolder() instanceof EquipMenuHolder) {
                GemInfo battleGem = GemCore.getGemInfoByItemStack(menu.getItem(0));
                if (battleGem != null) {
                    SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".battle.gem", battleGem.getLevel());
                    SurvivalExpert.getInstance().savePlayerData();
                    // 重新设置伤害加成缓存
                    GemCore.damageBonusCache.put(player.getName(), 5 * battleGem.getLevel());
                } else {
                    SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".battle.gem", null);
                    SurvivalExpert.getInstance().savePlayerData();
                    // 重新设置伤害加成缓存
                    GemCore.damageBonusCache.put(player.getName(), 0);
                }
                GemInfo lifeGem = GemCore.getGemInfoByItemStack(menu.getItem(4));
                if (lifeGem != null) {
                    SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".life.gem", lifeGem.getLevel());
                    SurvivalExpert.getInstance().savePlayerData();
                    // 立即设置生命加成缓存
                    GemCore.healthBonusCache.put(player.getName(), 5 * lifeGem.getLevel());
                    // 重新设置最大生命值
                    player.setHealthScale(20 + 5 * lifeGem.getLevel());
                    player.setMaxHealth(20 + 5 * lifeGem.getLevel());
                    player.setHealth(20 + 5 * lifeGem.getLevel());
                } else {
                    SurvivalExpert.getInstance().getPlayerData().set(player.getName() + ".life.gem", null);
                    SurvivalExpert.getInstance().savePlayerData();
                    // 立即设置生命加成缓存
                    GemCore.healthBonusCache.put(player.getName(), 0);
                    // 重新设置最大生命值
                    player.setHealthScale(20);
                    player.setMaxHealth(20);
                    player.setHealth(20);
                }
            }
        }
    }

}
