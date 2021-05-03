package ot.dan.chestshops;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ot.dan.chestshops.commands.ChestShopCMD;
import ot.dan.chestshops.events.ChestShopEvents;
import ot.dan.chestshops.events.ChestShopGuiEvents;
import ot.dan.chestshops.events.GeneralEvents;
import ot.dan.chestshops.objects.managers.ChestShopManager;
import ot.dan.chestshops.objects.Categories;
import ot.dan.chestshops.utils.Colors;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ChestShops extends JavaPlugin {
    //Config Related
    private File configFile, dataFile, categoryFile;
    private FileConfiguration config, data, category;

    //Utils
    private Colors colors;
    private static Economy econ = null;

    private Categories categories;
    private ChestShopManager chestShopManager;

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadConfig();
        registerCommands();
        registerEvents();

        colors = new Colors(this);

        categories = new Categories(this);
        chestShopManager = new ChestShopManager(this);
    }

    @Override
    public void onDisable() {
        chestShopManager.saveChestShops();
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("shop")).setExecutor(new ChestShopCMD(this));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new GeneralEvents(this), this);
        getServer().getPluginManager().registerEvents(new ChestShopEvents(this), this);
        getServer().getPluginManager().registerEvents(new ChestShopGuiEvents(this), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void loadConfig() {
        createConfig();
    }

    private void createConfig() {
        configFile = new File(getDataFolder(), "config.yml");
        dataFile = new File(getDataFolder(), "data.yml");
        categoryFile = new File(getDataFolder(), "categories.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        if (!categoryFile.exists()) {
            categoryFile.getParentFile().mkdirs();
            saveResource("categories.yml", false);
        }

        config = new YamlConfiguration();
        data = new YamlConfiguration();
        category = new YamlConfiguration();
        try {
            config.load(configFile);
            data.load(dataFile);
            category.load(categoryFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveActualConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getActualConfig() {
        return this.config;
    }

    public void saveDataConfig() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCategoryConfig() {
        try {
            category.save(categoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getDataConfig() {
        return this.data;
    }

    public FileConfiguration getCategoryConfig() {
        return this.category;
    }

    public Colors getColors() {
        return colors;
    }

    public Economy getEconomy() {
        return econ;
    }

    public Categories getCategories() {
        return categories;
    }

    public ChestShopManager getChestShopManager() {
        return chestShopManager;
    }
}
