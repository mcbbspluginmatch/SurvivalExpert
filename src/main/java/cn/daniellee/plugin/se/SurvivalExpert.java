package cn.daniellee.plugin.se;

import cn.daniellee.plugin.se.command.ExpertCommand;
import cn.daniellee.plugin.se.component.PlaceholderHook;
import cn.daniellee.plugin.se.listener.MenuListener;
import cn.daniellee.plugin.se.listener.PlayerListener;
import cn.daniellee.plugin.se.storage.MysqlStorage;
import cn.daniellee.plugin.se.storage.Storage;
import cn.daniellee.plugin.se.storage.StorageConverter;
import cn.daniellee.plugin.se.storage.YamlStorage;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurvivalExpert extends JavaPlugin {

	public static final int RANDOM_CALC_BASE = 10000;

	private static SurvivalExpert instance;

	private String prefix;

	private Storage storage;

	private BigDecimal battleBonusPercentage;

	private BigDecimal lifeBonusPercentage;

	private List<String> oreBlocks;

	private List<String> cropBlocks;

	private int orePointRange;

	private int cropPointRange;

	// 需要检查Age属性的方块
	private Map<String, Integer> ageCheckBlock = new HashMap<>();

	private Map<String, String> covertItemBlock = new HashMap<>();

	private PlaceholderExpansion placeholderHook;

	public void onEnable(){
		instance = this;

		loadConfig();
		getLogger().info(" ");
		getLogger().info(">>>>>>>>>>>>>>>>>>>>>>>> SurvivalExpert Loaded <<<<<<<<<<<<<<<<<<<<<<<<");
		getLogger().info(">>>>> If you encounter any bugs, please contact author's QQ: 768318841 <<<<<");
		getLogger().info(" ");

		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

		Bukkit.getPluginCommand("survivalexpert").setExecutor(new ExpertCommand());

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		// 挂钩PlaceholderAPI
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			placeholderHook = new PlaceholderHook(this);
			placeholderHook.register();
			getLogger().info(" ");
			getLogger().info(">>>>>>>>>>>>>>>>>>>> Successfully hook to PlaceholderAPI <<<<<<<<<<<<<<<<<<<".replace("&", "§"));
			getLogger().info(" ");
		}

		ageCheckBlock.put("WHEAT", 7);
		ageCheckBlock.put("CARROTS", 7);
		ageCheckBlock.put("POTATOS", 7);
		ageCheckBlock.put("BEETROOTS", 3);
		ageCheckBlock.put("COCOA", 8);
		ageCheckBlock.put("NETHER_WARTS", 3);
		ageCheckBlock.put("CHORUS_FLOWER", 5);
		ageCheckBlock.put("SUGAR_CANE", 1);
		ageCheckBlock.put("BAMBOO", 1);
		//LAGECY
		ageCheckBlock.put("CROPS", 7);
		ageCheckBlock.put("CARROT", 7);
		ageCheckBlock.put("POTATO", 7);

		covertItemBlock.put("CARROTS", "CARROT");
		covertItemBlock.put("POTATOS", "POTATO");
		covertItemBlock.put("BEETROOTS", "BEETROOT");
		covertItemBlock.put("COCOA", "COCOA_BEANS");
		covertItemBlock.put("NETHER_WARTS", "NETHER_WART");
		//LAGECY
		covertItemBlock.put("CROPS", "WHEAT");
		covertItemBlock.put("CARROT", "CARROT_ITEM");
		covertItemBlock.put("POTATO", "POTATO_ITEM");
		covertItemBlock.put("BEETROOT_BLOCK", "BEETROOT");
	}

	public void loadConfig() {
		getLogger().info("[SurvivalExpert]Loading config...");
		if(!getDataFolder().exists()) getDataFolder().mkdirs();
		storage = getConfig().getBoolean("storage.mysql.use", false) ? new MysqlStorage() : new YamlStorage();
		if (storage.initialize()) {
			getLogger().info("[SurvivalExpert]Storage initialized.");
		} else {
			getLogger().info(" ");
			getLogger().info("[SurvivalExpert]Initializing data store failed, please edit the config and reload the plugin.".replace("&", "§"));
			getLogger().info(" ");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		if (getConfig().getBoolean("storage.mysql.use", false) && getConfig().getBoolean("storage.mysql.convert", false)) {
			Storage yamlStorage = new YamlStorage();
			if (yamlStorage.initialize()) {
				StorageConverter converter = new StorageConverter((MysqlStorage) storage, (YamlStorage) yamlStorage);
				converter.execute();
				getConfig().set("storage.mysql.convert", false);
				saveConfig();
				getLogger().info("[SurvivalExpert]Successfully transferred Yaml data to Mysql.");
			} else {
				getLogger().info(" ");
				getLogger().info("[SurvivalExpert]Yaml data store initialization failed, data conversion canceled.".replace("&", "§"));
				getLogger().info(" ");
			}
		}
		prefix = "&7[&6" + getConfig().getString("prompt-prefix", "SurvivalExpert") + "&7] &3: &r";
		battleBonusPercentage = new BigDecimal(getConfig().getDouble("gem.battle.bonus-percentage", 0.1));
		lifeBonusPercentage = new BigDecimal(getConfig().getDouble("gem.life.bonus-percentage", 0.1));
		oreBlocks = Arrays.asList(getConfig().getString("setting.ore.blocks", "COAL_ORE,IRON_ORE,GOLD_ORE,DIAMOND_ORE,EMERALD_ORE,LAPIS_ORE,REDSTONE_ORE,NETHER_QUARTZ_ORE").split(","));
		cropBlocks = Arrays.asList(getConfig().getString("setting.crop.blocks", "WHEAT,CARROTS,POTATOS,BEETROOTS,COCOA,NETHER_WARTS,CHORUS_FLOWER,SUGAR_CANE,PUMPKIN,MELON,CACTUS,BAMBOO").split(","));
		double orePointChange = getConfig().getDouble("setting.ore.chance", 1);
		if (orePointChange > 1 || orePointChange < 0.01) {
			getLogger().info("[SurvivalExpert]The chance can only be between 0.01 and 1, it is automatically set to 1.".replace("&", "§"));
			orePointChange = 1;
		}
		// 计算几率范围
		orePointRange = new BigDecimal(orePointChange).multiply(new BigDecimal(RANDOM_CALC_BASE)).intValue();
		double cropPointChange = getConfig().getDouble("setting.crop.chance", 1);
		if (cropPointChange > 1 || cropPointChange < 0.01) {
			getLogger().info("[SurvivalExpert]The chance can only be between 0.01 and 1, it is automatically set to 1.".replace("&", "§"));
			cropPointChange = 1;
		}
		cropPointRange = new BigDecimal(cropPointChange).multiply(new BigDecimal(RANDOM_CALC_BASE)).intValue();
		saveDefaultConfig();
	}

	@Override
	public void onDisable() {
		getLogger().info(" ");
		getLogger().info(">>>>>>>>>>>>>>>>>>>>>>>> SurvivalExpert Unloaded <<<<<<<<<<<<<<<<<<<<<<<<");
		getLogger().info(" ");

		// 解挂PlaceholderAPI
		if (placeholderHook != null) {
			PlaceholderAPI.unregisterExpansion(placeholderHook);
			getLogger().info(" ");
			getLogger().info(">>>>>>>>>>>>>>>>>>>>>>>>>> Unhook to PlaceholderAPI <<<<<<<<<<<<<<<<<<<<<<<<<".replace("&", "§"));
			getLogger().info(" ");
		}
	}

	public static SurvivalExpert getInstance() {
		return instance;
	}

	public String getPrefix() {
		return prefix;
	}

	public Storage getStorage() {
		return storage;
	}

	public BigDecimal getBattleBonusPercentage() {
		return battleBonusPercentage;
	}

	public BigDecimal getLifeBonusPercentage() {
		return lifeBonusPercentage;
	}

	public List<String> getOreBlocks() {
		return oreBlocks;
	}

	public List<String> getCropBlocks() {
		return cropBlocks;
	}

	public int getOrePointRange() {
		return orePointRange;
	}

	public int getCropPointRange() {
		return cropPointRange;
	}

	public Map<String, Integer> getAgeCheckBlock() {
		return ageCheckBlock;
	}

	public Map<String, String> getCovertItemBlock() {
		return covertItemBlock;
	}
}
