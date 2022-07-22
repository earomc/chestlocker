package net.earomc.chestlocker;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;

import javax.annotation.Nullable;

/**
 * @author earomc
 * Created on Juli 15, 2022 | 02:19:22
 * ʕっ•ᴥ•ʔっ
 */

public class Util {
    public static boolean isDoubleChest(Chest chest) {
        return chest.getInventory().getSize() == 54;
    }

    @Nullable
    public static DoubleChest getDoubleChestIfSo(Chest chest) {
        if (isDoubleChest(chest)) {
            return (DoubleChest) chest.getInventory().getHolder();
        } else return null;
    }

    public static void playLockedSound(Location location) {
        playSound(location, Sound.BLOCK_CHEST_LOCKED, 1F, 1F);
    }
    public static void playUnlockedSound(Location location) {
        playSound(location, Sound.BLOCK_BARREL_OPEN, 1F, 1F);
    }

    public static void playFailSound(Location location) {
        playSound(location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.8F, 0.5F);
    }

    private static void playSound(Location location, Sound sound, float volume, float pitch) {
        World world = location.getWorld();
        if (world != null) {
            world.playSound(location, sound, SoundCategory.BLOCKS, volume, pitch);
        }
    }
}
