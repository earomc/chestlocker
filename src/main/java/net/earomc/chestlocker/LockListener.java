package net.earomc.chestlocker;


import net.earomc.chestlocker.lockables.LockableChest;
import net.earomc.chestlocker.lockables.LockableContainer;
import net.earomc.chestlocker.lockables.LockableDoubleChest;
import net.earomc.chestlocker.mode.Mode;
import net.earomc.chestlocker.mode.ModeManager;
import net.earomc.chestlocker.mode.ModeWithLock;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;

public class LockListener implements Listener {

    private final ModeManager modeManager;
    private final ContainerFactory containerFactory;

    public LockListener(ModeManager modeManager) {
        this.modeManager = modeManager;
        this.containerFactory = new ContainerFactory();
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Mode mode = modeManager.getMode(player);
        if (mode != null) {
            mode.onAbort();
            modeManager.resetMode(player);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        modeManager.resetMode(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteractWithContainer(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Mode mode = modeManager.getMode(player);
        if (mode == null) return;
        LockableContainer<?> container = containerFactory.newContainerFromState(clickedBlock.getState());
        if (container != null) {
            String lock = null;
            if (mode instanceof ModeWithLock modeWithLock) {
                lock = modeWithLock.getLock();
            }
            mode.handleAction(player, lock, container);
        }
    }

    //Safety handling
    @EventHandler
    public void onChestConnect(BlockPlaceEvent event) {
        Block block = event.getBlock();
        BlockState state = block.getState();
        if (state instanceof Chest chestState) {
            if (LockableDoubleChest.isDoubleChest(chestState)) {
                LockableDoubleChest lockableDoubleChest = (LockableDoubleChest) containerFactory.newContainerFromState(state);

                LockableChest otherChest = lockableDoubleChest.getOtherChest(chestState);

                //no nullpointer because of "isDoubleChest()"

                //noinspection ConstantConditions
                if (otherChest.isLocked()) {
                    String lock = otherChest.getLock();
                    lockableDoubleChest.lock(lock);
                }
            }
        }
    }

    @EventHandler
    public void onMoveShulkerBoxWithPiston(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (block.getState() instanceof Lockable lockable) {
                if (lockable.isLocked()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBreakLockedContainers(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (modeManager.inMode(player)) {
            event.setCancelled(true);
            return;
        }
        LockableContainer<?> lockableContainer = containerFactory.newContainerFromState(event.getBlock().getState());
        if (lockableContainer != null) {
            if (lockableContainer.isLocked()) {
                event.setCancelled(true);
                player.sendMessage("§cThis " + lockableContainer.getName().toLowerCase() + " is locked and cannot be broken.");
            }
        }
    }

    @EventHandler
    public void onBlowUpLockedContainers(EntityExplodeEvent event) {
        ArrayList<Block> lockedBlocks = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (block.getState() instanceof Lockable lockable) {
                if (lockable.isLocked()) {
                    lockedBlocks.add(block);
                }
            }
        }
        event.blockList().removeAll(lockedBlocks);
    }

    @EventHandler
    public void onHopperRetrieveItems(InventoryMoveItemEvent event) {
        InventoryHolder holder = event.getSource().getHolder();
        if (holder instanceof Lockable) {
            Lockable chest = (Lockable) event.getSource().getHolder();
            if (chest != null) {
                if (chest.isLocked()) {
                    event.setCancelled(true);
                }
            }
        } else if (holder instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest) event.getSource().getHolder();
            Chest chestLeft = (Chest) chest.getLeftSide();
            Chest chestRight = (Chest) chest.getRightSide();
            //noinspection ConstantConditions
            if (chestLeft.isLocked() || chestRight.isLocked()) {
                event.setCancelled(true);
            }
        }
    }
}
