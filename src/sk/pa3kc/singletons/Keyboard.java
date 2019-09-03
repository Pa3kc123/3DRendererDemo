package sk.pa3kc.singletons;

import java.awt.event.KeyEvent;

import sk.pa3kc.util.KeyboardKeyInfo;

public class Keyboard {
    private Keyboard() {}

    private static final KeyboardKeyInfo[] _supportedChars = new KeyboardKeyInfo[] {
        new KeyboardKeyInfo(KeyEvent.VK_G),
        new KeyboardKeyInfo(KeyEvent.VK_H)
    };

    private static char _lastRequestedChar = '\0';
    private static KeyboardKeyInfo _lastRequestedKeyInfo = null;

    public static int getSupportedCharCount() { return _supportedChars.length; }
    public static KeyboardKeyInfo getKeyInfo(int index) { return _supportedChars[index]; }
    public static KeyboardKeyInfo getKeyInfo(char symbol) {
        if (symbol == _lastRequestedChar) return _lastRequestedKeyInfo;

        KeyboardKeyInfo result = null;

        for (KeyboardKeyInfo keyInfo : _supportedChars)
        if (keyInfo.symbol == symbol) {
            _lastRequestedChar = symbol;
            _lastRequestedKeyInfo = keyInfo;
            result = keyInfo;
            break;
        }

        return result;
    }

    public static void processKeyEvent(KeyEvent event) {
        for (KeyboardKeyInfo keyInfo : _supportedChars)
        if (keyInfo.keyId == event.getKeyCode())
        switch (event.getID()) {
            case KeyEvent.KEY_PRESSED: keyInfo.onPressed(); break;
            case KeyEvent.KEY_TYPED: keyInfo.onTyped(); break;
            case KeyEvent.KEY_RELEASED: keyInfo.onReleased(); break;
        }
    }
}
