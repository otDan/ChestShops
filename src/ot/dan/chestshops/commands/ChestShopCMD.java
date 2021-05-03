package ot.dan.chestshops.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ot.dan.chestshops.ChestShops;
import ot.dan.chestshops.objects.ChestShopUser;

public class ChestShopCMD implements CommandExecutor {
    private ChestShops plugin;

    public ChestShopCMD(ChestShops plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            ChestShopUser chestShopUser = plugin.getChestShopManager().getChestShopUser(player.getUniqueId());
            chestShopUser.openShop(player);
        }
        return false;
    }
}
