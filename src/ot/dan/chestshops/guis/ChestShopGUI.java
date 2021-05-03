package ot.dan.chestshops.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShop;

import java.util.ArrayList;
import java.util.List;

public class ChestShopGUI implements InventoryHolder {
    private final ChestShops plugin;
    private final Inventory inventory;
    private final ChestShop chestShop;
    private ItemStack item;
    private ItemStack buy1, buy32, sell1, sell32;

    public ChestShopGUI(ChestShops plugin, ChestShop chestShop) {
        this.plugin = plugin;
        this.chestShop = chestShop;
        String name = plugin.getColors().translate(chestShop.getOwnerName() + "'s Shop");
        name = name.substring(0, Math.min(name.length(), 32));
        inventory = Bukkit.createInventory(this, 27, name);

        if (chestShop.getItemType() != null) {
            item = new ItemStack(chestShop.getItemType());
        } else {
            item = new ItemStack(Material.BARRIER);
        }
        inventory.setItem(13, item);
    }

    public void updateGui() {
        if (chestShop.getItemType() != null) {
            item = new ItemStack(chestShop.getItemType());
        } else {
            item = new ItemStack(Material.BARRIER);
        }

        buy1 = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMeta = buy1.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&cBuy x1"));
        List<String> lore = new ArrayList<>();
        if(chestShop.getBuyPrice() != -1) {
            lore.add(plugin.getColors().translate("&7Price: &f$" + chestShop.getBuyPrice()));
        }
        else {
            lore.add(plugin.getColors().translate("&7Price: &fDisabled"));
        }
        itemMeta.setLore(lore);
        buy1.setItemMeta(itemMeta);

        buy32 = new ItemStack(Material.RED_WOOL);
        buy32.setAmount(32);
        itemMeta = buy32.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&cBuy x32"));
        lore = new ArrayList<>();
        if(chestShop.getBuyPrice() != -1) {
            lore.add(plugin.getColors().translate("&7Price: &f$" + (chestShop.getBuyPrice() * 32)));
        }
        else {
            lore.add(plugin.getColors().translate("&7Price: &fDisabled"));
        }
        itemMeta.setLore(lore);
        buy32.setItemMeta(itemMeta);

        sell1 = new ItemStack(Material.LIME_WOOL);
        itemMeta = sell1.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&cSell x1"));
        lore = new ArrayList<>();
        if(chestShop.getSellPrice() != -1) {
            lore.add(plugin.getColors().translate("&7Price: &f$" + chestShop.getSellPrice()));
        }
        else {
            lore.add(plugin.getColors().translate("&7Price: &fDisabled"));
        }
        itemMeta.setLore(lore);
        sell1.setItemMeta(itemMeta);

        sell32 = new ItemStack(Material.LIME_WOOL);
        sell32.setAmount(32);
        itemMeta = sell32.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&cSell x32"));
        lore = new ArrayList<>();
        if(chestShop.getSellPrice() != -1) {
            lore.add(plugin.getColors().translate("&7Price: &f$" + (chestShop.getSellPrice() * 32)));
        }
        else {
            lore.add(plugin.getColors().translate("&7Price: &fDisabled"));
        }
        itemMeta.setLore(lore);
        sell32.setItemMeta(itemMeta);


        inventory.setItem(10, buy1);
        inventory.setItem(11, buy32);

        inventory.setItem(13, item);

        inventory.setItem(15, sell1);
        inventory.setItem(16, sell32);
        int loopSlot = 0;
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (ItemStack itemLoop:inventory) {
            if(itemLoop == null) {
                inventory.setItem(loopSlot, item);
            }
            loopSlot++;
        }
    }

    public ChestShop getChestShop() {
        return chestShop;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getBuy1() {
        return buy1;
    }

    public void setBuy1(ItemStack buy1) {
        this.buy1 = buy1;
    }

    public ItemStack getBuy32() {
        return buy32;
    }

    public void setBuy32(ItemStack buy32) {
        this.buy32 = buy32;
    }

    public ItemStack getSell1() {
        return sell1;
    }

    public void setSell1(ItemStack sell1) {
        this.sell1 = sell1;
    }

    public ItemStack getSell32() {
        return sell32;
    }

    public void setSell32(ItemStack sell32) {
        this.sell32 = sell32;
    }

    @Override
    public Inventory getInventory() {
        updateGui();
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
