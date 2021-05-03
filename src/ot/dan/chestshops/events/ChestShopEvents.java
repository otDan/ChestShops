package ot.dan.chestshops.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestObject;
import ot.dan.chestshops.objects.ChestShop;
import ot.dan.chestshops.objects.ChestShopUser;
import ot.dan.chestshops.objects.ChestType;

import java.util.Objects;

public class ChestShopEvents implements Listener {
    private final ChestShops plugin;

    public ChestShopEvents(ChestShops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopClick(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(event.getClickedBlock() != null) {
                if(event.getClickedBlock().getType().name().contains("SIGN")) {
                    if(event.getClickedBlock().getBlockData() instanceof WallSign) {
                        ChestObject chest = chestBlock(event.getClickedBlock(), (WallSign) event.getClickedBlock().getBlockData());
                        if (chest != null) {
                            ChestShop chestShop = getChestShop(chest.getLocation());
                            ChestShopUser chestShopUser = plugin.getChestShopManager().getChestShopUser(event.getPlayer().getUniqueId());
                            if (chestShop != null) {
                                if (chestShopUser != null) {
                                    if (plugin.getChestShopManager().isChestShopFromUser(chestShop, chestShopUser)) {
                                        chestShop.openChestShopGUI(event.getPlayer());
                                    }
                                    else {
                                        chestShop.openShopGUI(event.getPlayer());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        if(event.getInventory().getLocation() != null) {
            ChestShop chestShop = getChestShop(event.getInventory().getLocation());
            if (chestShop != null) {
                chestShop.updateChestShopBars();
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getType().equals(Material.CHEST)) {
            if(getChestShop(event.getBlock().getLocation()) != null) {
                //TODO add message
                event.setCancelled(true);
            }
        }
        else if(event.getBlock().getType().name().contains("SIGN")) {
            if(event.getBlock().getBlockData() instanceof WallSign) {
                ChestObject chest = chestBlock(event.getBlock(), (WallSign) event.getBlock().getBlockData());
                if (chest != null) {
                    ChestShop chestShop = getChestShop(chest.getLocation());
                    ChestShopUser chestShopUser = plugin.getChestShopManager().getChestShopUser(event.getPlayer().getUniqueId());
                    if (chestShop != null) {
                        if (chestShopUser != null) {
                            if (plugin.getChestShopManager().isChestShopFromUser(chestShop, chestShopUser)) {
                                plugin.getChestShopManager().removeChestShopFromUser(chestShop);
                                event.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &aYou successfully removed this chest shop!"));
                            }
                            else {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlockPlaced().getType().equals(Material.CHEST)) {
            if(checkPresentShop(event.getBlockPlaced().getLocation())) {
                //TODO add message
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSignEdit(SignChangeEvent event) {
        if(event.getBlock().getBlockData() instanceof WallSign) {
            ChestObject chest = chestBlock(event.getBlock(), (WallSign) event.getBlock().getBlockData());
            if (chest != null) {
                if(!checkPresentShop(chest.getLocation())) {
                    if (event.getLines().length > 0) {
                        if (Objects.equals(Objects.requireNonNull(event.getLine(0)).toLowerCase(), "[shop]")) {
                            ChestShopUser shopUser = plugin.getChestShopManager().getChestShopUser(event.getPlayer().getUniqueId());
                            if(shopUser != null) {
                                int limit = getLimit(event.getPlayer());
                                if (limit == -1) {
                                    placeShop(event, chest, shopUser, event.getBlock().getLocation());
                                    event.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &aYou successfully created a chest shop!"));
                                } else if (limit > 0) {
                                    if (limit > shopUser.getChestShops().size()) {
                                        placeShop(event, chest, shopUser, event.getBlock().getLocation());
                                        event.getPlayer().sendMessage(plugin.getColors().translate("&8&l> &aYou successfully created a chest shop!"));
                                    }
                                    else {
                                        //TODO Add too many shops message
                                    }
                                }
                                else {
                                    //TODO Add no shop message
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void placeShop(SignChangeEvent event, ChestObject chest, ChestShopUser shopUser, Location signLocation) {
        event.setLine(0, plugin.getColors().translate("&l" + event.getPlayer().getName()));
        event.setLine(1, plugin.getColors().translate("Buy 1: &a&lX"));
        event.setLine(2, plugin.getColors().translate("Sell 1: &c&lX"));
        event.setLine(3, plugin.getColors().translate("&7(Click to view)"));
        if (chest.getChestType().equals(ChestType.SINGLE_CHEST)) {
            shopUser.getChestShops().add(new ChestShop(event.getPlayer().getName(), plugin, ChestType.SINGLE_CHEST, signLocation, chest.getLocation()));
        } else {
            shopUser.getChestShops().add(new ChestShop(event.getPlayer().getName(), plugin, ChestType.DOUBLE_CHEST, signLocation, chest.getLocation(), chest.getLocation2()));
        }

    }

    private boolean checkPresentShop(Location location) {
        Location checkLocation = location.add(1, 0, 0);
        if(plugin.getChestShopManager().getChestShopFromLocation(checkLocation) != null) {
            return true;
        }
        checkLocation = location.add(-1, 0, 0);
        if(plugin.getChestShopManager().getChestShopFromLocation(checkLocation) != null) {
            return true;
        }
        checkLocation = location.add(0, 0, 1);
        if(plugin.getChestShopManager().getChestShopFromLocation(checkLocation) != null) {
            return true;
        }
        checkLocation = location.add(0, 0, -1);
        return plugin.getChestShopManager().getChestShopFromLocation(checkLocation) != null;
    }

    private ChestShop getChestShop(Location location) {
        if(plugin.getChestShopManager().getChestShopFromLocation(location) != null) {
            return plugin.getChestShopManager().getChestShopFromLocation(location);
        }
        return null;
    }

    private ChestObject chestBlock(Block block, WallSign sign) {
        BlockFace attached = sign.getFacing().getOppositeFace();
        Block blockAttached = block.getRelative(attached);
        if (blockAttached.getType().equals(Material.CHEST)) {
            BlockState state = blockAttached.getState();
            if (state instanceof Chest) {
                Chest chest = (Chest) state;
                Inventory inventory = chest.getInventory();
                if (inventory instanceof DoubleChestInventory) {
                    DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
                    assert doubleChest != null;
                    return new ChestObject(ChestType.DOUBLE_CHEST, ((Chest) Objects.requireNonNull(doubleChest.getLeftSide())).getLocation(), ((Chest) Objects.requireNonNull(doubleChest.getRightSide())).getLocation());
                } else {
                    return new ChestObject(ChestType.SINGLE_CHEST, blockAttached.getLocation());
                }
            }
        }
        return null;
    }

    public int getLimit(Player player) {
        if(player.hasPermission("chestshop.amount.*")) {
            return -1;
        }
        for(int i = 1; i <= 300; i++) {
            if(player.hasPermission("chestshop.amount." + i)) {
                return i;
            }
        }
        return -330;
    }
}
