package net.earomc.chestlocker.lockables;

import net.earomc.chestlocker.LockResult;
import net.earomc.chestlocker.UnlockResult;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lockable;

public class LockableContainer<T extends Lockable & BlockState> {

    protected final T state;
    protected final String name;

    public LockableContainer(T lockableBlockState, String name) {
        this.state = lockableBlockState;
        this.name = name;
    }

    public LockResult tryLock(String lock) {
        if (!state.isLocked()) {
            lock(lock);
            return LockResult.SUCCESS;
        } else {
            return LockResult.LOCK_ALREADY_SET;
        }
    }
    public void lock(String lock) {
        // /data merge block xyz {lock:{components:{"minecraft:custom_name":'"lock"'}}}
        // FIXME: This doesn't work anymore :( Need to wait for Paper/Bukkit update
        state.setLock(lock); // deprecated API
        state.update();
    }

    public UnlockResult tryUnlock(String lock) {
        if (isLocked()) {
            if (getLock().equals(lock)) {
                unlock();
                return UnlockResult.SUCCESS;
            } else {
                return UnlockResult.INCORRECT_LOCK;
            }
        } else {
            return UnlockResult.CONTAINER_NOT_LOCKED;
        }
    }

    public void unlock() {
        state.setLock(""); // deprecated API
        state.update();
    }
    public String getName() {
        return name;
    }
    public String getLock() {
        // problem is we can't query the item that locked the container ...
        return state.getLock(); // deprecated API
    }
    public boolean isLocked() {
        return state.isLocked();
    }

    public T getState() {
        return state;
    }
}
