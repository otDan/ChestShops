package ot.dan.chestshops.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.guis.ChestShopControlGUI;
import ot.dan.chestshops.guis.ChestShopGUI;
import ot.dan.chestshops.guis.ChestShopPriceGUI;

import java.util.ArrayList;
import java.util.List;

public class ChestShop {
    private final ChestShops plugin;

    private final String ownerName;
    private ChestType chestType;
    private Location signLocation, chestRight, chestLeft;
    private Material itemType;
    private int buyPrice;
    private int sellPrice;
    private boolean advertize;
    private int advertizeTime;
    private final List<String> transactions;
    //Shop Hologram
    private final Hologram hologram;
    //Shop Gui Instances
    private final ChestShopControlGUI chestShopControlGUI;
    private final ChestShopPriceGUI chestShopPriceGUI;
    private final ChestShopGUI chestShopGUI;

    public ChestShop(String ownerName, ChestShops plugin, ChestType chestType, Location signLocation, Location chestRight) {
        this.plugin = plugin;

        this.ownerName = ownerName;
        this.chestType = chestType;
        this.signLocation = signLocation;
        this.chestRight = chestRight;
        this.itemType = null;
        this.buyPrice = -1;
        this.sellPrice = -1;
        this.advertize = false;
        this.advertizeTime = 0;
        this.transactions = new ArrayList<>();
        //Shop Gui Instances
        this.chestShopControlGUI = new ChestShopControlGUI(plugin, this);
        this.chestShopPriceGUI = new ChestShopPriceGUI(plugin, this);
        this.chestShopGUI = new ChestShopGUI(plugin, this);
        //Shop Hologram
        Location holoLocation = chestRight.clone();
        hologram = HologramsAPI.createHologram(plugin, holoLocation.add(0.5, 2, 0.5));
        hologram.appendItemLine(new ItemStack(Material.BARRIER));
        hologram.appendTextLine(plugin.getColors().translate("&c&l||||||||||"));
    }

    public ChestShop(String ownerName, ChestShops plugin, ChestType chestType, Location signLocation, Location chestRight, Location chestLeft) {
        this.plugin = plugin;

        this.ownerName = ownerName;
        this.chestType = chestType;
        this.signLocation = signLocation;
        this.chestRight = chestRight;
        this.chestLeft = chestLeft;
        this.itemType = null;
        this.buyPrice = -1;
        this.sellPrice = -1;
        this.advertize = false;
        this.advertizeTime = 0;
        this.transactions = new ArrayList<>();
        //Shop Gui Instances
        this.chestShopControlGUI = new ChestShopControlGUI(plugin, this);
        this.chestShopPriceGUI = new ChestShopPriceGUI(plugin, this);
        this.chestShopGUI = new ChestShopGUI(plugin, this);

        Location holoLocation = chestRight.clone().add(chestLeft.clone()).multiply(0.5);
        hologram = HologramsAPI.createHologram(plugin, holoLocation.add(0.5, 2, 0.5));
        hologram.appendItemLine(new ItemStack(Material.BARRIER));
        hologram.appendTextLine(plugin.getColors().translate("&c&l||||||||||"));
    }

    public ChestShop(String ownerName, ChestShops plugin, ChestType chestType, Location signLocation, Location chestRight, Location chestLeft, Material itemType, int buyPrice, int sellPrice, boolean advertize, int advertizeTime, List<String> transactions) {
        this.plugin = plugin;

        this.ownerName = ownerName;
        this.chestType = chestType;
        this.signLocation = signLocation;
        this.chestRight = chestRight;
        this.chestLeft = chestLeft;
        this.itemType = itemType;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.advertize = advertize;
        this.advertizeTime = advertizeTime;
        this.transactions = transactions;
        //Shop Gui Instances
        this.chestShopControlGUI = new ChestShopControlGUI(plugin, this);
        this.chestShopPriceGUI = new ChestShopPriceGUI(plugin, this);
        this.chestShopGUI = new ChestShopGUI(plugin, this);
        //Shop Hologram

        //TODO Check item amount in chest
        Location holoLocation;
        if(chestType.equals(ChestType.SINGLE_CHEST)) {
            holoLocation = chestRight.clone();
        }
        else {
            holoLocation = chestRight.clone().add(chestLeft.clone()).multiply(0.5);
        }
        hologram = HologramsAPI.createHologram(plugin, holoLocation.add(0.5, 2, 0.5));
        if(itemType != null) {
            hologram.insertItemLine(0, new ItemStack(itemType));
        }
        else {
            hologram.insertItemLine(0, new ItemStack(Material.BARRIER));
        }

        if(signLocation.getBlock() instanceof Sign) {
            Sign sign = (Sign) signLocation.getBlock().getState();
            sign.setLine(0, plugin.getColors().translate("&l" + ownerName));
            sign.setLine(1, plugin.getColors().translate("Buy 1: &a&lX"));
            sign.setLine(2, plugin.getColors().translate("Sell 1: &c&lX"));
            sign.setLine(3, plugin.getColors().translate("&7(Click to view)"));
            setBuyPrice(buyPrice);
            setSellPrice(sellPrice);

            updateChestShopBars();
        }
    }

