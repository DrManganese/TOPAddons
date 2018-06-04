package io.github.drmanganese.topaddons.styles;

public class SimpleProgressStyle {

    private int width = 100;
    private int height = 12;
    private int borderColor = 0xffffffff;
    private int backgroundColor = 0xff000000;
    private int fillColor = 0xffaaaaaa;
    private int alternateFillColor = 0xffaaaaaa;

    public SimpleProgressStyle width(int width) {
        this.width = width;
        return this;
    }

    public SimpleProgressStyle height(int height) {
        this.height = height;
        return this;
    }

    public SimpleProgressStyle borderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public SimpleProgressStyle backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public SimpleProgressStyle fillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public SimpleProgressStyle alternateFillColor(int alternateFillColor) {
        this.alternateFillColor = alternateFillColor;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getFillColor() {
        return fillColor;
    }

    public int getAlternateFillColor() {
        return alternateFillColor;
    }
}
