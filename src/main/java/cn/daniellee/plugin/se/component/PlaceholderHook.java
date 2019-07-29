package cn.daniellee.plugin.se.component;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.core.GemCore;
import cn.daniellee.plugin.se.model.PlayerData;
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

		PlayerData playerData = SurvivalExpert.getInstance().getStorage().getPlayerData(player.getName());
		switch (identifier) {
			case "battle_gem":
				return Integer.toString(playerData.getBattleGem());
			case "life_gem":
				return Integer.toString(playerData.getLifeGem());
			case "battle_total":
				return Integer.toString(playerData.getBattleTotal());
			case "life_total":
				return Integer.toString(playerData.getLifeTotal());
			case "battle_usable":
				return Integer.toString(playerData.getBattleTotal() - playerData.getBattleUsed());
			case "life_usable":
				return Integer.toString(playerData.getLifeTotal() - playerData.getLifeUsed());
		}

		return null;
	}
}
