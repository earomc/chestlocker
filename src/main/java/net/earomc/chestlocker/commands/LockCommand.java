package net.earomc.chestlocker.commands;

import net.earomc.chestlocker.mode.ModeManager;
import net.earomc.chestlocker.mode.modes.LockMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LockCommand implements CommandExecutor {

    private final ModeManager modeManager;

    public LockCommand(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 1) {
                String lock = args[0];
                modeManager.setMode(player, new LockMode(player, lock));
                return true;
            }
        }
        return false;
    }

}
