package cn.daniellee.plugin.se.menu;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import org.bukkit.inventory.ItemStack;

class Common {

	public static ItemStack getCorner() {
		return ItemGenerator.getItem(" ", null, SurvivalExpert.getInstance().getConfig().getString("menu.common.corner.material", "MAGENTA_STAINED_GLASS_PANE"),  SurvivalExpert.getInstance().getConfig().getInt("menu.common.corner.durability", 0));
	}

	public static ItemStack getBorder() {
		return ItemGenerator.getItem(" ", null, SurvivalExpert.getInstance().getConfig().getString("menu.common.border.material", "BLUE_STAINED_GLASS_PANE"),  SurvivalExpert.getInstance().getConfig().getInt("menu.common.border.durability", 0));
	}

}
