package sk.pa3kc.util;

public class SizedMap<T, E> {
    private int indexer = 0;
    private T[] keys;
    private E[] values;

    public SizedMap(int length) {
        this.keys = (T[]) new Object[length];
        this.values = (E[]) new Object[length];
    }

    public boolean put(T key, E value) {
        if (key == null) return false;

        final boolean result = !this.containsKey(key);

        if (result) {
            this.keys[this.indexer] = key;
            this.values[this.indexer] = value;
            this.indexer++;
        }

        return result;
    }

    public E get(T key) {
        if (this.containsKey(key)) {
            return this.values[this.indexOfKey(key)];
        }

        return null;
    }

    public boolean containsKey(T key) {
        if (key == null) return false;

        for (T mKey : this.keys) {
            if (key.equals(mKey)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsValue(E value) {
        for (E mValue : this.values) {
            if (value.equals(mValue)) {
                return true;
            }
        }

        return false;
    }

    public int indexOfKey(T key) {
        if (key == null) return -1;

        for (int i = 0; i < this.keys.length; i++) {
            if (key.equals(this.keys[i])) {
                return i;
            }
        }

        return -1;
    }

    public int indexOfValue(E value) {
        for (int i = 0; i < this.values.length; i++) {
            if (value.equals(this.values[i])) {
                return i;
            }
        }

        return -1;
    }
}
