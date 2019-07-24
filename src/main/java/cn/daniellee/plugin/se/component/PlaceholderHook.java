package cn.daniellee.plugin.se.component;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.core.GemCore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * PlaceholderAPI扩展
 */
public class PlaceholderHook extends PlaceholderExpansion {

	private SurvivalExpert plugin;

	public PlaceholderHook(SurvivalExpert plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean persist(){
		return true;
	}

	@Override
	public boolean canRegister(){
		return true;
	}

	@Override
	public String getAuthor(){
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public String getIdentifier(){
		return "expert";
	}

	@Override
	public String getVersion(){
		return plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier){

		if (player == null) return "";

		switch (identifier) {
			case "battle_gem":
				return Integer.toString(GemCore.getBattleLevel(player.getName()));
			case "life_gem":
				return Integer.toString(GemCore.getLifeLevel(player.getName()));
			case "battle_total":
				return Integer.toString(SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.total", 0));
			case "life_total":
				return Integer.toString(SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.total", 0));
			case "battle_usable":
				return Integer.toString(SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.total", 0) - SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".battle.used", 0));
			case "life_usable":
				return Integer.toString(SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.total", 0) - SurvivalExpert.getInstance().getPlayerData().getInt(player.getName() + ".life.used", 0));
		}

		return null;
	}
}
