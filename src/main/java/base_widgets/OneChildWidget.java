package base_widgets;

import com.googlecode.lanterna.screen.Screen;

public abstract class OneChildWidget extends Widget {
    final protected Widget child;

    public OneChildWidget(Widget child) {
        assert child != null ;
        this.child = child;
    }

    @Override
    public int getMinHeight(int availableWidth) {
        return child.getMinHeight(availableWidth);
    }

    @Override
    public int getMinWidth(int availableHeight) {
        return child.getMinWidth(availableHeight);
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
    public int getAbsoluteMinHeight() {
        return child.getAbsoluteMinHeight();
    }

    @Override
    public int getAbsoluteMinWidth() {
        return child.getAbsoluteMinWidth();
    }

    @Override
    public boolean hasComplexLayout() {
        return child.hasComplexLayout();
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        child.rawRender(x, y, width, height, screen, errorRecorder);
    }

    @Override
    public void insertIntoWidgetTree(WidgetContext c, RerenderParent parent) {
        child.insertIntoWidgetTree(c, parent);
    }

    @Override
    public void insertIntoWidgetTree(WidgetContext c){
        child.insertIntoWidgetTree(c);
    }

    @Override
    public void takeOutOfWidgetTree() {
        child.takeOutOfWidgetTree();
    }
}
