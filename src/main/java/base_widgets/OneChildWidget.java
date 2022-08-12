package base_widgets;

import com.googlecode.lanterna.screen.Screen;

public abstract class OneChildWidget extends Widget {
    final protected Widget child;

    public OneChildWidget(Widget child) {
        assert child != null ;
        this.child = child;
    }

    @Override
    public int getMinHeight() {
        return child.getMinHeight();
    }

    @Override
    public int getMinWidth() {
        return child.getMinWidth();
    }

    @Override
    public int getMaxHeight(int maxWidth, int maxHeight) {
        return child.getMaxHeight(maxWidth, maxHeight);
    }

    @Override
    public int getMaxWidth(int maxWidth, int maxHeight) {
        return child.getMaxWidth(maxWidth, maxHeight);
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen) {
        child.rawRender(x, y, width, height, screen);
    }
}
