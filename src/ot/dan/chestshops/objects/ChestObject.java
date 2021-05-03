package ot.dan.chestshops.objects;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class ChestObject {
    private ChestType chestType;
    private Location location, location2;

    public ChestObject(ChestType chestType, Location location) {
        this.chestType = chestType;
        this.location = location;
    }

    public ChestObject(ChestType chestType, Location location, Location location2) {
        this.chestType = chestType;
        this.location = location;
        this.location2 = location2;
    }

    public ChestType getChestType() {
        return chestType;
    }

    public void setChestType(ChestType chestType) {
        this.chestType = chestType;
    }

    public Block getBlock() {
        return location.getBlock();
    }

    public Block getBlock2() {
        return location2.getBlock();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation2() {
        return location2;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }
}
