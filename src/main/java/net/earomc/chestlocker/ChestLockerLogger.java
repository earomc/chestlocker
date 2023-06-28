package net.earomc.chestlocker;

import net.earomc.chestlocker.lockables.LockableContainer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.logging.Logger;


public class ChestLockerLogger {

    private static final Logger logger = Bukkit.getLogger();

    public static void logUnlock(Player player, LockableContainer<?> container) {
        Block block = container.getState().getBlock();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        String msg = String.format("Player %s (UUID: %s) unlocked a %s at ", player.getName(), player.getUniqueId(), container.getName()) +
                block.getLocation();
        logger.info(msg);
    }

    public static void logFailedUnlock(Player player, LockableContainer<?> container) {
        Block block = container.getState().getBlock();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        String msg = String.format("Player %s (UUID: %s) attempted to unlock a %s at ", player.getName(), player.getUniqueId(), container.getName()) +
                block.getLocation();
        logger.info(msg);
    }

    public static void logLock(Player player, LockableContainer<?> container) {
        Block block = container.getState().getBlock();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        String msg = String.format("Player %s (UUID: %s) locked a %s at ", player.getName(), player.getUniqueId(), container.getName()) +
                block.getLocation();
        logger.info(msg);
    }
}
