package widgets;

import com.googlecode.lanterna.screen.Screen;

public class Align extends OneChildWidget {
    final private Alignment alignment;

    public Align(Alignment alignment, Widget child) {
        super(child);
        this.alignment = alignment;
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen) {
        // TODO: split up in different method
        final int childWidth = child.getMaxWidth(width, height);
        final int restWidth = width - childWidth;
        final int childX = switch (alignment.getHorizontalAlignment()) {
            case start -> x;
            case center -> x + restWidth / 2;
            case end -> x + restWidth;
        };
        final int childHeight = child.getMaxHeight(width, height);
        final int restHeight = height - childHeight;
        final int childY = switch (alignment.getVerticalAlignment()) {
            case start -> y;
            case center -> y + restHeight / 2;
            case end -> y + restHeight;
        };
        super.rawRender(childX, childY, childWidth, childHeight, screen);
    }

    @Override
    public int getMaxWidth(int maxWidth, int maxHeight) {
        return maxWidth;
    }

    @Override
    public int getMaxHeight(int maxWidth, int maxHeight) {
        return maxHeight;
    }
}
