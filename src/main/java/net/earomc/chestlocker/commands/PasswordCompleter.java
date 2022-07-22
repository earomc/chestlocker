package net.earomc.chestlocker.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author earomc
 * Created on Juli 22, 2022 | 01:39:30
 * ʕっ•ᴥ•ʔっ
 */

public class PasswordCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return List.of("password");
        }
        return null;
    }
}
