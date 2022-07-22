package net.earomc.chestlocker.mode;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * @author earomc
 * Created on Juli 21, 2022 | 23:08:54
 * ʕっ•ᴥ•ʔっ
 */

public class ModeManager {

    private final HashMap<Player, Mode> modeMap;

    public ModeManager() {
        this.modeMap = new HashMap<>();
    }

    @Nullable
    public ModeType getModeType(Player player) {
        Mode mode = getMode(player);
        return mode != null ? mode.getType() : null;
    }

    @Nullable
    public Mode getMode(Player player) {
        return modeMap.get(player);
    }

    public boolean inMode(Player player) {
        return modeMap.containsKey(player);
    }

    public boolean inMode(Player player, ModeType modeType) {
        Mode mode = modeMap.get(player);
        if (mode != null) {
            return mode.getType() == modeType;
        }
        return false;
    }

    public void resetMode(Player player) {
        modeMap.remove(player);
    }

    public void setMode(Player player, Mode mode) {
        modeMap.put(player, mode);
        mode.onEnable();
    }

    public void setMode(Player player, String lock, ModeType modeType) {
        modeMap.put(player, modeType.getNew(player, lock));
    }
}
