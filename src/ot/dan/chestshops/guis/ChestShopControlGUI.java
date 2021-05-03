package ot.dan.chestshops.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShop;

import java.util.*;

public class ChestShopControlGUI implements InventoryHolder {
    private final ChestShops plugin;
    private final Inventory inventory;
    private final ChestShop chestShop;
    private final ItemStack item;
    private final ItemStack transaction;
    private final ItemStack info;
    private final ItemStack advertize;

    public ChestShopControlGUI(ChestShops plugin, ChestShop chestShop) {
        this.plugin = plugin;
        this.chestShop = chestShop;
        String name = plugin.getColors().translate("Shop Management");
        name = name.substring(0, Math.min(name.length(), 32));
        inventory = Bukkit.createInventory(this, 27, name);

        if(chestShop.getItemType() != null) {
            item = new ItemStack(chestShop.getItemType());
        }
        else {
            item = new ItemStack(Material.BARRIER);
        }
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(plugin.getColors().translate("&e&lChest Shop Item"));
        List<String> lore = new ArrayList<>();
        lore.add(plugin.getColors().translate("&7This button represents the"));
        lore.add(plugin.getColors().translate("&7item you currently have up"));
        lore.add(plugin.getColors().translate("&7for offer in your chest shop"));
        lore.add(plugin.getColors().translate(""));
        lore.add(plugin.getColors().translate("&eTo update your chest item,"));
        lore.add(plugin.getColors().translate("&eclick on an item in your inventory"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(10, item);

        transaction = new ItemStack(Material.WRITABLE_BOOK);
        meta = transaction.getItemMeta();
        assert meta != null;
        meta.setDisplayName(plugin.getColors().translate("&e&lLatest Transactions"));
        if(chestShop.getTransactions().size() > 0) {
            meta.setLore(plugin.getColors().translate(chestShop.getTransactions()));
        }
        else {
            meta.setLore(Collections.singletonList(plugin.getColors().translate("&8> &cThere are no recent transactions.")));
        }
        transaction.setItemMeta(meta);
        inventory.setItem(12, transaction);

        info = new ItemStack(Material.GOLD_INGOT);
        meta = info.getItemMeta();
        assert meta != null;
        meta.setDisplayName(plugin.getColors().translate("&e&lPrice Info"));
        lore = new ArrayList<>();
        lore.add(plugin.getColors().translate("&7Buy Price: "));
        lore.add(plugin.getColors().translate("&c&l< &cLeft click to set price.."));
        lore.add(plugin.getColors().translate(""));
        lore.add(plugin.getColors().translate("&7Sell Price: "));
        lore.add(plugin.getColors().translate("&a&l> &aRight click to set price.."));
        if(chestShop.getBuyPrice() != -1) {
            lore.set(0, plugin.getColors().translate("&7Buy Price: &c$" + chestShop.getBuyPrice()));
        }
        else {
            lore.set(0, plugin.getColors().translate("&7Buy Price: &cDisabled"));
        }
        if(chestShop.getSellPrice() != -1) {
            lore.set(3, plugin.getColors().translate("&7Sell Price: &a$" + chestShop.getSellPrice()));
        }
        else {
            lore.set(3, plugin.getColors().translate("&7Sell Price: &aDisabled"));
        }
        meta.setLore(lore);
        info.setItemMeta(meta);
        inventory.setItem(14, info);

        advertize = new ItemStack(Material.SUNFLOWER);
        meta = advertize.getItemMeta();
        assert meta != null;
        meta.setDisplayName(plugin.getColors().translate("&e&lAdvertize Shop"));
        lore = new ArrayList<>();
        lore.add(plugin.getColors().translate("&7Advertize your chest shop for $100"));
        lore.add(plugin.getColors().translate("&7in the global directory for 24 hours!"));
        lore.add(plugin.getColors().translate("&8> &fCurrent Time: "));
        if(chestShop.isAdvertize()) {
            lore.set(2, plugin.getColors().translate("&8> &fCurrent Time: &e"));
        }
        else {
            lore.set(2, plugin.getColors().translate("&8> &fCurrent Time: &eNone"));
        }
        meta.setLore(lore);
        advertize.setItemMeta(meta);
        inventory.setItem(16, advertize);
        updateGui();
    }

    public void updateGui() {
        if(chestShop.getItemType() != null) {
            item.setType(chestShop.getItemType());
        }
        else {
            item.setType(Material.BARRIER);
        }

        ItemMeta meta = transaction.getItemMeta();
        assert meta != null;
        if(chestShop.getTransactions().size() > 0) {
            meta.setLore(chestShop.getTransactions());
        }
        transaction.setItemMeta(meta);

        meta = info.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        if(chestShop.getBuyPrice() != -1) {
            lore.set(0, plugin.getColors().translate("&7Buy Price: &c$" + chestShop.getBuyPrice()));
        }
        else {
            lore.set(0, plugin.getColors().translate("&7Buy Price: &cDisabled"));
        }
        if(chestShop.getSellPrice() != -1) {
            lore.set(3, plugin.getColors().translate("&7Sell Price: &a$" + chestShop.getSellPrice()));
        }
        else {
            lore.set(3, plugin.getColors().translate("&7Sell Price: &aDisabled"));
        }
        meta.setLore(lore);
        info.setItemMeta(meta);

        meta = advertize.getItemMeta();
        assert meta != null;
        lore = meta.getLore();
        assert lore != null;
        if(chestShop.isAdvertize()) {
            lore.set(2, plugin.getColors().translate("&8> &fCurrent Time: &e" + secToTime(chestShop.getAdvertizeTime())));
        }
        else {
            lore.set(2, plugin.getColors().translate("&8> &fCurrent Time: &eNone"));
        }
        meta.setLore(lore);
        advertize.setItemMeta(meta);

        inventory.setItem(10, item);
        inventory.setItem(12, transaction);
        inventory.setItem(14, info);
        inventory.setItem(16, advertize);

        int slot = 0;
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (ItemStack itemLoop:inventory) {
            if(itemLoop == null) {
                inventory.setItem(slot, item);
            }
            slot++;
        }
    }

    public ChestShop getChestShop() {
        return chestShop;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getTransaction() {
        return transaction;
    }

    public ItemStack getInfo() {
        return info;
    }

    public ItemStack getAdvertize() {
        return advertize;
    }

    private String secToTime(int sec) {
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if( hours >= 24) {
                int days = hours / 24;
                return String.format("%dd %02dh %02dm", days,hours%24, minutes);
            }
            return String.format("%02dh %02dm", hours, minutes);
        }
        return String.format("00h %02dm", minutes);
    }

    @Override
    public Inventory getInventory() {
        updateGui();
        return inventory;
    }

    public String printItems() {
        StringBuilder print = new StringBuilder();
        for (ItemStack item:inventory.getContents()) {
            if(item != null)
                print.append(item.getType()).append(" ");
        }
        return print.toString();
    }
}
