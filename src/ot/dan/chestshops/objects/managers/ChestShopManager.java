package ot.dan.chestshops.objects.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShop;
import ot.dan.chestshops.objects.ChestShopUser;
import ot.dan.chestshops.objects.ChestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChestShopManager {
    private final ChestShops plugin;
    private final List<ChestShopUser> chestShopUsers;
    private final List<ChestShop> toAssign;

    public ChestShopManager(ChestShops plugin) {
        this.plugin = plugin;
        chestShopUsers = new ArrayList<>();
        toAssign = new ArrayList<>();
        loadChestShops();
        advertizingRunnable();
    }
//TODO finish config loading

    public void saveChestShops() {
        for (ChestShopUser chestShopUser:chestShopUsers) {
            ConfigurationSection userSection = plugin.getDataConfig().getConfigurationSection(chestShopUser.getUser().toString());
            if(userSection != null) {
                chestLoopSave(chestShopUser, userSection);
            }
            else {
                userSection = plugin.getDataConfig().createSection(chestShopUser.getUser().toString());
                chestLoopSave(chestShopUser, userSection);
            }
        }
        plugin.saveDataConfig();
    }

    private void chestLoopSave(ChestShopUser chestShopUser, ConfigurationSection userSection) {
        int chest = 0;
        for (ChestShop chestShop:chestShopUser.getChestShops()) {
            if(userSection.getConfigurationSection(String.valueOf(chest)) != null) {
                ConfigurationSection chestSection = userSection.getConfigurationSection("shop" + chest);
                assert chestSection != null;
                chestLoopSaveEnd(chestShop, chestSection);
            }
            else {
                ConfigurationSection chestSection = userSection.createSection("shop" + chest);
                chestLoopSaveEnd(chestShop, chestSection);
            }
            chest++;
        }
    }

    private void chestLoopSaveEnd(ChestShop chestShop, ConfigurationSection chestSection) {
        chestSection.set("owner", chestShop.getOwnerName());
        chestSection.set("chest", chestShop.getChestType().name());
        chestSection.set("signLoc", getLiteStringFromLocation(chestShop.getSignLocation()));
        chestSection.set("chestRight", getLiteStringFromLocation(chestShop.getChestRight()));
        if (chestShop.getChestLeft() != null) {
            chestSection.set("chestLeft", getLiteStringFromLocation(chestShop.getChestLeft()));
        }
        if(chestShop.getItemType() != null) {
            chestSection.set("item", chestShop.getItemType().name());
        }
        else {
            chestSection.set("item", Material.BARRIER.name());
        }
        chestSection.set("buy", chestShop.getBuyPrice());
        chestSection.set("sell", chestShop.getSellPrice());
        chestSection.set("advertize", chestShop.isAdvertize());
        chestSection.set("advertizeTime", chestShop.getAdvertizeTime());
        chestSection.set("transactions", chestShop.getTransactions());
    }

    public void loadChestShops() {
        for (String user:plugin.getDataConfig().getKeys(false)) {
            if(plugin.getDataConfig().isConfigurationSection(user)) {
                ConfigurationSection userSection = plugin.getDataConfig().getConfigurationSection(user);
                assert userSection != null;
                for (String chest:userSection.getKeys(false)) {
                    if(userSection.isConfigurationSection(chest)) {
                        ConfigurationSection chestSection = userSection.getConfigurationSection(chest);
                        assert chestSection != null;
                        String owner = chestSection.getString("owner");
                        ChestType chestType = ChestType.valueOf(chestSection.getString("chest"));
                        Location signLoc = getLiteLocationFromString(chestSection.getString("signLoc"));
                        Location chestRight = getLiteLocationFromString(chestSection.getString("chestRight"));
                        Location chestLeft = null;
                        if(chestType.equals(ChestType.DOUBLE_CHEST)) {
                            chestLeft = getLiteLocationFromString(chestSection.getString("chestLeft"));
                        }
                        Material item = Material.valueOf(chestSection.getString("item"));
                        if(item.equals(Material.BARRIER)) {
                            item = null;
                        }
                        int buy = chestSection.getInt("buy");
                        int sell = chestSection.getInt("sell");
                        boolean advertize = chestSection.getBoolean("advertize");
                        int advertizeTime = chestSection.getInt("advertizeTime");
                        List<String> transactions = chestSection.getStringList("transactions");

                        ChestShop newChestShop = new ChestShop(owner, plugin, chestType, signLoc, chestRight, chestLeft, item, buy, sell, advertize, advertizeTime, transactions);
                        toAssign.add(newChestShop);
                        System.out.println("Loaded chest shop for owner: " + owner);
                    }
                }
            }
        }
    }

    public void advertizingRunnable() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (ChestShopUser chestShopUser : chestShopUsers) {
                for (ChestShop chestShop : chestShopUser.getChestShops()) {
                    if(chestShop.isAdvertize()) {
                        if(chestShop.getAdvertizeTime()-1 > 0) {
                            chestShop.setAdvertizeTime(chestShop.getAdvertizeTime()-1);
                        }
                        else {
                            chestShop.setAdvertize(false);
                        }
                    }
                }
            }
        }, 0, 20);
    }

    public List<ChestShop> getChestShopsFromName(String name) {
        List<ChestShop> out = new ArrayList<>();
        for (ChestShop chestShop:toAssign) {
            if(chestShop.getOwnerName().equals(name)) {
                out.add(chestShop);
            }
        }
        return out;
    }

    public void removeToAssign(List<ChestShop> userShops) {
        for (ChestShop chestShop:userShops) {
            toAssign.remove(chestShop);
        }
    }

    public List<ChestShop> getToAssign() {
        return toAssign;
    }

    public List<ChestShopUser> getChestShopUsers() {
        return chestShopUsers;
    }

    public List<ChestShop> getChestShopAdvertizedWithItem(Material material) {
        List<ChestShop> chestShops = new ArrayList<>();
        for (ChestShopUser chestShopUser : chestShopUsers) {
            for (ChestShop chestShop:chestShopUser.getChestShops()) {
                if(chestShop.isAdvertize()) {
                    if (chestShop.getItemType() == material) {
                        chestShops.add(chestShop);
                    }
                }
            }
        }
        return chestShops;
    }

    public int getShopsWithItem(Material material) {
        int out = 0;
        for (ChestShopUser chestShopUser : chestShopUsers) {
            for (ChestShop chestShop:chestShopUser.getChestShops()) {
                if(chestShop.isAdvertize()) {
                    if (chestShop.getItemType() == material) {
                        out++;
                    }
                }
            }
        }
        return out;
    }

    public boolean isChestShopFromUser(ChestShop chestShopCheck, ChestShopUser chestShopUser) {
        for (ChestShop chestShop:chestShopUser.getChestShops()) {
            if(chestShopCheck == chestShop) {
                return true;
            }
        }
        return false;
    }

    public void removeChestShopFromUser(ChestShop chestShopCheck) {
        for (ChestShopUser chestShopUser : chestShopUsers) {
            for (ChestShop chestShop:chestShopUser.getChestShops()) {
                if(chestShopCheck.equals(chestShop)) {
                    chestShop.removeHologram();
                    chestShopUser.getChestShops().remove(chestShop);
                    return;
                }
            }
        }
    }

    public ChestShop getChestShopFromLocation(Location location) {
        for (ChestShopUser chestShopUser : chestShopUsers) {
            for (ChestShop chestShop:chestShopUser.getChestShops()) {
                if(chestShop.getChestType().equals(ChestType.SINGLE_CHEST)) {
                    if (chestShop.getChestRight().equals(location)) {
                        return chestShop;
                    }
                }
                else {
                    if (chestShop.getChestRight().equals(location)) {
                        return chestShop;
                    }
                    if (chestShop.getChestLeft().equals(location)) {
                        return chestShop;
                    }
                }
            }
        }
        for (ChestShop chestShop:toAssign) {
            if(chestShop.getChestType().equals(ChestType.SINGLE_CHEST)) {
                if (chestShop.getChestRight().equals(location)) {
                    return chestShop;
                }
            }
            else {
                if (chestShop.getChestRight().equals(location)) {
                    return chestShop;
                }
                if (chestShop.getChestLeft().equals(location)) {
                    return chestShop;
                }
            }
        }
        return null;
    }

    public ChestShopUser getChestShopUser(UUID user) {
        for (ChestShopUser chestShopUser : chestShopUsers) {
            if (chestShopUser.getUser().equals(user)) {
                return chestShopUser;
            }
        }
        return null;
    }

    public static String getLiteStringFromLocation(Location loc) {
        if (loc == null) {
            return "";
        }
        return Objects.requireNonNull(loc.getWorld()).getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() ;
    }

    public static Location getLiteLocationFromString(String s) {
        if (s == null || s.trim().equals("")) {
            return null;
        }
        final String[] parts = s.split(":");
        if (parts.length == 4) {
            World w = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }
}
