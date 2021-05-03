package ot.dan.chestshops.guis;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShopUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryGUI implements InventoryHolder {
    private final ChestShops plugin;
    private Inventory inventory;
    private ChestShopUser user;
    private int page;
    private String category;

    public CategoryGUI(ChestShops plugin, ChestShopUser user) {
        this.plugin = plugin;
        this.user = user;
        this.page = 0;
        this.category = "";
        String name = plugin.getColors().translate("Category");
        name = name.substring(0, Math.min(name.length(), 32));
        inventory = Bukkit.createInventory(this, 27, name);
    }

    public void updateGui(String category, int page) {
        this.page = page;
        this.category = category;
        String name = plugin.getColors().translate(category);
        name = name.substring(0, Math.min(name.length(), 32));
        inventory = Bukkit.createInventory(this, 54, name);

        int slot = 10;
        List<Material> materials = plugin.getCategories().getCategory(category);
        for (int i = 27*page; i < materials.size(); i++) {
            ItemStack item = new ItemStack(materials.get(i));
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            String type  = materials.get(i).name();
            type = type.replaceAll("_", " ");
            type = WordUtils.capitalizeFully(type);
            meta.setDisplayName(plugin.getColors().translate("&6" + type));
            List<String> lore = new ArrayList<>();
            lore.add(plugin.getColors().translate("&fPlayershops: &7" + plugin.getChestShopManager().getShopsWithItem(materials.get(i))));
            lore.add(plugin.getColors().translate(""));
            lore.add(plugin.getColors().translate(" &a> Click to view shops.."));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(slot, item);

            slot++;
            if(slot == 17) {
                slot = 19;
            }
            else if(slot == 26) {
                slot = 28;
            }
            else if(slot == 35) {
                slot = 37;
            }
            else if(slot == 44) {
                break;
            }
        }

        if(materials.size() > 27*(page+1)) {
            ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(plugin.getColors().translate("&a&lNext Page"));
            item.setItemMeta(meta);
            inventory.setItem(17, item);
            inventory.setItem(26, item);
            inventory.setItem(35, item);
            inventory.setItem(44, item);
        }
        else {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(plugin.getColors().translate("&c&lNo Next Page"));
            item.setItemMeta(meta);
            inventory.setItem(17, item);
            inventory.setItem(26, item);
            inventory.setItem(35, item);
            inventory.setItem(44, item);
        }


        if(page > 0) {
            ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(plugin.getColors().translate("&a&lPrevious Page"));
            item.setItemMeta(meta);
            inventory.setItem(9, item);
            inventory.setItem(18, item);
            inventory.setItem(27, item);
            inventory.setItem(36, item);
        }
        else {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(plugin.getColors().translate("&c&lNo Previous Page"));
            item.setItemMeta(meta);
            inventory.setItem(9, item);
            inventory.setItem(18, item);
            inventory.setItem(27, item);
            inventory.setItem(36, item);
        }

        int loopSlot = 0;
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (ItemStack itemLoop:inventory) {
            if(itemLoop == null) {
                inventory.setItem(loopSlot, item);
            }
            loopSlot++;
        }
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public ChestShopUser getUser() {
        return user;
    }

    public void setUser(ChestShopUser user) {
        this.user = user;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public String printItems() {
        StringBuilder print = new StringBuilder();
        for (ItemStack item : inventory.getContents()) {
            if (item != null)
                print.append(item.getType()).append(" ");
        }
        return print.toString();
    }
}
