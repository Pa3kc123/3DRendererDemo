package sk.pa3kc.pojo;

public class ValidationResult {
    public final int index;
    public final boolean valid;

    public ValidationResult(boolean valid, int index) {
        this.valid = valid;
        this.index = index;
    }
}
