package sk.pa3kc.singletons;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import sk.pa3kc.pojo.KeyboardKeyInfo;

public class Keyboard {
    private static Keyboard instance = new Keyboard();

    private final KeyboardKeyInfo[] supportedChars;

    private char lastRequestedChar = '\0';
    private KeyboardKeyInfo lastRequestedKeyInfo = null;

    private Keyboard() {
        this.supportedChars = new ArrayList<KeyboardKeyInfo>() {
            private static final long serialVersionUID = 1L;

            {
                super.add(new KeyboardKeyInfo(KeyEvent.VK_G));
            }
        }.toArray(new KeyboardKeyInfo[0]);
    }

    public static Keyboard getInst() { return instance; }

    public int getSupportedCharCount() { return this.supportedChars.length; }
    public KeyboardKeyInfo getKeyInfo(int index) { return this.supportedChars[index]; }
    public KeyboardKeyInfo getKeyInfo(char symbol) {
        if (symbol == lastRequestedChar) return this.lastRequestedKeyInfo;

        KeyboardKeyInfo result = null;

        for (KeyboardKeyInfo keyInfo : this.supportedChars)
        if (keyInfo.symbol == symbol) {
            this.lastRequestedChar = symbol;
            this.lastRequestedKeyInfo = keyInfo;
            result = keyInfo;
            break;
        }

        return result;
    }

    public void processKeyEvent(KeyEvent event) {
        for (KeyboardKeyInfo keyInfo : this.supportedChars)
        if (keyInfo.keyId == event.getKeyCode())
        switch (event.getID()) {
            case KeyEvent.KEY_PRESSED: keyInfo.onPressed(); break;
            case KeyEvent.KEY_RELEASED: keyInfo.onReleased(); break;
        }
    }
}
