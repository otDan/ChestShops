package ot.dan.chestshops.objects;

import org.bukkit.Material;
import ot.dan.chestshops.ChestShops;

import java.util.ArrayList;
import java.util.List;

public class Categories {
    private final ChestShops plugin;
    private final List<Material> building;
    private final List<Material> minerals;
    private final List<Material> farming;
    private final List<Material> redstone;
    private final List<Material> potions;
    private final List<Material> miscellaneous;

    public Categories(ChestShops plugin) {
        this.plugin = plugin;
        this.building = new ArrayList<>();
        this.minerals = new ArrayList<>();
        this.farming = new ArrayList<>();
        this.redstone = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.miscellaneous = new ArrayList<>();
        loadCategory();
    }

    private void loadCategory() {
        List<String> buildingList = plugin.getCategoryConfig().getStringList("building");
        List<String> mineralsList = plugin.getCategoryConfig().getStringList("minerals");
        List<String> farmingList = plugin.getCategoryConfig().getStringList("farming");
        List<String> redstoneList = plugin.getCategoryConfig().getStringList("redstone");
        List<String> potionsList = plugin.getCategoryConfig().getStringList("potions");
        List<String> miscellaneousList = plugin.getCategoryConfig().getStringList("miscellaneous");
        for (String string:buildingList) {
            this.building.add(Material.valueOf(string.toUpperCase()));
        }
        for (String string:mineralsList) {
            this.minerals.add(Material.valueOf(string.toUpperCase()));
        }
        for (String string:farmingList) {
            this.farming.add(Material.valueOf(string.toUpperCase()));
        }
        for (String string:redstoneList) {
            this.redstone.add(Material.valueOf(string.toUpperCase()));
        }
        for (String string:potionsList) {
            this.potions.add(Material.valueOf(string.toUpperCase()));
        }
        for (String string:miscellaneousList) {
            this.miscellaneous.add(Material.valueOf(string.toUpperCase()));
        }
    }

    public List<Material> getCategory(String category) {
        if(category.equals("Building Blocks")) {
            return building;
        }
        if(category.equals("Minerals & Ores")) {
            return minerals;
        }
        if(category.equals("Farming & Mobs")) {
            return farming;
        }
        if(category.equals("Redstone Items")) {
            return redstone;
        }
        if(category.equals("Potions & Enchanting")) {
            return potions;
        }
        if(category.equals("Miscellaneous")) {
            return miscellaneous;
        }
        return null;
    }

    public List<Material> getBuilding() {
        return building;
    }

    public List<Material> getMinerals() {
        return minerals;
    }

    public List<Material> getFarming() {
        return farming;
    }

    public List<Material> getRedstone() {
        return redstone;
    }

    public List<Material> getPotions() {
        return potions;
    }

    public List<Material> getMiscellaneous() {
        return miscellaneous;
    }
}
