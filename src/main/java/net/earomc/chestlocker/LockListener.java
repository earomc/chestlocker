package net.earomc.chestlocker;


import net.earomc.chestlocker.lockables.LockableChest;
import net.earomc.chestlocker.lockables.LockableContainer;
import net.earomc.chestlocker.lockables.LockableDoubleChest;
import net.earomc.chestlocker.mode.Mode;
import net.earomc.chestlocker.mode.ModeManager;
import net.earomc.chestlocker.mode.ModeWithLock;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
    public void onPlayerRightClickContainer(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (player.isSneaking() && event.isBlockInHand()) return;
        LockableContainer<?> container = containerFactory.newContainerFromBlockState(clickedBlock.getState());
        if (container == null) return;

        Mode mode = modeManager.getMode(player);
        if (mode != null) {
            event.setCancelled(true);
            String lock = null;
            if (mode instanceof ModeWithLock modeWithLock) {
                lock = modeWithLock.getLock();
            }
            mode.handleAction(player, lock, container);
        } else {

            if (container.isLocked() && !holdsCorrectItem(player, container)) {
                event.setCancelled(true);
                ChestLockerSounds.playLockedSound(player.getLocation());
            }
        }
    }

    private boolean holdsCorrectItem(Player player, LockableContainer<?> container) {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInMainHand.getItemMeta();
        if (itemMeta == null) return false;
        String itemName = PlainTextComponentSerializer.plainText().serialize(itemInMainHand.effectiveName());
        return itemName.equals(container.getLock());
    }

    //Safety handling
    @EventHandler
    public void onChestConnect(BlockPlaceEvent event) {
        Block block = event.getBlock();
        BlockState state = block.getState();
        if (state instanceof Chest chestState) {
            Bukkit.getScheduler().runTaskLater(ChestLocker.instance(), () -> {
                if (event.isCancelled()) return;
                if (LockableDoubleChest.isDoubleChest(chestState)) {
                    LockableDoubleChest lockableDoubleChest = (LockableDoubleChest) containerFactory.newContainerFromBlockState(chestState);

                    // Not null because of isDoubleChest()
                    //noinspection DataFlowIssue
                    LockableChest otherChest = lockableDoubleChest.getOtherChest(chestState);
                    //noinspection ConstantConditions
                    if (otherChest.isLocked()) {
                        String lock = otherChest.getLock();
                        lockableDoubleChest.lock(lock);
                    }
                }
            }, 1);
        }
    }

    @EventHandler
    public void onMoveShulkerBoxWithPiston(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            LockableContainer<?> container = containerFactory.newContainerFromBlockState(block.getState());
            if (container == null || !container.isLocked()) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakLockedContainers(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (modeManager.inMode(player)) {
            event.setCancelled(true);
            return;
        }
        LockableContainer<?> lockableContainer = containerFactory.newContainerFromBlockState(event.getBlock().getState());
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
            LockableContainer<?> container = containerFactory.newContainerFromBlockState(block.getState());
            if (container == null || !container.isLocked()) continue;
            lockedBlocks.add(block);
        }
        event.blockList().removeAll(lockedBlocks);
    }

    @EventHandler
    public void onHopperRetrieveItems(InventoryMoveItemEvent event) {
        InventoryHolder holder = event.getSource().getHolder();
        if (holder instanceof BlockState blockState) {
            LockableContainer<?> container = containerFactory.newContainerFromBlockState(blockState);
            if (container != null && container.isLocked()) {
                event.setCancelled(true);
            }
        } else if (holder instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest) event.getSource().getHolder();
            BlockState chestLeftState = (BlockState) chest.getLeftSide();
            BlockState chestRightState = (BlockState) chest.getRightSide();

            LockableContainer<?> chestLeft = containerFactory.newContainerFromBlockState(chestLeftState);
            LockableContainer<?> chestRight = containerFactory.newContainerFromBlockState(chestRightState);

            //noinspection ConstantConditions
            if (chestLeft.isLocked() || chestRight.isLocked()) {
                event.setCancelled(true);
            }
        }
    }
}
