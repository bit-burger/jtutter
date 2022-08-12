package base_widgets;

import com.googlecode.lanterna.screen.Screen;

public abstract class OptionalChildWidget extends Widget {
    final protected Widget child;

    public OptionalChildWidget(Widget child) {
        this.child = child;
    }

    public OptionalChildWidget() {
        this.child = null;
    }

    @Override
    public int getMinWidth() {
        if (child == null) {
            return super.getMinWidth();
        }
        return child.getMinWidth();
    }

    @Override
    public int getMinHeight() {
        if (child == null) {
            return super.getMinWidth();
        }
        return child.getMinHeight();
    }

    @Override
    public int getMaxWidth(int maxAvailableWidth, int maxAvailableHeight) {
        if (child != null) {
            return child.getMaxWidth(maxAvailableWidth, maxAvailableHeight);
        }
        return super.getMaxWidth(maxAvailableWidth, maxAvailableHeight);
    }

    @Override
    public int getMaxHeight(int maxAvailableWidth, int maxAvailableHeight) {
        if (child != null) {
            return child.getMaxHeight(maxAvailableWidth, maxAvailableHeight);
        }
        return super.getMaxHeight(maxAvailableWidth, maxAvailableHeight);
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen) {
        if( child != null) {
            child.rawRender(x, y, width, height, screen);
        }
    }
}
