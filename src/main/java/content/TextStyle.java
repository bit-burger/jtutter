package content;

import com.googlecode.lanterna.TextColor;
import layout.BasicAlignment;

public final class TextStyle {

    private TextWrappingBehavior wrappingBehavior;
    private BasicAlignment textAlign;
    private TextColor color;
    private TextColor backgroundColor;
    private boolean shrinkWidth;

    public TextStyle(
    ) {
        this.wrappingBehavior = TextWrappingBehavior.words;
        this.textAlign = BasicAlignment.start;
        this.color = TextColor.ANSI.WHITE;
        this.backgroundColor = null;
    }

    public TextWrappingBehavior getWrappingBehavior() {
        return wrappingBehavior;
    }

    public BasicAlignment getTextAlign() {
        return textAlign;
    }

    public TextColor getColor() {
        return color;
    }

    public TextColor getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isShrinkWidth() {
        return shrinkWidth;
    }

    public TextStyle setWrappingBehavior(TextWrappingBehavior wrappingBehavior) {
        this.wrappingBehavior = wrappingBehavior;
        return this;
    }

    public TextStyle setTextAlign(BasicAlignment textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public TextStyle setColor(TextColor color) {
        this.color = color;
        return this;
    }

    public TextStyle setBackgroundColor(TextColor backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public TextStyle shrinkWidth() {
        this.shrinkWidth = true;
        return this;
    }
}
