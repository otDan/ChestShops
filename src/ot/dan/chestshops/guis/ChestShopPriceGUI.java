package ot.dan.chestshops.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShop;

public class ChestShopPriceGUI implements InventoryHolder {
    private final ChestShops plugin;
    private Inventory inventory;
    private final ChestShop chestShop;
    private ItemStack item;
    private final ItemStack disable;
    private final ItemStack add1;
    private final ItemStack add10;
    private final ItemStack add100;
    private final ItemStack add1000;
    private final ItemStack remove1;
    private final ItemStack remove10;
    private final ItemStack remove100;
    private final ItemStack remove1000;
    private boolean buy;
    private int priceChange;

    public ChestShopPriceGUI(ChestShops plugin, ChestShop chestShop) {
        this.plugin = plugin;
        this.chestShop = chestShop;
        String name = plugin.getColors().translate("Price Change");
        this.inventory = Bukkit.createInventory(this, 36, name);

        add1 = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = add1.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&a&l+$1"));
        add1.setItemMeta(itemMeta);

        add10 = new ItemStack(Material.LIME_WOOL);
        itemMeta = add10.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&a&l+$10"));
        add10.setItemMeta(itemMeta);

        add100 = new ItemStack(Material.LIME_WOOL);
        itemMeta = add100.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&a&l+$100"));
        add100.setItemMeta(itemMeta);

        add1000 = new ItemStack(Material.LIME_WOOL);
        itemMeta = add1000.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&a&l+$1000"));
        add1000.setItemMeta(itemMeta);

        remove1 = new ItemStack(Material.RED_WOOL);
        itemMeta = remove1.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&c&l-$1"));
        remove1.setItemMeta(itemMeta);

        remove10 = new ItemStack(Material.RED_WOOL);
        itemMeta = remove10.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&c&l-$10"));
        remove10.setItemMeta(itemMeta);

        remove100 = new ItemStack(Material.RED_WOOL);
        itemMeta = remove100.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&c&l-$100"));
        remove100.setItemMeta(itemMeta);

        remove1000 = new ItemStack(Material.RED_WOOL);
        itemMeta = remove1000.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&c&l-$1000"));
        remove1000.setItemMeta(itemMeta);

        disable = new ItemStack(Material.BARRIER);
        itemMeta = disable.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&e&lDisable"));
        disable.setItemMeta(itemMeta);

        inventory.setItem(2, add1);
        inventory.setItem(3, add10);
        inventory.setItem(4, disable);
        inventory.setItem(5, add100);
        inventory.setItem(6, add1000);

        inventory.setItem(24, remove1);
        inventory.setItem(23, remove10);
        inventory.setItem(22, disable);
        inventory.setItem(21, remove100);
        inventory.setItem(20, remove1000);
    }

    public void updateGui(boolean buy) {
        this.buy = buy;
        String name;
        if(buy) {
            name = plugin.getColors().translate("Buy Price Change");
        }
        else {
            name = plugin.getColors().translate("Sell Price Change");
        }
        name = name.substring(0, Math.min(name.length(), 32));
        inventory = Bukkit.createInventory(this, 27, name);

        if(chestShop.getItemType() != null) {
            item = new ItemStack(chestShop.getItemType());
        }
        else {
            item = new ItemStack(Material.BARRIER);
        }
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&7Current Price: &a$" + priceChange));
        item.setItemMeta(itemMeta);

        inventory.setItem(2, add1);
        inventory.setItem(3, add10);
        inventory.setItem(4, disable);
        inventory.setItem(5, add100);
        inventory.setItem(6, add1000);

        inventory.setItem(13, item);

        inventory.setItem(24, remove1);
        inventory.setItem(23, remove10);
        inventory.setItem(22, disable);
        inventory.setItem(21, remove100);
        inventory.setItem(20, remove1000);

        int slot = 0;
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (ItemStack itemLoop:inventory) {
            if(itemLoop == null) {
                inventory.setItem(slot, item);
            }
            slot++;
        }
    }

    public void updatePrice() {
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        if(priceChange == -1) {
            itemMeta.setDisplayName(plugin.getColors().translate("&7Current Price: &aNone"));
        }
        else {
            itemMeta.setDisplayName(plugin.getColors().translate("&7Current Price: &a$" + priceChange));
        }
        item.setItemMeta(itemMeta);

        inventory.setItem(13, item);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != this) return;
        if(buy) {
            chestShop.setBuyPrice(priceChange);
            event.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &aYou successfully set your shops buy price!"));
        }
        else {
            chestShop.setSellPrice(priceChange);
            event.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &aYou successfully set your shops sell price!"));
        }


        Bukkit.getScheduler().runTaskLater(plugin, () -> chestShop.openChestShopGUI((Player) event.getPlayer()), 1);
    }

    public ItemStack getDisable() {
        return disable;
    }
    public int getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(int priceChange) {
        this.priceChange = priceChange;
    }

    @Override
    public Inventory getInventory() {
        if(buy) {
            priceChange = chestShop.getBuyPrice();
        }
        else {
            priceChange = chestShop.getSellPrice();
        }
        updatePrice();
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
