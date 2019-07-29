package cn.daniellee.plugin.se.core;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.model.GemInfo;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.regex.Pattern;

public class GemCore {

	public static final String GEM_LORE = "§3SurvivalExpert:{type}:{level}";

	public static final String SLOT_LORE = "§3SurvivalExpert:Slot";

	public static final String SHORTCUT_LORE = "§3SurvivalExpert:Shortcut";

	public static final Pattern GEM_LORE_PATTERN = Pattern.compile("^§3SurvivalExpert:(Battle|Life):\\d+$");

	public static ItemStack getGemItemStack(GemInfo gemInfo) {
		List<String> gemLore = SurvivalExpert.getInstance().getConfig().getStringList("gem." + gemInfo.getType().toLowerCase() + ".lore");
		gemLore.add(0, GEM_LORE.replace("{type}", gemInfo.getType()).replace("{level}", Integer.toString(gemInfo.getLevel())));
		return ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("gem." + gemInfo.getType().toLowerCase() + ".name", "&7[&6SurvivalExpert&7]" + ("Battle".equals(gemInfo.getType()) ? "&d" : "&a") + gemInfo.getType() + "&bGem&7[&eLv.{level}&7]").replace("{level}", Integer.toString(gemInfo.getLevel())), gemLore, SurvivalExpert.getInstance().getConfig().getString("gem." + gemInfo.getType().toLowerCase() + ".item.material", "Battle".equals(gemInfo.getType()) ? "MAGMA_CREAM" : "SLIME_BALL"), SurvivalExpert.getInstance().getConfig().getInt("gem." + gemInfo.getType().toLowerCase() + ".item.durability", 0));
	}

	public static boolean isShortcut(ItemStack itemStack) {
		if (itemStack != null) {
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (itemMeta != null) {
				List<String> lore = itemMeta.getLore();
				if (lore != null && !lore.isEmpty()) {
					return SHORTCUT_LORE.equals(lore.get(0));
				}
			}
		}
		return false;
	}

	public static boolean isSlot(ItemStack itemStack) {
		if (itemStack != null) {
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (itemMeta != null) {
				List<String> lore = itemMeta.getLore();
				if (lore != null && !lore.isEmpty()) {
					return SLOT_LORE.equals(lore.get(0));
				}
			}
		}
		return false;
	}

	public static GemInfo getGemInfoByItemStack(ItemStack itemStack) {
		if (itemStack != null) {
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (itemMeta != null) {
				List<String> lore = itemMeta.getLore();
				if (lore != null && !lore.isEmpty()) {
					String firstLore = lore.get(0);
					if (GEM_LORE_PATTERN.matcher(firstLore).matches()) {
						GemInfo gemInfo = new GemInfo();
						gemInfo.setType(firstLore.substring(firstLore.indexOf(":") + 1, firstLore.lastIndexOf(":")));
						gemInfo.setLevel(Integer.valueOf(firstLore.substring(firstLore.lastIndexOf(":") + 1)));
						return gemInfo;
					}
				}
			}
		}
		return null;
	}
}
