package cn.daniellee.plugin.se.component;

import cn.daniellee.plugin.se.SurvivalExpert;
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
//			case "title":
//				int segment = plugin.getPlayerData().getInt(player.getName() + ".segment", 0);
//				return BattleCore.segmentToTitle(segment);
//			case "score":
//				int score = plugin.getPlayerData().getInt(player.getName() + ".score", 0);
//				return String.valueOf(score);
//			case "times":
//				int times = plugin.getPlayerData().getInt(player.getName() + ".times", 0);
//				return String.valueOf(times);
		}

		return null;
	}
}
