package sk.pa3kc.util;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class KeyboardKeyInfo {
    //Variables
    public final char symbol;
    public final int keyId;

    private final ArrayList<Runnable> onPressedActions = new ArrayList<Runnable>();
    private final ArrayList<Runnable> onTypedActions = new ArrayList<Runnable>();
    private final ArrayList<Runnable> onReleasedActions = new ArrayList<Runnable>();

    private boolean isPressed = false;

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
        this.onPressedActions.add(onPressedAction);
        this.onReleasedActions.add(onReleasedAction);
    }

    //Getters
    public boolean isPressed() { return this.isPressed; }
    public boolean isReleased() { return !this.isPressed; }
    public ArrayList<Runnable> getOnPressedActions() {
        return this.onPressedActions;
    }
    public ArrayList<Runnable> getOnTypedActions() {
        return this.onTypedActions;
    }
    public ArrayList<Runnable> getOnReleasedActions() {
        return this.onReleasedActions;
    }

    //Events
    public void onPressed() {
        if (!this.isPressed) {
            this.isPressed = true;

            for (Runnable action : this.onPressedActions)
            if (action != null)
                action.run();
        }
    }
    public void onTyped() {
        for (Runnable action : this.onTypedActions)
        if (action != null)
            action.run();
    }
    public void onReleased() {
        if (this.isPressed) {
            this.isPressed = false;

            for (Runnable action : this.onReleasedActions)
            if (action != null)
                action.run();
        }
    }
}
