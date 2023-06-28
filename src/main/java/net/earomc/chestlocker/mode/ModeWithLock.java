package net.earomc.chestlocker.mode;

import org.bukkit.entity.Player;

/**
 * @author earomc
 * Created on Juli 21, 2022 | 23:15:14
 * ʕっ•ᴥ•ʔっ
 * Represents a mode that carries a lock word with it.
 */

public abstract class ModeWithLock extends Mode {

    protected final String lock;

    public ModeWithLock(Player player, String lock) {
        super(player);
        this.lock = lock;
    }

    public final String getLock() {
        return lock;
    }

}
