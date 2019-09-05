package sk.pa3kc.singletons;

import java.awt.event.KeyEvent;
import java.util.Iterator;

import sk.pa3kc.util.KeyboardKeyInfo;

public class Keyboard {
    private Keyboard() {}

    private static final KeyboardKeyCollection _supportedChars = new KeyboardKeyCollection();

    private static char _lastRequestedChar = '\0';
    private static KeyboardKeyInfo _lastRequestedKeyInfo = null;

    public static int getSupportedCharCount() { return _supportedChars.length; }
    public static KeyboardKeyInfo getKeyInfo(int index) { return _supportedChars.get(index); }
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

final class KeyboardKeyCollection implements Iterable<KeyboardKeyInfo> {
    private final KeyboardKeyInfo[] collection;
    final int length;

    KeyboardKeyCollection() {
        final int [] arr = new int[] {
            KeyEvent.VK_G,
            KeyEvent.VK_H,
            KeyEvent.VK_O,
            KeyEvent.VK_CONTROL
        };

        this.collection = new KeyboardKeyInfo[arr.length];
        this.length = arr.length;

        for (int i = 0; i < arr.length; i++)
            this.collection[i] = new KeyboardKeyInfo(arr[i]);
    }

    KeyboardKeyInfo get(int index) {
        if (index < 0 || index > this.length) throw new IndexOutOfBoundsException();

        return this.collection[index];
    }

    @Override
    public Iterator<KeyboardKeyInfo> iterator() {
        return new Iterator<KeyboardKeyInfo>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return this.index >= 0 && this.index < length;
            }

            @Override
            public KeyboardKeyInfo next() {
                return collection[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
	}
}
