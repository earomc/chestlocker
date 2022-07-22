package net.earomc.chestlocker.lockables;

import net.earomc.chestlocker.LockResult;
import org.bukkit.block.Chest;

import javax.annotation.Nullable;

public class LockableDoubleChest extends LockableContainer<Chest> {

    private final LockableChest chestLeft;
    private final LockableChest chestRight;

    public LockableDoubleChest(LockableChest chestLeft, LockableChest chestRight) {
        super(null, "Double chest");
        this.chestLeft = chestLeft;
        this.chestRight = chestRight;
        //Bukkit.broadcastMessage(this.toString());
    }

    /**
     * @return Returns the chest part in the double chest that is not the given chestState
     */

    @Nullable
    public LockableChest getOtherChest(Chest chestState) {
        if (chestState.equals(this.getChestLeft().getState())) {
            return this.getChestRight();
        } else if (chestState.equals(this.getChestRight().getState())) {
            return this.getChestLeft();
        } else return null;
    }

    private LockableChest getOtherChest(LockableChest chest) {
        if (chest.equals(chestLeft)) {
            return chestRight;
        } else if (chest.equals(chestRight)) {
            return chestLeft;
        }
        return null;
    }

    @Override
    public LockResult tryLock(String lock) {
        if (!isLocked()) {
            this.lock(lock);
            return LockResult.SUCCESS;
        } else {
            return LockResult.LOCK_ALREADY_SET;
        }
    }

    @Override
    public void lock(String lock) {
        chestLeft.lock(lock);
        chestRight.lock(lock);
        //Bukkit.broadcastMessage("Double chest locked!!!!");
    }

    @Override
    public void unlock() {
        chestLeft.unlock();
        chestRight.unlock();
    }

    public LockableChest getChestLeft() {
        return chestLeft;
    }

    public LockableChest getChestRight() {
        return chestRight;
    }

    @Override
    public String getLock() {
        if (chestLeft.getLock().equals(chestRight.getLock())) {
            return chestLeft.getLock();
        }
        else return "";
    }

    @Override
    public boolean isLocked() {
        return chestRight.isLocked() && chestLeft.isLocked();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " lock: " + getLock() + ", chestRightLock: " + chestRight.getLock()
                + ", chestLeftLock: " + chestLeft.getLock() + ", @" + hashCode();
    }

}
