package ot.dan.chestshops.events;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.guis.*;
import ot.dan.chestshops.objects.ChestShop;
import ot.dan.chestshops.objects.ChestShopUser;

import java.util.Objects;

public class ChestShopGuiEvents implements Listener {
    private final ChestShops plugin;

    public ChestShopGuiEvents(ChestShops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof CategoryGUI) {
            if (event.getClick() == ClickType.NUMBER_KEY) return;
            final ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();

            CategoryGUI categoryGUI = (CategoryGUI) inventory.getHolder();

            if (slot == 9 || slot == 18 || slot == 27 || slot == 36) {
                if (ChatColor.stripColor(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName()).equals("Previous Page")) {
                    int open = categoryGUI.getPage() - 1;
                    categoryGUI.getUser().openCategory(player, categoryGUI.getCategory(), open);
                }
            } else if (slot == 17 || slot == 26 || slot == 35 || slot == 44) {
                if (ChatColor.stripColor(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName()).equals("Next Page")) {
                    int open = categoryGUI.getPage() + 1;
                    categoryGUI.getUser().openCategory(player, categoryGUI.getCategory(), open);
                }
            } else if (slot > 9 && slot < 44) {
                categoryGUI.getUser().openItem(player, clickedItem.getType(), 0);
            }

            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
        }

