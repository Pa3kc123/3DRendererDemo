package sk.pa3kc.pojo;

import sk.pa3kc.mylibrary.util.StringUtils;

public class Pair<T, E> {
    private T index;
    private E value;

    public Pair(T index, E value) {
        this.index = index;
        this.value = value;
    }

    public T getIndex() {
        return index;
    }
    public E getValue() {
        return value;
    }

    public void setIndex(T index) {
        this.index = index;
    }
    public void setValue(E value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return StringUtils.build(this.index, " = ", this.value);
    }
}
