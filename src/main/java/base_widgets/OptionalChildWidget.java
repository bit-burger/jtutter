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
    public int getMinHeight(int availableWidth) {
        if (child == null) {
            return super.getMinHeight(availableWidth);
        }
        return child.getMinHeight(availableWidth);
    }

    @Override
    public int getMinWidth(int availableHeight) {
        if (child == null) {
            return super.getMinWidth(availableHeight);
        }
        return child.getMinWidth(availableHeight);
    }

    @Override
    public int getMaxHeight(int maxWidth, int maxHeight) {
        if (child == null) {
            return super.getMaxHeight(maxWidth, maxHeight);
        }
        return child.getMaxHeight(maxWidth, maxHeight);
    }

    @Override
    public int getMaxWidth(int maxWidth, int maxHeight) {
        if (child == null) {
            return super.getMaxWidth(maxWidth, maxHeight);
        }
        return child.getMaxWidth(maxWidth, maxHeight);
    }

    @Override
    public int getAbsoluteMinHeight() {
        if (child == null) {
            return super.getAbsoluteMinHeight();
        }
        return child.getAbsoluteMinHeight();
    }

    @Override
    public int getAbsoluteMinWidth() {
        if (child == null) {
            return super.getAbsoluteMinWidth();
        }
        return child.getAbsoluteMinWidth();
    }

    @Override
    public boolean hasComplexLayout() {
        if (child == null) {
            return super.hasComplexLayout();
        }
        return child.hasComplexLayout();
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        if (child != null) {
            child.rawRender(x, y, width, height, screen, errorRecorder);
        }
    }

    @Override
    public void insertIntoWidgetTree(WidgetContext c, RerenderParent parent) {
        if (child != null) {
            child.insertIntoWidgetTree(c, parent);
        }
    }

    @Override
    public void insertIntoWidgetTree(WidgetContext c) {
        if (child != null) {
            child.insertIntoWidgetTree(c);
        }
    }

    @Override
    public void takeOutOfWidgetTree() {
        if (child != null) {
            child.takeOutOfWidgetTree();
        }
    }
}