        else if(inventory.getHolder() instanceof ChestShopControlGUI) {
            if (event.getClick() == ClickType.NUMBER_KEY) return;
            final ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            Player player = (Player) event.getWhoClicked();

            ChestShopControlGUI chestShopControlGUI = (ChestShopControlGUI) inventory.getHolder();
            ChestShop chestShop = chestShopControlGUI.getChestShop();

            if(clickedItem.equals(chestShopControlGUI.getInfo())) {
                if(chestShop.getItemType() != null) {
                    if (event.getAction().equals(InventoryAction.PICKUP_ALL)) {
                        chestShop.openChestPriceGUI(player, true);
                    } else if (event.getAction().equals(InventoryAction.PICKUP_HALF)) {
                        chestShop.openChestPriceGUI(player, false);
                    }
                }
                else {
                    player.sendMessage(plugin.getColors().translate("&8&l> &cYou must set a shop item within the GUI first!"));
                }
            }
            else if(clickedItem.equals(chestShopControlGUI.getAdvertize())) {
                if(chestShop.getItemType() != null) {
                    if (chestShop.getSellPrice() != -1 || chestShop.getBuyPrice() != -1) {
                        chestShop.startAdvertizing();
                        chestShopControlGUI.updateGui();
                    } else {
                        player.sendMessage(plugin.getColors().translate("&8&l> &cYou must set a shop buy or sell price with the GUI first!"));
                    }
                }
                else {
                    player.sendMessage(plugin.getColors().translate("&8&l> &cYou must set a shop item within the GUI first!"));
                }
            }
            else if (event.getClickedInventory() == event.getView().getBottomInventory()) {
                chestShop.setItemType(clickedItem.getType());
                chestShopControlGUI.updateGui();
                player.sendMessage(plugin.getColors().translate("&8&l> &aYou successfully updated your shop item!"));
            }

            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
        }
        else if(inventory.getHolder() instanceof ChestShopGUI) {
            if (event.getClick() == ClickType.NUMBER_KEY) return;
            final ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            Player player = (Player) event.getWhoClicked();

            ChestShopGUI chestShopGUI = (ChestShopGUI) inventory.getHolder();
            ChestShop chestShop = chestShopGUI.getChestShop();

            if(clickedItem.equals(chestShopGUI.getBuy1())) {
                if(!chestShopGUI.getBuy1().getItemMeta().getLore().get(0).contains("Disabled")) {
                    chestShop.buy(player, 1);
                }
            }

            if(clickedItem.equals(chestShopGUI.getBuy32())) {
                if(!chestShopGUI.getBuy32().getItemMeta().getLore().get(0).contains("Disabled")) {
                    chestShop.buy(player, 32);
                }
            }

            if(clickedItem.equals(chestShopGUI.getSell1())) {
                if(!chestShopGUI.getSell1().getItemMeta().getLore().get(0).contains("Disabled")) {
                    chestShop.sell(player, 1);
                }
            }

            if(clickedItem.equals(chestShopGUI.getSell32())) {
                if(!chestShopGUI.getSell32().getItemMeta().getLore().get(0).contains("Disabled")) {
                    chestShop.sell(player, 32);
                }
            }

            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
        }
        else if(inventory.getHolder() instanceof ChestShopPriceGUI) {
            if (event.getClick() == ClickType.NUMBER_KEY) return;
            final ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            Player player = (Player) event.getWhoClicked();

            ChestShopPriceGUI chestShopPriceGUI = (ChestShopPriceGUI) inventory.getHolder();

            if(clickedItem.equals(chestShopPriceGUI.getDisable())) {
                chestShopPriceGUI.setPriceChange(-1);
                chestShopPriceGUI.updatePrice();
            }

            if(clickedItem.getType().equals(Material.LIME_WOOL)) {
                int amount = Integer.parseInt(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().split("\\$")[1]);
                if(chestShopPriceGUI.getPriceChange() == -1) {
                    chestShopPriceGUI.setPriceChange(chestShopPriceGUI.getPriceChange() + amount + 1);
                }
                else {
                    chestShopPriceGUI.setPriceChange(chestShopPriceGUI.getPriceChange() + amount);
                }
                chestShopPriceGUI.updatePrice();
            }
            else if(clickedItem.getType().equals(Material.RED_WOOL)) {
                int amount = Integer.parseInt(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().split("\\$")[1]);
                chestShopPriceGUI.setPriceChange(Math.max(chestShopPriceGUI.getPriceChange() - amount, 0));
                chestShopPriceGUI.updatePrice();
            }

            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
        }
        else if(inventory.getHolder() instanceof ItemGUI) {
            if (event.getClick() == ClickType.NUMBER_KEY) return;
            final ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            Player player = (Player) event.getWhoClicked();

            NamespacedKey key = new NamespacedKey(plugin, "location");
            NamespacedKey key2 = new NamespacedKey(plugin, "locationchest");
            ItemMeta itemMeta = clickedItem.getItemMeta();
            assert itemMeta != null;
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            if(container.has(key , PersistentDataType.STRING)) {
                if(container.has(key2, PersistentDataType.STRING)) {
                    String foundValue = container.get(key, PersistentDataType.STRING);
                    String foundValueChest = container.get(key2, PersistentDataType.STRING);
                    Location signLocation = getLiteLocationFromString(foundValue);
                    Location chestLocation = getLiteLocationFromString(foundValueChest);
                    assert signLocation != null;
                    signLocation.add(0.5, 0, 0.5);
                    assert chestLocation != null;
                    chestLocation.add(0.5, -1, 0.5);
                    player.teleport(lookAt(signLocation, chestLocation));
                }
            }

            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
        }
        else if(inventory.getHolder() instanceof ShopGUI) {
            if (event.getClick() == ClickType.NUMBER_KEY) return;
            final ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            int slot = event.getSlot();
            Player player = (Player) event.getWhoClicked();

            ShopGUI shopGUI = (ShopGUI) inventory.getHolder();
            ChestShopUser user = shopGUI.getUser();

            switch (slot) {
                case 10:
                    user.openCategory((Player) event.getWhoClicked(), "Building Blocks", 0);
                    break;
                case 11:
                    user.openCategory((Player) event.getWhoClicked(), "Minerals & Ores", 0);
                    break;
                case 12:
                    user.openCategory((Player) event.getWhoClicked(), "Farming & Mobs", 0);
                    break;
                case 14:
                    user.openCategory((Player) event.getWhoClicked(), "Redstone Items", 0);
                    break;
                case 15:
                    user.openCategory((Player) event.getWhoClicked(), "Potions & Enchanting", 0);
                    break;
                case 16:
                    user.openCategory((Player) event.getWhoClicked(), "Miscellaneous", 0);
                    break;
            }

            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
        }
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

    private Location lookAt(Location loc, Location lookat) {
        //Clone the loc to prevent applied changes to the input loc
        loc = loc.clone();

        // Values of change in distance (make it relative)
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc.setPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }
}
