package cn.daniellee.plugin.se.storage;

import cn.daniellee.plugin.se.SurvivalExpert;
import cn.daniellee.plugin.se.model.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlStorage implements Storage {

	private Connection connection;

	private Map<String, PlayerData> playerDataCache = new HashMap<>();

	private String tablePrefix;

	@Override
	public boolean initialize() {
		// 初始化Mysql驱动
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]An error occurred while getting the Mysql database driver.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
			return false;
		}
		// 初始化连接
		String url = "jdbc:mysql://" + SurvivalExpert.getInstance().getConfig().getString("storage.mysql.host", "localhost") + ":" + SurvivalExpert.getInstance().getConfig().getInt("storage.mysql.port", 3306) + "/" + SurvivalExpert.getInstance().getConfig().getString("storage.mysql.database", "minecraft") + "?" + SurvivalExpert.getInstance().getConfig().getString("storage.mysql.parameter", "characterEncoding=utf-8&useSSL=false");
		try {
			connection = DriverManager.getConnection(url, SurvivalExpert.getInstance().getConfig().getString("storage.mysql.username", "username"), SurvivalExpert.getInstance().getConfig().getString("storage.mysql.password", "password"));
		} catch (SQLException e) {
			e.printStackTrace();
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]Mysql connection information is incorrect.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
			return false;
		}
		// 初始化数据表
		tablePrefix = SurvivalExpert.getInstance().getConfig().getString("storage.mysql.table_perfix", "se_");
		String sql = "CREATE TABLE IF NOT EXISTS `" + tablePrefix + "player` (" +
				"`name` varchar(48) NOT NULL," +
				"`battle_total` int(11) DEFAULT '0'," +
				"`battle_used` int(11) DEFAULT '0'," +
				"`battle_gem` int(11) DEFAULT '0'," +
				"`life_total` int(11) DEFAULT '0'," +
				"`life_used` int(11) DEFAULT '0'," +
				"`life_gem` int(11) DEFAULT '0'," +
				"PRIMARY KEY (`name`)," +
				"KEY `name_UNIQUE` (`name`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]An error occurred while initializing the Mysql data table.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ignored) { }
			}
		}
		return true;
	}

	@Override
	public PlayerData getPlayerData(String name) {
		PlayerData playerData = playerDataCache.get(name);
		if (playerData == null) playerData = refreshPlayerData(name);
		return playerData;
	}

	@Override
	public PlayerData refreshPlayerData(String name) {
		PreparedStatement statement = null;
		try {
			String sql = "select * from " + tablePrefix + "player where name = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				PlayerData playerData = new PlayerData(name);
				playerData.setName(resultSet.getString("name"));
				playerData.setBattleTotal(resultSet.getInt("battle_total"));
				playerData.setBattleUsed(resultSet.getInt("battle_used"));
				playerData.setBattleGem(resultSet.getInt("battle_gem"));
				playerData.setLifeTotal(resultSet.getInt("life_total"));
				playerData.setLifeUsed(resultSet.getInt("life_used"));
				playerData.setLifeGem(resultSet.getInt("life_gem"));
				playerDataCache.put(name, playerData);
				return playerData;
			} else insertPlayerData(name, 0, 0, 0, 0, 0, 0);
		} catch (SQLException e) {
			e.printStackTrace();
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]An error occurred while reading player data from Mysql.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ignored) { }
			}
		}
		return null;
	}

	@Override
	public void updatePlayerData(String name, String type, int number) {
		PreparedStatement statement = null;
		try {
			String sql = "update " + tablePrefix + "player set " + type + " = ? where name = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, number);
			statement.setString(2, name);
			statement.executeUpdate();
			PlayerData playerData = getPlayerData(name);
			switch (type) {
				case "battle_total":
					playerData.setBattleTotal(number);
					break;
				case "battle_used":
					playerData.setBattleUsed(number);
					break;
				case "battle_gem":
					playerData.setBattleGem(number);
					break;
				case "life_total":
					playerData.setLifeTotal(number);
					break;
				case "life_used":
					playerData.setLifeUsed(number);
					break;
				case "life_gem":
					playerData.setLifeGem(number);
					break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]An error occurred while updating player data from Mysql.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ignored) { }
			}
		}
	}

	@Override
	public List<PlayerData> getAllPlayerData() {
		List<PlayerData> allPlayerData = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			String sql = "select * from " + tablePrefix + "player";
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				PlayerData playerData = new PlayerData(resultSet.getString("name"));
				playerData.setBattleTotal(resultSet.getInt("battle_total"));
				playerData.setBattleUsed(resultSet.getInt("battle_used"));
				playerData.setBattleGem(resultSet.getInt("battle_gem"));
				playerData.setLifeTotal(resultSet.getInt("life_total"));
				playerData.setLifeUsed(resultSet.getInt("life_used"));
				playerData.setLifeGem(resultSet.getInt("life_gem"));
				allPlayerData.add(playerData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]An error occurred while reading player data from Mysql.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ignored) { }
			}
		}
		return allPlayerData;
	}

	void insertPlayerData(String name, int battleTotal, int battleUsed, int battleGem, int lifeTotal, int lifeUsed, int lifeGem) {
		PreparedStatement statement = null;
		try {
			String sql = "insert into " + tablePrefix + "player (name,battle_total,battle_used,battle_gem,life_total,life_used,life_gem)values(?,?,?,?,?,?,?) on duplicate key update battle_total = ?,battle_used = ?,battle_gem = ?,life_total = ?,life_used = ?,life_gem = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setInt(2, battleTotal);
			statement.setInt(3, battleUsed);
			statement.setInt(4, battleGem);
			statement.setInt(5, lifeTotal);
			statement.setInt(6, lifeUsed);
			statement.setInt(7, lifeGem);
			statement.setInt(8, battleTotal);
			statement.setInt(9, battleUsed);
			statement.setInt(10, battleGem);
			statement.setInt(11, lifeTotal);
			statement.setInt(12, lifeUsed);
			statement.setInt(13, lifeGem);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			SurvivalExpert.getInstance().getLogger().info(" ");
			SurvivalExpert.getInstance().getLogger().info("[SurvivalExpert]An error occurred while inserting player data from Mysql.".replace("&", "§"));
			SurvivalExpert.getInstance().getLogger().info(" ");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ignored) { }
			}
		}
	}
}
