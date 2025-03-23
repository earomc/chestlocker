package net.earomc.chestlocker.lockables;

import net.earomc.chestlocker.LockResult;
import net.earomc.chestlocker.UnlockResult;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static net.earomc.chestlocker.ChestLocker.LOCK;

public class LockableContainer<T extends TileState> {

    protected final T state;
    protected final String name;

    public LockableContainer(T lockableBlockState, String name) {
        this.state = lockableBlockState;
        this.name = name;
    }

    public LockResult tryLock(String lock) {
        if (!isLocked()) {
            lock(lock);
            return LockResult.SUCCESS;
        } else {
            return LockResult.LOCK_ALREADY_SET;
        }
    }

    public void lock(String lock) {
        pdc().set(LOCK, PersistentDataType.STRING, lock);
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
        pdc().remove(LOCK);
        state.update();
    }

    public String getName() {
        return name;
    }

    public String getLock() {
        return pdc().get(LOCK, PersistentDataType.STRING);
    }

    public boolean isLocked() {
        return pdc().has(LOCK, PersistentDataType.STRING);
    }

    private PersistentDataContainer pdc() {
        return this.state.getPersistentDataContainer();
    }

    public T getState() {
        return state;
    }
}
