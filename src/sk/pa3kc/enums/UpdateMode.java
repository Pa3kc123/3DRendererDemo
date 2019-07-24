package sk.pa3kc.enums;

public enum UpdateMode {
    ALL(0xF),
    X(0x1),
    Y(0x2),
    Z(0x4),
    DISTANCE(0x8);

    public final boolean x;
    public final boolean y;
    public final boolean z;
    public final boolean w;
    UpdateMode(int code) {
        this.x = ((code & 0x1) >> 0) == 1;
        this.y = ((code & 0x2) >> 1) == 1;
        this.z = ((code & 0x4) >> 2) == 1;
        this.w = ((code & 0x8) >> 3) == 1;
    }
}
