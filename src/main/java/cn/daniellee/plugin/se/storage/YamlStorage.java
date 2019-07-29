package cn.daniellee.plugin.se.storage;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.model.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YamlStorage implements Storage {

	private File dataFile = new File(SurvivalExpert.getInstance().getDataFolder(), "player.yml");

	private FileConfiguration dataYaml = new YamlConfiguration();

	private Map<String, PlayerData> playerDataCache = new HashMap<>();

	@Override
	public boolean initialize() {
		try {
			if (!dataFile.exists()) dataFile.createNewFile();
			dataYaml.load(dataFile);
		} catch (Exception e) {
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]An error occurred in player / reward file load.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public PlayerData getPlayerData(String name) {
		PlayerData playerData = playerDataCache.get(name);
		if (playerData == null) playerData = refreshPlayerData(name);
		return playerData;
	}

	@Override
	public PlayerData refreshPlayerData(String name) {
		PlayerData playerData = new PlayerData(name);
		playerData.setBattleTotal(dataYaml.getInt(name + ".battle.total", 0));
		playerData.setBattleUsed(dataYaml.getInt(name + ".battle.used", 0));
		playerData.setBattleGem(dataYaml.getInt(name + ".battle.gem", 0));
		playerData.setLifeTotal(dataYaml.getInt(name + ".life.total", 0));
		playerData.setLifeUsed(dataYaml.getInt(name + ".life.used", 0));
		playerData.setLifeGem(dataYaml.getInt(name + ".life.gem", 0));
		playerDataCache.put(name, playerData);
		return playerData;
	}

	@Override
	public void updatePlayerData(String name, String type, int number) {
		PlayerData playerData = getPlayerData(name);
		switch (type) {
			case "battle_total":
				dataYaml.set(name + ".battle.total", number);
				playerData.setBattleTotal(number);
				break;
			case "battle_used":
				dataYaml.set(name + ".battle.used", number);
				playerData.setBattleUsed(number);
				break;
			case "battle_gem":
				dataYaml.set(name + ".battle.gem", number);
				playerData.setBattleGem(number);
				break;
			case "life_total":
				dataYaml.set(name + ".life.total", number);
				playerData.setLifeTotal(number);
				break;
			case "life_used":
				dataYaml.set(name + ".life.used", number);
				playerData.setLifeUsed(number);
				break;
			case "life_gem":
				dataYaml.set(name + ".life.gem", number);
				playerData.setLifeGem(number);
				break;
		}
		saveDataYaml();
	}

	@Override
	public List<PlayerData> getAllPlayerData() {
		List<PlayerData> allPlayerData = new ArrayList<>();
		Set<String> names = dataYaml.getKeys(false);
		for (String name : names) {
			PlayerData playerData = playerDataCache.get(name);
			if (playerData == null) {
				playerData = new PlayerData(name);
				playerData.setName(name);
				playerData.setBattleTotal(dataYaml.getInt(name + ".battle.total", 0));
				playerData.setBattleUsed(dataYaml.getInt(name + ".battle.used", 0));
				playerData.setBattleGem(dataYaml.getInt(name + ".battle.gem", 0));
				playerData.setLifeTotal(dataYaml.getInt(name + ".life.total", 0));
				playerData.setLifeUsed(dataYaml.getInt(name + ".life.used", 0));
				playerData.setLifeGem(dataYaml.getInt(name + ".life.gem", 0));
			}
			allPlayerData.add(playerData);
		}
		return allPlayerData;
	}

	private void saveDataYaml() {
		try {
			dataYaml.save(dataFile);
		} catch (IOException e) {
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]An error occurred in player file save.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
			e.printStackTrace();
		}
	}
}
