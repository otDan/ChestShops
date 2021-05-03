package ot.dan.chestshops.guis;

import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemGUI implements InventoryHolder {
    private final ChestShops plugin;
    private Inventory inventory;
    private Material material;
    private int page;

    public ItemGUI(ChestShops plugin) {
        this.plugin = plugin;
        String name = plugin.getColors().translate("Category");
        name = name.substring(0, Math.min(name.length(), 32));
        inventory = Bukkit.createInventory(this, 27, name);
    }

    public void updateGui(Material material, int page) {
        this.page = page;
        this.material = material;
        String type  = material.name();
        type = type.replaceAll("_", " ");
        type = WordUtils.capitalizeFully(type);
        type = type.substring(0, Math.min(type.length(), 32));
        inventory = Bukkit.createInventory(this, 54, type);

        int slot = 10;
        List<ChestShop> chestShops = plugin.getChestShopManager().getChestShopAdvertizedWithItem(material);
        for (int i = 27*page; i < chestShops.size(); i++) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            assert meta != null;
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(chestShops.get(i).getOwnerName()));
            meta.setDisplayName(plugin.getColors().translate("&6" + chestShops.get(i).getOwnerName() + "'s Shop"));
            List<String> lore = new ArrayList<>();
            if(chestShops.get(i).getBuyPrice() != -1) {
                lore.add(plugin.getColors().translate("&f(1x) &7Buy Price: &c$" + chestShops.get(i).getBuyPrice()));
            }
            else {
                lore.add(plugin.getColors().translate("&f(1x) &7Sell Price: &cNone"));
            }
            if(chestShops.get(i).getSellPrice() != -1) {
                lore.add(plugin.getColors().translate("&f(1x) &7Sell Price: &a$" + chestShops.get(i).getSellPrice()));
            }
            else {
                lore.add(plugin.getColors().translate("&f(1x) &7Sell Price: &aNone"));
            }
            lore.add(plugin.getColors().translate(""));
            lore.add(plugin.getColors().translate(" &a> Click to visit shop.."));
            meta.setLore(lore);
            NamespacedKey key = new NamespacedKey(plugin, "location");
            NamespacedKey key2 = new NamespacedKey(plugin, "locationchest");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, getLiteStringFromLocation(chestShops.get(i).getSignLocation()));
            meta.getPersistentDataContainer().set(key2, PersistentDataType.STRING, getLiteStringFromLocation(chestShops.get(i).getChestRight()));
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

        if(chestShops.size() > 27*(page+1)) {
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

//    @EventHandler
//    public void onClick(InventoryClickEvent event) {
//        if (!(event.getWhoClicked() instanceof Player)) return;
//        if (event.getInventory().getHolder() != this) return;
//        if (event.getClick() == ClickType.NUMBER_KEY) return;
//        final ItemStack clickedItem = event.getCurrentItem();
//        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
//        Player player = (Player) event.getWhoClicked();
//
//        NamespacedKey key = new NamespacedKey(plugin, "location");
//        NamespacedKey key2 = new NamespacedKey(plugin, "locationchest");
//        ItemMeta itemMeta = clickedItem.getItemMeta();
//        assert itemMeta != null;
//        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
//
//        if(container.has(key , PersistentDataType.STRING)) {
//            if(container.has(key2, PersistentDataType.STRING)) {
//                String foundValue = container.get(key, PersistentDataType.STRING);
//                String foundValueChest = container.get(key2, PersistentDataType.STRING);
//                Location signLocation = getLiteLocationFromString(foundValue);
//                Location chestLocation = getLiteLocationFromString(foundValueChest);
//                assert signLocation != null;
//                signLocation.add(0.5, 0, 0.5);
//                assert chestLocation != null;
//                chestLocation.add(0.5, -1, 0.5);
//                player.teleport(lookAt(signLocation, chestLocation));
//            }
//        }
//
//        event.setCancelled(true);
//        Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
//    }

    public static String getLiteStringFromLocation(Location loc) {
        if (loc == null) {
            return "";
        }
        return Objects.requireNonNull(loc.getWorld()).getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() ;
    }

//    public static Location getLiteLocationFromString(String s) {
//        if (s == null || s.trim().equals("")) {
//            return null;
//        }
//        final String[] parts = s.split(":");
//        if (parts.length == 4) {
//            World w = Bukkit.getServer().getWorld(parts[0]);
//            double x = Double.parseDouble(parts[1]);
//            double y = Double.parseDouble(parts[2]);
//            double z = Double.parseDouble(parts[3]);
//            return new Location(w, x, y, z);
//        }
//        return null;
//    }

//    private Location lookAt(Location loc, Location lookat) {
//        //Clone the loc to prevent applied changes to the input loc
//        loc = loc.clone();
//
//        // Values of change in distance (make it relative)
//        double dx = lookat.getX() - loc.getX();
//        double dy = lookat.getY() - loc.getY();
//        double dz = lookat.getZ() - loc.getZ();
//
//        // Set yaw
//        if (dx != 0) {
//            // Set yaw start value based on dx
//            if (dx < 0) {
//                loc.setYaw((float) (1.5 * Math.PI));
//            } else {
//                loc.setYaw((float) (0.5 * Math.PI));
//            }
//            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
//        } else if (dz < 0) {
//            loc.setYaw((float) Math.PI);
//        }
//
//        // Get the distance from dx/dz
//        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
//
//        // Set pitch
//        loc.setPitch((float) -Math.atan(dy / dxz));
//
//        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
//        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
//        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);
//
//        return loc;
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