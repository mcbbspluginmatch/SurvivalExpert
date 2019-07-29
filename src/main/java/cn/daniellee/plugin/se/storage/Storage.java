package cn.daniellee.plugin.se.storage;

import cn.daniellee.plugin.se.model.PlayerData;

import java.util.List;

public interface Storage {

	boolean initialize();

	PlayerData getPlayerData(String name);

	PlayerData refreshPlayerData(String name);

	void updatePlayerData(String name, String type, int number);

	List<PlayerData> getAllPlayerData();
}
