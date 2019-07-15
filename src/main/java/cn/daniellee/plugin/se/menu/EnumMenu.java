package cn.daniellee.plugin.se.menu;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.component.ItemGenerator;
import cn.daniellee.plugin.se.menu.holder.EnumMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class EnumMenu {

	public static Inventory generate() {
		Inventory menu = Bukkit.createInventory(new EnumMenuHolder(), 45, SurvivalExpert.getInstance().getConfig().getString("menu.main.title", "&b&lOre / Crop").replace("&", "§"));

		// 填充角
		int cornerPosition[] = new int[]{ 18, 20, 22, 24, 26 };
		for (int i : cornerPosition) menu.setItem(i, Common.corner);
		// 填充边
		int[] borderPosition = new int[]{ 19, 21, 23, 25 };
		for (int i : borderPosition) menu.setItem(i, Common.border);

		List<String> oreBlocks = SurvivalExpert.getInstance().getOreBlocks();
		for (int i = 0; i < oreBlocks.size(); i++) {
			String blockType = oreBlocks.get(i);
			String convertItem = SurvivalExpert.getInstance().getCovertItemBlock().get(blockType);
			if (convertItem != null) blockType = convertItem;
			SurvivalExpert.getInstance().getConfig().getString("message.type.ore", "&dOre");
			menu.setItem(i, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.enum.block.name", "{type}&e: {name}").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.ore", "&dOre")).replace("{name}", blockType), null, blockType, 0));
		}

		List<String> cropBlocks = SurvivalExpert.getInstance().getCropBlocks();
		for (int i = 0; i < cropBlocks.size(); i++) {
			String blockType = cropBlocks.get(i);
			String convertItem = SurvivalExpert.getInstance().getCovertItemBlock().get(blockType);
			if (convertItem != null) blockType = convertItem;
			SurvivalExpert.getInstance().getConfig().getString("message.type.crop", "&aCrop");
			menu.setItem(i + 27, ItemGenerator.getItem(SurvivalExpert.getInstance().getConfig().getString("menu.enum.block.name", "{type}&e: {name}").replace("{type}", SurvivalExpert.getInstance().getConfig().getString("message.type.crop", "&aCrop")).replace("{name}", blockType), null, blockType, 0));
		}

		return menu;
	}


}
