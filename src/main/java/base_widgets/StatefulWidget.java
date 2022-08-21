package base_widgets;

import com.googlecode.lanterna.screen.Screen;

import java.util.HashMap;

public abstract class StatefulWidget<StateType, WidgetControllerType extends WidgetStateController<StateType>> extends StateControlledWidget<StateType, WidgetControllerType> {
    protected StatefulWidget(String id) {
        super(id);
    }

    protected StatefulWidget() {
    }

    private Widget currentWidgetTree;

    @Override
    protected void beforeStateRender(StateType s) {
        if (currentWidgetTree != null) {
            currentWidgetTree.takeOutOfWidgetTree();
        }
        currentWidgetTree = build(s);
        currentWidgetTree.insertIntoWidgetTree(cachedContextParent);
    }

    protected abstract Widget build(StateType state);

    @Override
    public boolean shouldParentRerender(WidgetErrorRecorder errorRecorder) {
        if (super.shouldParentRerender(errorRecorder)) {
            return true;
        }
        if (currentWidgetTree.shouldParentRerender(errorRecorder)) {
            return boundariesAreDifferentOrRerender(errorRecorder);
        }
        return false;
    }

    @Override
    protected void render(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        currentWidgetTree.rawRender(x, y, width, height, screen, errorRecorder);
    }

    @Override
    protected boolean hasRealComplexLayout() {
        return currentWidgetTree.hasComplexLayout();
    }

    @Override
    protected int getRealAbsoluteMinWidth() {
        return currentWidgetTree.getAbsoluteMinWidth();
    }

    @Override
    protected int getRealAbsoluteMinHeight() {
        return currentWidgetTree.getAbsoluteMinHeight();
    }

    @Override
    protected int getRealMinWidth(int availableHeight) {
        return currentWidgetTree.getMinWidth(availableHeight);
    }

    @Override
    protected int getRealMinHeight(int availableWidth) {
        return currentWidgetTree.getMinHeight(availableWidth);
    }

    @Override
    protected int getRealMaxWidth(int maxAvailableWidth, int maxAvailableHeight) {
        return currentWidgetTree.getMaxWidth(maxAvailableWidth, maxAvailableHeight);
    }

    @Override
    protected int getRealMaxHeight(int maxAvailableWidth, int maxAvailableHeight) {
        return currentWidgetTree.getMaxHeight(maxAvailableWidth, maxAvailableHeight);
    }
}
