package net.earomc.chestlocker.mode;

import net.earomc.chestlocker.UnlockResult;
import net.earomc.chestlocker.Util;
import net.earomc.chestlocker.lockables.LockableContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author earomc
 * Created on Juli 21, 2022 | 23:09:10
 * ʕっ•ᴥ•ʔっ
 */

public abstract class Mode {

    public Mode(Player player) {
        this.player = player;
    }

    protected Player player;

    public abstract ModeType getType();

    public void onEnable() {

    }

    public void onAbort() {
    }

    public void handleAction(Player player, @Nullable String lock, LockableContainer<?> container) {
    }

}
