package net.earomc.chestlocker;

import net.earomc.chestlocker.lockables.LockableChest;
import net.earomc.chestlocker.lockables.LockableContainer;
import net.earomc.chestlocker.lockables.LockableDoubleChest;
import org.bukkit.block.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ContainerFactory {

    private final Map<Class<? extends BlockState>, Function<BlockState, LockableContainer<?>>> blockstateToContainerSupplierMap;

    public ContainerFactory() {
         /*
        This monstrosity of a map basically maps all the BlockState interfaces of the Minecraft container blocks that can be locked to a function that creates a new
        LockableContainer with the input of the BlockState that is actually clicked. See the method newContainerFromState.
         */
        this.blockstateToContainerSupplierMap = new HashMap<>();
        registerContainer(Beacon.class, s -> new LockableContainer<>((Beacon) s, "Beacon"));
        registerContainer(Barrel.class, s -> new LockableContainer<>((Barrel) s, "Barrel"));
        registerContainer(Dispenser.class, s -> new LockableContainer<>((Dispenser) s, "Dispenser"));
        registerContainer(Dropper.class, s -> new LockableContainer<>((Dropper) s, "Dropper"));
        registerContainer(Furnace.class, s -> new LockableContainer<>((Furnace) s, "Furnace"));
        registerContainer(ShulkerBox.class, s -> new LockableContainer<>((ShulkerBox) s, "Shulker Box"));
        registerContainer(BlastFurnace.class, s -> new LockableContainer<>((BlastFurnace) s, "Blast Furnace"));
        registerContainer(Smoker.class, s -> new LockableContainer<>((Smoker) s, "Smoker"));
        registerContainer(Hopper.class, s -> new LockableContainer<>((Hopper) s, "Hopper"));
        registerContainer(BrewingStand.class, s -> new LockableContainer<>((BrewingStand) s, "Brewing Stand"));
        registerContainer(Chest.class, s -> {
            Chest chest = (Chest) s;
            DoubleChest doubleChest = LockableDoubleChest.getDoubleChestIfSo(chest);
            if (doubleChest != null) {
                return new LockableDoubleChest(new LockableChest((Chest) doubleChest.getLeftSide()),
                        new LockableChest((Chest) doubleChest.getRightSide()));
            } else {
                return new LockableChest(chest);
            }
        });
    }

    public void registerContainer(Class<? extends BlockState> blockStateClass, Function<BlockState, LockableContainer<?>> stateToContainerFunc) {
        blockstateToContainerSupplierMap.put(blockStateClass, stateToContainerFunc);
    }

    public LockableContainer<?> newContainerFromState(BlockState blockState) {
        for (Class<? extends BlockState> blockStateClass : blockstateToContainerSupplierMap.keySet()) {
            if (blockStateClass.isInstance(blockState)) { // if blocksStateClass == Chest.class, then equivalent to: blockState instanceof Chest
                Function<BlockState, LockableContainer<?>> stateToContainerFunc = blockstateToContainerSupplierMap.get(blockStateClass);
                if (stateToContainerFunc != null) {
                    return stateToContainerFunc.apply(blockState);
                }
            }
        }
        return null;
    }
}
