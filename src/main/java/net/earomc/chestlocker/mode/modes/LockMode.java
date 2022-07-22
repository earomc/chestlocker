package net.earomc.chestlocker.mode.modes;

import net.earomc.chestlocker.LockResult;
import net.earomc.chestlocker.LoggingUtil;
import net.earomc.chestlocker.Util;
import net.earomc.chestlocker.lockables.LockableContainer;
import net.earomc.chestlocker.mode.ModeType;
import net.earomc.chestlocker.mode.WordMode;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @author earomc
 * Created on Juli 21, 2022 | 23:22:57
 * ʕっ•ᴥ•ʔっ
 */

public class LockMode extends WordMode {

    public LockMode(Player player, String lock) {
        super(player, lock);
    }

    @Override
    public ModeType getType() {
        return ModeType.LOCK;
    }

    @Override
    public void onEnable() {
        player.sendMessage("§aLeft click on a container you want to lock with §7" + lock + "§a.");
        player.sendMessage("§aSneak to cancel the process.");
    }

    @Override
    public void onAbort() {
        player.sendMessage("§cExited locking mode.");
    }

    @Override
    public void handleAction(Player player, String lock, LockableContainer<?> container) {
        LockResult result = container.tryLock(lock);
        switch (result) {
            case SUCCESS -> {
                player.sendMessage("§a" + container.getName() + " locked with §9" + lock + "§a.");
                Util.playLockedSound(player.getLocation());
                LoggingUtil.logLock(player, container);
            }
            case LOCK_ALREADY_SET -> {
                player.sendMessage("§cCannot lock this locked " + container.getName().toLowerCase() + ". Lock has to be removed first.");
                Util.playFailSound(player.getLocation());
            }
        }
    }


}
