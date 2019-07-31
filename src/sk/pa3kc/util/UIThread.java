package sk.pa3kc.util;

import sk.pa3kc.mylibrary.pojo.ObjectPointer;
import sk.pa3kc.mylibrary.util.ArrayUtils;

public class UIThread {
    Runnable[] updateRunnables = new Runnable[0];
    Runnable[] renderRunnables = new Runnable[0];

    public UIThread() {}

    // region Adders
    public boolean[] addUpdateRunnables(Runnable... runnables) {
        ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.updateRunnables);
        return ArrayUtils.addAll(pointer, runnables);
    }
    public boolean[] addRenderRunnables(Runnable... runnables) {
        ObjectPointer<Runnable[]> pointer = new ObjectPointer<Runnable[]>(this.renderRunnables);
        return ArrayUtils.addAll(pointer, runnables);
    }
    // endregion
}
