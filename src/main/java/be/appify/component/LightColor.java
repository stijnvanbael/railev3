package be.appify.component;

public enum LightColor {
    RED(2),
    GREEN(1),
    ORANGE(3);
    private int value;

    LightColor(int value) {
        this.value = value;
    }


    public int value() {
        return value;
    }
}
