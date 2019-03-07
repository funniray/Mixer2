package com.funniray.mixer.interactive;

public class InteractiveEvent {

    private boolean isCancelled = false;
    private boolean shouldCharge = true;
    private boolean shouldUpdate = false;
    private boolean shouldReset = false;

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean shouldCharge() {
        return shouldCharge;
    }

    public void setShouldCharge(boolean shouldCharge) {
        this.shouldCharge = shouldCharge;
    }

    public boolean shouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }

    public boolean shouldReset() {
        return shouldReset;
    }

    public void setShouldReset(boolean shouldReset) {
        this.shouldReset = shouldReset;
    }

    void resetTempVals(){
        this.isCancelled = false;
        this.shouldCharge = true;
        this.shouldReset = false;
        this.shouldUpdate = false;
    }
}
