package cn.daniellee.plugin.se;

import cn.daniellee.plugin.se.command.ExpertCommand;
import cn.daniellee.plugin.se.listener.MenuListener;
import cn.daniellee.plugin.se.listener.PlayerListener;
import cn.daniellee.plugin.se.model.GemInfo;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SurvivalExpert extends JavaPlugin {

	private static SurvivalExpert instance;

	private String prefix;

	private File playerFile = new File(getDataFolder(), "player.yml");

	private FileConfiguration playerData = new YamlConfiguration();

	private List<String> oreBlocks;

	private List<String> cropBlocks;

	private int orePointRange;

	private int cropPointRange;

	public static final String GEM_LORE = "§3SurvivalExpert:{type}:{level}";

	public static final String SLOT_LORE = "§3SurvivalExpert:Slot";

	public static final Pattern GEM_LORE_PATTERN = Pattern.compile("^§3SurvivalExpert:(Battle|Life):\\d$");

	// 需要检查Age属性的方块
	private Map<String, Integer> ageCheckBlock = new HashMap<>();

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

		ageCheckBlock.put("WHEAT", 7);
		ageCheckBlock.put("CARROTS", 7);
		ageCheckBlock.put("POTATOS", 7);
		ageCheckBlock.put("BEETROOTS", 3);
		ageCheckBlock.put("COCOA", 2);
		ageCheckBlock.put("NETHER_WARTS", 3);
		ageCheckBlock.put("CHORUS_FLOWER", 5);
	}

	public void loadConfig() {
		getLogger().info("[SurvivalExpert] Loading config...");
		if(!getDataFolder().exists()) getDataFolder().mkdirs();
		try {
			if (!playerFile.exists()) playerFile.createNewFile();
			playerData.load(playerFile);
		} catch (Exception e) {
			getLogger().info(" ");
			getLogger().info("&5[Qualifying]&dAn error occurred in player / reward file load.".replace("&", "§"));
			getLogger().info(" ");
			e.printStackTrace();
		}
		prefix = "&7[&b" + getConfig().get("prompt-prefix", "SurvivalExpert") + "&7] &3: &r";
		oreBlocks = Arrays.asList(getConfig().getString("setting.ore.blocks", "COAL_ORE,IRON_ORE,GOLD_ORE,DIAMOND_ORE,EMERALD_ORE,LAPIS_ORE,REDSTONE_ORE,NETHER_QUARTZ_ORE").split(","));
		cropBlocks = Arrays.asList(getConfig().getString("setting.crop.blocks", "WHEAT,CARROTS,POTATOS,BEETROOTS,COCOA,NETHER_WARTS,CHORUS_FLOWER,SUGAR_CANE,PUMPKIN,MELON,CACTUS").split(","));
		double orePointChange = getConfig().getDouble("setting.ore.chance", 1);
		if (orePointChange > 1 || orePointChange < 0) {
			getLogger().info("&5[Qualifying]&dThe chance can only be between 0 and 1, it is automatically set to 1.".replace("&", "§"));
			orePointChange = 1;
		}
		// 计算几率范围
		orePointRange = new BigDecimal(1).divide(new BigDecimal(orePointChange), 0, BigDecimal.ROUND_HALF_UP).intValue();
		double cropPointChange = getConfig().getDouble("setting.crop.chance", 1);
		if (cropPointChange > 1 || cropPointChange < 0) {
			getLogger().info("&5[Qualifying]&dThe chance can only be between 0 and 1, it is automatically set to 1.".replace("&", "§"));
			cropPointChange = 1;
		}
		cropPointRange = new BigDecimal(1).divide(new BigDecimal(cropPointChange), 0, BigDecimal.ROUND_HALF_UP).intValue();
		saveDefaultConfig();
	}

	@Override
	public void onDisable() {
		getLogger().info(" ");
		getLogger().info(">>>>>>>>>>>>>>>>>>>>>>>> SurvivalExpert Unloaded <<<<<<<<<<<<<<<<<<<<<<<<");
		getLogger().info(" ");
	}

	public static SurvivalExpert getInstance() {
		return instance;
	}

	public String getPrefix() {
		return prefix;
	}

	public FileConfiguration getPlayerData() {
		return playerData;
	}

	public void savePlayerData() {
		try {
			playerData.save(playerFile);
		} catch (IOException e) {
			getLogger().info(" ");
			getLogger().info("&5[Qualifying]&dAn error occurred in player file save.".replace("&", "§"));
			getLogger().info(" ");
			e.printStackTrace();
		}
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

	public boolean isSlot(ItemStack itemStack) {
		if (itemStack != null) {
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (itemMeta != null) {
				List<String> lore = itemMeta.getLore();
				if (lore != null && !lore.isEmpty()) {
					return SLOT_LORE.equals(lore.get(0));
				}
			}
		}
		return false;
	}

	public GemInfo getGemInfoByItemStack(ItemStack itemStack) {
		if (itemStack != null) {
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (itemMeta != null) {
				List<String> lore = itemMeta.getLore();
				if (lore != null && !lore.isEmpty()) {
					String firstLore = lore.get(0);
					if (GEM_LORE_PATTERN.matcher(firstLore).matches()) {
						GemInfo gemInfo = new GemInfo();
						gemInfo.setType(firstLore.substring(firstLore.indexOf(":") + 1, firstLore.lastIndexOf(":")));
						gemInfo.setLevel(Integer.valueOf(firstLore.substring(firstLore.lastIndexOf(":") + 1)));
						return gemInfo;
					}
				}
			}
		}
		return null;
	}
}
