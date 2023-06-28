package net.earomc.chestlocker.mode.modes;

import net.earomc.chestlocker.ChestLockerLogger;
import net.earomc.chestlocker.UnlockResult;
import net.earomc.chestlocker.ChestLockerSounds;
import net.earomc.chestlocker.lockables.LockableContainer;
import net.earomc.chestlocker.mode.ModeType;
import net.earomc.chestlocker.mode.ModeWithLock;
import org.bukkit.entity.Player;

public class UnlockMode extends ModeWithLock {

    public UnlockMode(Player player, String lock) {
        super(player, lock);
    }

    @Override
    public ModeType getType() {
        return ModeType.UNLOCK;
    }

    @Override
    public void onEnable() {
        player.sendMessage("§aLeft click on a container you want to unlock.");
        player.sendMessage("§aSneak to cancel the process.");
    }

    @Override
    public void onAbort() {
        player.sendMessage("§cExited unlocking mode.");
    }

    @Override
    public void handleAction(Player player, String lock, LockableContainer<?> container) {
        UnlockResult result = container.tryUnlock(lock);
        handleUnlock(player, container, result);
    }

    static void handleUnlock(Player player, LockableContainer<?> container, UnlockResult result) {
        switch (result) {
            case SUCCESS -> {
                player.sendMessage("§a" + container.getName() + " unlocked!");
                ChestLockerSounds.playUnlockedSound(player.getLocation());
                ChestLockerLogger.logUnlock(player, container);
            }
            case INCORRECT_LOCK -> {
                player.sendMessage("§cCould not unlock " + container.getName().toLowerCase() + ". Lock is incorrect.");
                ChestLockerSounds.playFailSound(player.getLocation());
                ChestLockerLogger.logFailedUnlock(player, container);
            }
            case CONTAINER_NOT_LOCKED -> {
                player.sendMessage("§cThis " + container.getName().toLowerCase() + " is not locked.");
                ChestLockerSounds.playFailSound(player.getLocation());
            }
        }
    }
}
