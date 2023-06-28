package net.earomc.chestlocker;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;

public class ChestLockerSounds {

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
