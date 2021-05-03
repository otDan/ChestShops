package ot.dan.chestshops.utils;

import org.bukkit.ChatColor;
import ot.dan.chestshops.ChestShops;

import java.util.ArrayList;
import java.util.List;

public class Colors {
    private ChestShops plugin;

    public Colors(ChestShops plugin) {
        this.plugin = plugin;
    }

    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public List<String> translate(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String string:list) {
            newList.add(ChatColor.translateAlternateColorCodes('&', string));
        }
        return newList;
    }
}
