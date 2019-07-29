package cn.daniellee.plugin.se.storage;

import cn.daniellee.plugin.se.model.PlayerData;

public class StorageConverter {

	private MysqlStorage mysqlStorage;

	private YamlStorage yamlStorage;

	public StorageConverter(MysqlStorage mysqlStorage, YamlStorage yamlStorage) {
		this.mysqlStorage = mysqlStorage;
		this.yamlStorage = yamlStorage;
	}

	public void execute() {
		for (PlayerData playerData : yamlStorage.getAllPlayerData()) {
			mysqlStorage.insertPlayerData(playerData.getName(), playerData.getBattleTotal(), playerData.getBattleUsed(), playerData.getBattleGem(), playerData.getLifeTotal(), playerData.getLifeUsed(), playerData.getLifeGem());
		}
	}
}
