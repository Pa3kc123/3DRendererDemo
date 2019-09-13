package sk.pa3kc.pojo;

public class Parameter<T> {
    private final String flagName;
    private T value;

    public Parameter(String flagName, T defaultValue) {
        this.flagName = flagName;
        this.value = defaultValue;
    }

    public String getFlagName() {
        return this.flagName;
    }
    public T getValue() {
        return this.value;
    }
    public Class<?> getValueClass() {
        return this.value.getClass();
    }

    public void setValue(T value) {
        this.value = value;
    }
}
