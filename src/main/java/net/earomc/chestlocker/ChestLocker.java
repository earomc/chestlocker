package net.earomc.chestlocker;

import net.earomc.chestlocker.commands.AdminUnlockCommand;
import net.earomc.chestlocker.commands.LockCommand;
import net.earomc.chestlocker.commands.PasswordCompleter;
import net.earomc.chestlocker.commands.UnlockCommand;
import net.earomc.chestlocker.mode.ModeManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestLocker extends JavaPlugin {

    private ModeManager modeManager;
    private static ChestLocker INSTANCE;
    public static NamespacedKey LOCK;
    @Override
    public void onLoad() {
        INSTANCE = this;
        LOCK = NamespacedKey.fromString("locked", ChestLocker.instance());
    }

    public void onEnable() {
        this.modeManager = new ModeManager();
        Bukkit.getPluginManager().registerEvents(new LockListener(modeManager), this);
        registerCommands();
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        PluginCommand adminUnlockCommand = getCommand("adminunlock");
        adminUnlockCommand.setExecutor(new AdminUnlockCommand(modeManager));

        PasswordCompleter tabCompleter = new PasswordCompleter();

        PluginCommand lockCommand = getCommand("lock");
        lockCommand.setExecutor(new LockCommand(modeManager));
        lockCommand.setTabCompleter(tabCompleter);

        PluginCommand unlockCommand = getCommand("unlock");
        unlockCommand.setExecutor(new UnlockCommand(modeManager));
        unlockCommand.setTabCompleter(tabCompleter);
    }

    public static ChestLocker instance() {
        return INSTANCE;
    }
}
