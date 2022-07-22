package net.earomc.chestlocker.mode;

import net.earomc.chestlocker.mode.modes.AdminUnlockMode;
import net.earomc.chestlocker.mode.modes.LockMode;
import net.earomc.chestlocker.mode.modes.UnlockMode;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

/**
 * @author earomc
 * Created on Juli 21, 2022 | 23:17:11
 * ʕっ•ᴥ•ʔっ
 */

public enum ModeType {
    LOCK(LockMode::new),
    UNLOCK(UnlockMode::new),
    ADMIN_UNLOCK((player, s) -> new AdminUnlockMode(player));

    private final BiFunction<Player, String, Mode> modeFunction;

    ModeType(BiFunction<Player, String, Mode> modeFunction) {
        this.modeFunction = modeFunction;
    }

    public Mode getNew(Player player, String lock) {
        return modeFunction.apply(player, lock);
    }
}