    public void updateChestShopItem() {
        hologram.removeLine(0);
        hologram.insertItemLine(0, new ItemStack(itemType));
    }

    public void updateChestShopBars() {
        Chest chest = (Chest) chestRight.getBlock().getState();
        int items = 0;
        for (ItemStack item:chest.getInventory()) {
            if(item != null) {
                items += item.getAmount();
            }
        }
        float floatitems = items;
        float percentage;
        if(itemType == null) {
            percentage = floatitems / 64 / chest.getInventory().getSize();
        }
        else {
            percentage = floatitems / itemType.getMaxStackSize() / chest.getInventory().getSize();
        }
        percentage = percentage * 10;
        percentage = Math.round(percentage);
        int loop = (int) percentage;
        StringBuilder stringBuilder = new StringBuilder("&a&l");
        for (int i = 0; i < loop; i++) {
            stringBuilder.append("|");
        }
        stringBuilder.append("&c&l");
        int rest = 10 - loop;
        if(rest > 0) {
            for (int i = 0; i < rest; i++) {
                stringBuilder.append("|");
            }
        }
        if(hologram.size() > 1) {
            if (hologram.getLine(1) != null) {
                hologram.removeLine(1);
            }
        }
        hologram.insertTextLine(1, plugin.getColors().translate(stringBuilder.toString()));
    }

