package layout;

import base_widgets.WidgetErrorRecorder;
import com.googlecode.lanterna.screen.Screen;
import base_widgets.OptionalChildWidget;
import base_widgets.Widget;

public final class Padding extends OptionalChildWidget {
    final int top, bottom, left, right;

    public Padding(int top, int bottom, int left, int right, Widget child) {
        super(child);
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        assert top >= 0;
        assert bottom >= 0;
        assert left >= 0;
        assert right >= 0;
    }

    public Padding(int all, Widget child) {
        this(all, all, all, all, child);
    }

    public Padding(int vertical, int horizontal, Widget child) {
        this(vertical, vertical, horizontal, horizontal, child);
    }

    @Override
    public int getMinHeight(int availableWidth) {
        return top + bottom + super.getMinHeight(availableWidth);
    }

    @Override
    public int getMinWidth(int availableHeight) {
        return left + right + super.getMinWidth(availableHeight);
    }

    @Override
    public int getAbsoluteMinWidth() {
        return left + right + super.getAbsoluteMinWidth();
    }

    @Override
    public int getAbsoluteMinHeight() {
        return top + bottom + super.getAbsoluteMinHeight();
    }

    @Override
    public int getMaxHeight(int maxWidth, int maxHeight) {
        // TODO: does this make sense, or should maxHeight be given back, and render used instead of rawRender
        int candidateHeight = top + bottom + super.getMaxHeight(maxWidth, maxHeight);
        return Math.min(maxHeight, candidateHeight);
    }

    @Override
    public int getMaxWidth(int maxWidth, int maxHeight) {
        int candidateWidth = left + right + super.getMaxWidth(maxWidth, maxHeight);
        return Math.min(maxWidth, candidateWidth);
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        super.rawRender(x + left, y + top, width - left - right, height - top - bottom, screen, errorRecorder);
    }
}