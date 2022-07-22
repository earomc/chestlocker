package net.earomc.chestlocker;

import net.earomc.chestlocker.lockables.LockableChest;
import net.earomc.chestlocker.lockables.LockableContainer;
import net.earomc.chestlocker.lockables.LockableDoubleChest;
import org.bukkit.block.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author earomc
 * Created on Juli 22, 2022 | 01:12:21
 * ʕっ•ᴥ•ʔっ
 */

public class ContainerFactory {

    private final Map<Class<? extends BlockState>, Function<BlockState, LockableContainer<?>>> blockstateToContainerSupplierMap;

    public ContainerFactory() {
         /*
        This monstrosity of a map basically maps all the BlockState interfaces to a function that creates a new
        LockableContainer with the input of the BlockState that is actually clicked. See the method getContainerFromState.
         */
        this.blockstateToContainerSupplierMap = new HashMap<>();
        registerLockable(Barrel.class, s -> new LockableContainer<>((Barrel) s, "Barrel"));
        registerLockable(Dispenser.class, s -> new LockableContainer<>((Dispenser) s, "Dispenser"));
        registerLockable(Dropper.class, s -> new LockableContainer<>((Dropper) s, "Dropper"));
        registerLockable(Furnace.class, s -> new LockableContainer<>((Furnace) s, "Furnace"));
        registerLockable(ShulkerBox.class, s -> new LockableContainer<>((ShulkerBox) s, "Shulker Box"));
        registerLockable(BlastFurnace.class, s -> new LockableContainer<>((BlastFurnace) s, "Blast Furnace"));
        registerLockable(Smoker.class, s -> new LockableContainer<>((Smoker) s, "Smoker"));
        registerLockable(Hopper.class, s -> new LockableContainer<>((Hopper) s, "Hopper"));
        registerLockable(BrewingStand.class, s -> new LockableContainer<>((BrewingStand) s, "Brewing Stand"));
        registerLockable(Chest.class, s -> {
            Chest chest = (Chest) s;
            DoubleChest doubleChest = Util.getDoubleChestIfSo(chest);
            if (doubleChest != null) {
                return new LockableDoubleChest(new LockableChest((Chest) doubleChest.getLeftSide()),
                        new LockableChest((Chest) doubleChest.getRightSide()));
            } else {
                return new LockableChest(chest);
            }
        });
    }

    public void registerLockable(Class<? extends BlockState> blockStateClass, Function<BlockState, LockableContainer<?>> stateToContainerFunc) {
        blockstateToContainerSupplierMap.put(blockStateClass, stateToContainerFunc);
    }

    public LockableContainer<?> newContainerFromState(BlockState blockState) {
        for (Class<? extends BlockState> key : blockstateToContainerSupplierMap.keySet()) {
            if (key.isInstance(blockState)) {
                Function<BlockState, LockableContainer<?>> func = blockstateToContainerSupplierMap.get(key);
                if (func != null) {
                    return func.apply(blockState);
                }
            }
        }
        return null;
    }
}
