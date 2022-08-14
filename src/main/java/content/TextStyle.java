package content;

import com.googlecode.lanterna.TextColor;
import layout.BasicAlignment;

public final class TextStyle {

    private TextWrappingBehavior wrappingBehavior;
    private BasicAlignment textAlign;
    private TextColor color;
    private TextColor backgroundColor;

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

    public void setWrappingBehavior(TextWrappingBehavior wrappingBehavior) {
        this.wrappingBehavior = wrappingBehavior;
    }

    public void setTextAlign(BasicAlignment textAlign) {
        this.textAlign = textAlign;
    }

    public void setColor(TextColor color) {
        this.color = color;
    }

    public void setBackgroundColor(TextColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
