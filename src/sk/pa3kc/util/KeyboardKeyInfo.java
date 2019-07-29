package sk.pa3kc.util;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class KeyboardKeyInfo {
    //Variables
    public final char symbol;
    public final int keyId;

    private final ArrayList<Runnable> onPressedActions = new ArrayList<Runnable>();
    private final ArrayList<Runnable> onReleasedActions = new ArrayList<Runnable>();

    private boolean _isPressed = false;

    //Constructor
    public KeyboardKeyInfo(int keyId) {
        this.symbol = KeyEvent.getKeyText(keyId).toLowerCase().charAt(0);
        this.keyId = keyId;
    }
    public KeyboardKeyInfo(char symbol, int keyId) {
        this.symbol = symbol;
        this.keyId = keyId;
    }
    public KeyboardKeyInfo(char symbol, int keyId, Runnable onPressedAction, Runnable onReleasedAction) {
        this.symbol = symbol;
        this.keyId = keyId;
        this.addOnPressedAction(onPressedAction);
        this.addOnReleasedAction(onReleasedAction);
    }

    //Adders
    public boolean addOnPressedAction(Runnable action) {
        if (action == null) return false;

        boolean res = !this.onPressedActions.contains(action);

        if (res) this.onPressedActions.add(action);

        return res;
    }
    public boolean addOnReleasedAction(Runnable action) {
        if (action == null) return false;

        boolean res = !this.onReleasedActions.contains(action);

        if (!res) this.onReleasedActions.add(action);

        return res;
    }

    //Removers
    public boolean removeOnPressedAction(Runnable action) {
        boolean res = this.onPressedActions.contains(action);

        if (res) this.onPressedActions.remove(action);

        return res;
    }
    public boolean removeOnReleasedAction(Runnable action) {
        boolean res = this.onReleasedActions.contains(action);

        if (res) this.onReleasedActions.remove(action);

        return res;
    }

    //Getters
    public boolean isPressed() { return this._isPressed; }
    public boolean isReleased() { return !this._isPressed; }

    //Events
    public void onPressed() {
        if (!this._isPressed) {
            this._isPressed = true;

            for (Runnable action : this.onPressedActions)
            if (action != null)
                action.run();
        }
    }
    public void onReleased() {
        if (this._isPressed) {
            this._isPressed = false;

            for (Runnable action : this.onReleasedActions)
            if (action != null)
                action.run();
        }
    }
}
