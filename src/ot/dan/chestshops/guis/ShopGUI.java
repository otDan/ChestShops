package ot.dan.chestshops.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShopUser;

public class ShopGUI implements InventoryHolder {
    private final ChestShops plugin;
    private final Inventory inventory;
    private final ChestShopUser user;

    public ShopGUI(ChestShops plugin, ChestShopUser user) {
        this.plugin = plugin;
        this.user = user;
        String name = plugin.getColors().translate("Player Shop Directory");
        name = name.substring(0, Math.min(name.length(), 32));
        inventory = Bukkit.createInventory(this, 27, name);

        ItemStack item = new ItemStack(Material.BRICKS);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&6Building Blocks"));
        item.setItemMeta(itemMeta);
        inventory.setItem(10, item);

        item = new ItemStack(Material.DIAMOND);
        itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&bMinerals & Ores"));
        item.setItemMeta(itemMeta);
        inventory.setItem(11, item);

        item = new ItemStack(Material.WHEAT);
        itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&eFarming & Mob Drops"));
        item.setItemMeta(itemMeta);
        inventory.setItem(12, item);

        item = new ItemStack(Material.REDSTONE);
        itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&cRedstone Items"));
        item.setItemMeta(itemMeta);
        inventory.setItem(14, item);

        item = new ItemStack(Material.DRAGON_BREATH);
        itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&dPotions & Enchanting"));
        item.setItemMeta(itemMeta);
        inventory.setItem(15, item);

        item = new ItemStack(Material.ENDER_EYE);
        itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(plugin.getColors().translate("&aMiscellaneous"));
        item.setItemMeta(itemMeta);
        inventory.setItem(16, item);

        int loopSlot = 0;
        item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        for (ItemStack itemLoop:inventory) {
            if(itemLoop == null) {
                inventory.setItem(loopSlot, item);
            }
            loopSlot++;
        }
    }

    public ChestShopUser getUser() {
        return user;
    }

    //    @EventHandler
//    public void onClick(InventoryClickEvent event) {
//        if (!(event.getWhoClicked() instanceof Player)) return;
//        if (event.getInventory().getHolder() != this) return;
//        if (event.getClick() == ClickType.NUMBER_KEY) return;
//        final ItemStack clickedItem = event.getCurrentItem();
//        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
//        int slot = event.getSlot();
//        Player player = (Player) event.getWhoClicked();
//
//        switch (slot) {
//            case 10:
//                user.openCategory((Player) event.getWhoClicked(), "Building Blocks", 0);
//                break;
//            case 11:
//                user.openCategory((Player) event.getWhoClicked(), "Minerals & Ores", 0);
//                break;
//            case 12:
//                user.openCategory((Player) event.getWhoClicked(), "Farming & Mobs", 0);
//                break;
//            case 14:
//                user.openCategory((Player) event.getWhoClicked(), "Redstone Items", 0);
//                break;
//            case 15:
//                user.openCategory((Player) event.getWhoClicked(), "Potions & Enchanting", 0);
//                break;
//            case 16:
//                user.openCategory((Player) event.getWhoClicked(), "Miscellaneous", 0);
//                break;
//        }
//
//        event.setCancelled(true);
//        Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
//    }

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