    public void openChestShopGUI(Player player) {
        player.closeInventory();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.openInventory(chestShopControlGUI.getInventory());
        }, 1);
    }

    public void openChestPriceGUI(Player player, boolean buy) {
        player.closeInventory();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            chestShopPriceGUI.updateGui(buy);
            player.openInventory(chestShopPriceGUI.getInventory());
        }, 1);
    }

    public void openShopGUI(Player player) {
        player.closeInventory();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.openInventory(chestShopGUI.getInventory());
        }, 1);
    }

    public void buy(Player player, int amount) {
        if(chestType.equals(ChestType.SINGLE_CHEST)) {
            Chest chest = (Chest) chestRight.getBlock().getState();
            if(chest.getInventory().containsAtLeast(new ItemStack(itemType), amount)) {
                int price = buyPrice*amount;
                if(plugin.getEconomy().getBalance(player) >= price) {
                    plugin.getEconomy().withdrawPlayer(player, price);
                    plugin.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(ownerName), price);
                    ItemStack item = new ItemStack(itemType, amount);
                    chest.getInventory().removeItem(item);
                    player.getInventory().addItem(item);
                    player.updateInventory();
                    addTransaction("&8> &7" + player.getName() + " &cBought &7x" + amount + " &c(-$" + price + ")");
                    player.sendMessage(plugin.getColors().translate("&8&l> &aYou successfully purchased items from this shop!"));
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ownerName);
                    if(offlinePlayer.isOnline()) {
                        String type  = itemType.name();
                        type = type.replaceAll("_", " ");
                        type = WordUtils.capitalizeFully(type);
                        offlinePlayer.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &a" + player.getName() + " bought &7" + amount + " " + type + " &afrom your shop! &2(+" + price + ")"));
                    }
                }
            }
        }
        else {
            DoubleChest chest = (DoubleChest) chestRight.getBlock().getState();
            if(chest.getInventory().containsAtLeast(new ItemStack(itemType), amount)) {
                int price = buyPrice*amount;
                if(plugin.getEconomy().getBalance(player) >= price) {
                    plugin.getEconomy().withdrawPlayer(player, price);
                    plugin.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(ownerName), price);
                    ItemStack item = new ItemStack(itemType, amount);
                    chest.getInventory().removeItem(item);
                    player.getInventory().addItem(item);
                    player.updateInventory();
                    addTransaction("&8> &7" + player.getName() + " &cBought &7x" + amount + " &c(-$" + price + ")");
                    player.sendMessage(plugin.getColors().translate("&8&l> &aYou successfully purchased items from this shop!"));
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ownerName);
                    if(offlinePlayer.isOnline()) {
                        String type  = itemType.name();
                        type = type.replaceAll("_", " ");
                        type = WordUtils.capitalizeFully(type);
                        offlinePlayer.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &a" + player.getName() + " bought &7" + amount + " " + type + " &afrom your shop! &2(+" + price + ")"));
                    }
                }
            }
        }
        updateChestShopBars();
    }

    public void sell(Player player, int amount) {
        if(chestType.equals(ChestType.SINGLE_CHEST)) {
            Chest chest = (Chest) chestRight.getBlock().getState();
            if(player.getInventory().containsAtLeast(new ItemStack(itemType), amount)) {
                int price = sellPrice*amount;
                if(plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(ownerName)) >= price) {
                    plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(ownerName), price);
                    plugin.getEconomy().depositPlayer(player, price);
                    ItemStack item = new ItemStack(itemType, amount);
                    player.getInventory().removeItem(item);
                    chest.getInventory().addItem(item);
                    player.updateInventory();
                    addTransaction("&8> &7" + player.getName() + " &aSold &7x" + amount + " &a(-$" + price + ")");
                    player.sendMessage(plugin.getColors().translate("&8&l> &aYou successfully sold items to this shop!"));
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ownerName);
                    if(offlinePlayer.isOnline()) {
                        String type  = itemType.name();
                        type = type.replaceAll("_", " ");
                        type = WordUtils.capitalizeFully(type);
                        offlinePlayer.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &a" + player.getName() + " sold &7" + amount + " " + type + " &ato your shop! &c(-" + price + ")"));
                    }
                }
            }
        }
        else {
            DoubleChest chest = (DoubleChest) chestRight.getBlock().getState();
            if(player.getInventory().containsAtLeast(new ItemStack(itemType), amount)) {
                int price = sellPrice*amount;
                if(plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(ownerName)) >= price) {
                    plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(ownerName), price);
                    plugin.getEconomy().depositPlayer(player, price);
                    ItemStack item = new ItemStack(itemType, amount);
                    player.getInventory().removeItem(item);
                    chest.getInventory().addItem(item);
                    player.updateInventory();
                    addTransaction("&8> &7" + player.getName() + " &aSold &7x" + amount + " &a(-$" + price + ")");
                    player.sendMessage(plugin.getColors().translate("&8&l> &aYou successfully sold items to this shop!"));
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ownerName);
                    if(offlinePlayer.isOnline()) {
                        String type  = itemType.name();
                        type = type.replaceAll("_", " ");
                        type = WordUtils.capitalizeFully(type);
                        offlinePlayer.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &a" + player.getName() + " sold &7" + amount + " " + type + " &ato your shop! &c(-" + price + ")"));
                    }
                }
            }
        }
        updateChestShopBars();
    }

    public void addTransaction(String transaction) {
        transactions.add(0, plugin.getColors().translate(transaction));
        if(transactions.size() > 10) {
            transactions.remove(10);
        }
    }

    public void startAdvertizing() {
        if (!advertize) {
            if (plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(ownerName)) >= 100) {
                plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(ownerName), 100);
                advertize = true;
                advertizeTime += 86400;
            }
        }
        else {
            if (plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(ownerName)) >= 100) {
                plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(ownerName), 100);
                if(advertizeTime+86400 <= 86400*7) {
                    advertizeTime += 86400;
                }
            }
        }
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void removeHologram() {
        hologram.delete();
    }

    public ChestType getChestType() {
        return chestType;
    }

    public void setChestType(ChestType chestType) {
        this.chestType = chestType;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public void setSignLocation(Location signLocation) {
        this.signLocation = signLocation;
    }

    public Location getChestRight() {
        return chestRight;
    }

    public void setChestRight(Location chestRight) {
        this.chestRight = chestRight;
    }

    public Location getChestLeft() {
        return chestLeft;
    }

    public void setChestLeft(Location chestLeft) {
        this.chestLeft = chestLeft;
    }

    public Material getItemType() {
        return itemType;
    }

    public void setItemType(Material itemType) {
        this.itemType = itemType;
        updateChestShopItem();
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;

        Sign sign = (Sign) signLocation.getBlock().getState();
        if(buyPrice == -1) {
            sign.setLine(1, plugin.getColors().translate("Buy 1: &a&lX"));
        }
        else {
            sign.setLine(1, plugin.getColors().translate("Buy 1: &a&l$" + buyPrice));
        }
        sign.update();
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;

        Sign sign = (Sign) signLocation.getBlock().getState();
        if(sellPrice == -1) {
            sign.setLine(2, plugin.getColors().translate("Sell 1: &c&lX"));
        }
        else {
            sign.setLine(2, plugin.getColors().translate("Sell 1: &c&l$" + sellPrice));
        }
        sign.update();
    }

    public boolean isAdvertize() {
        return advertize;
    }

    public void setAdvertize(boolean advertize) {
        this.advertize = advertize;
    }

    public int getAdvertizeTime() {
        return advertizeTime;
    }

    public void setAdvertizeTime(int advertizeTime) {
        this.advertizeTime = advertizeTime;
    }

    public List<String> getTransactions() {
        return transactions;
    }
}
