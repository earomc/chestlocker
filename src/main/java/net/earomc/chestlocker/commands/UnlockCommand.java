package net.earomc.chestlocker.commands;

import net.earomc.chestlocker.mode.ModeManager;
import net.earomc.chestlocker.mode.modes.UnlockMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UnlockCommand implements CommandExecutor {

    private final ModeManager modeManager;

    public UnlockCommand(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        if (commandSender instanceof Player player) {
            if (args.length == 1) {
                String lock = args[0];
                modeManager.setMode(player, new UnlockMode(player, lock));
                return true;
            }
        }
        return false;
    }
}
