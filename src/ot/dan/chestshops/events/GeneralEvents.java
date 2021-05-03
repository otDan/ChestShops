package ot.dan.chestshops.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShop;
import ot.dan.chestshops.objects.ChestShopUser;

import java.util.List;

public class GeneralEvents implements Listener {
    private final ChestShops plugin;

    public GeneralEvents(ChestShops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ChestShopUser chestShopUser = plugin.getChestShopManager().getChestShopUser(event.getPlayer().getUniqueId());
        if(chestShopUser == null) {
            ChestShopUser newChestShopUser = new ChestShopUser(plugin, event.getPlayer().getUniqueId());
            List<ChestShop> toAssignShops = plugin.getChestShopManager().getChestShopsFromName(event.getPlayer().getName());
            newChestShopUser.getChestShops().addAll(toAssignShops);
            plugin.getChestShopManager().removeToAssign(toAssignShops);
            plugin.getChestShopManager().getChestShopUsers().add(newChestShopUser);
        }
    }
}
