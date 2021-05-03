package ot.dan.chestshops.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.guis.CategoryGUI;
import ot.dan.chestshops.guis.ItemGUI;
import ot.dan.chestshops.guis.ShopGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChestShopUser {
    private final UUID user;
    private final List<ChestShop> chestShops;

    private final ChestShops plugin;
    private final ShopGUI shopGUI;
    private final CategoryGUI categoryGUI;
    private final ItemGUI itemGUI;

    public ChestShopUser(ChestShops plugin, UUID user) {
        this.user = user;
        this.chestShops = new ArrayList<>();

        this.plugin = plugin;
        //GUI Instances
        this.shopGUI = new ShopGUI(plugin, this);
        this.categoryGUI = new CategoryGUI(plugin, this);
        this.itemGUI = new ItemGUI(plugin);
    }

    public void openShop(Player player) {
        player.closeInventory();
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(shopGUI.getInventory()), 1);
    }

    public void openCategory(Player player, String category, int page) {
        categoryGUI.updateGui(category, page);
        player.closeInventory();
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(categoryGUI.getInventory()), 1);
    }

    public void openItem(Player player, Material item, int page) {
        itemGUI.updateGui(item, page);
        player.closeInventory();
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(itemGUI.getInventory()), 1);
    }

    public UUID getUser() {
        return user;
    }

    public List<ChestShop> getChestShops() {
        return chestShops;
    }
}
