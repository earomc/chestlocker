package net.earomc.chestlocker.commands;

import net.earomc.chestlocker.mode.ModeManager;
import net.earomc.chestlocker.mode.modes.AdminUnlockMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminUnlockCommand implements CommandExecutor {

    private final ModeManager modeManager;

    public AdminUnlockCommand(ModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        if (commandSender instanceof Player player) {
            if (player.hasPermission("chestlocker.adminunlock")) {
                if (args.length == 0) {
                    modeManager.setMode(player, new AdminUnlockMode(player));
                    return true;
                }
            }
        }
        return false;
    }
}
