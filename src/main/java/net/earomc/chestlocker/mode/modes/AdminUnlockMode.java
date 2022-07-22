package net.earomc.chestlocker.mode.modes;

import net.earomc.chestlocker.UnlockResult;
import net.earomc.chestlocker.Util;
import net.earomc.chestlocker.lockables.LockableContainer;
import net.earomc.chestlocker.mode.Mode;
import net.earomc.chestlocker.mode.ModeType;
import org.bukkit.entity.Player;

/**
 * @author earomc
 * Created on Juli 21, 2022 | 23:18:33
 * ʕっ•ᴥ•ʔっ
 */

public class AdminUnlockMode extends Mode {

    public AdminUnlockMode(Player player) {
        super(player);
    }

    @Override
    public ModeType getType() {
        return ModeType.ADMIN_UNLOCK;
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
    public void handleAction(Player player, String ignored, LockableContainer<?> container) {
        UnlockResult result = container.tryUnlock(container.getLock());
        UnlockMode.handleUnlock(player, container, result);
    }


}
