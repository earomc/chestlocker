package net.earomc.chestlocker.lockables;

import org.bukkit.block.Chest;

public class LockableChest extends LockableContainer<Chest> {
    public LockableChest(Chest lockable) {
        super(lockable, "Chest");
    }
}
