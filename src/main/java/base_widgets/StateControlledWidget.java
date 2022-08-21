package base_widgets;

import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.UUID;


abstract class StateControlledWidget<StateType, WidgetControllerType extends WidgetStateController<StateType>> extends Widget {

    private final String id;
    //    private final boolean rerenderBackground;
    private WidgetControllerType controller;
    WidgetContext cachedContextParent;
    private int lastX = -1, lastY = -1, lastWidth = -1, lastHeight = -1;
    private boolean lastHasComplexLayout;
    private int lastAbsoluteMinWidth = -1, lastAbsoluteMinHeight = -1, lastMinWidth = -1, lastMinHeight = -1,
            lastMaxWidth = -1, lastMaxHeight = -1;

    protected StateControlledWidget(String id) {
        this.id = id;
    }

    protected StateControlledWidget() {
        id = UUID.randomUUID().toString();
    }

    public StateType state() {
        return controller.state();
    }

    public WidgetControllerType controller() {
        return controller;
    }

    abstract protected void beforeStateRender(StateType s);

    private StateType lastRenderedState = null;

    @Override
    final public void rawRender(
            int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder
    ) {
        controller.updateRenderCoordinates(x, y, width, height);
        lastX = x;
        lastY = y;
        lastWidth = width;
        lastHeight = height;
        if (lastRenderedState != controller.currentState) {
            beforeStateRender(controller.currentState);
        }
        render(x, y, width, height, screen, errorRecorder);
    }

    // TODO: big update needed, lots of problems currently
    boolean checkAndUpdateBoundariesDifference() {
        boolean newHasComplexLayout = hasRealComplexLayout();
        int newAbsoluteMinWidth = getRealAbsoluteMinWidth();
        int newAbsoluteMinHeight = getRealAbsoluteMinHeight();
        int newMinWidth = getRealMinWidth(lastHeight);
        int newMinHeight = getRealMinHeight(lastWidth);
        int newMaxWidth = getRealMaxWidth(lastWidth, lastHeight);
        int newMaxHeight = getRealMaxHeight(lastWidth, lastHeight);
        if (newHasComplexLayout == lastHasComplexLayout && newAbsoluteMinWidth == lastAbsoluteMinWidth && newAbsoluteMinHeight == lastAbsoluteMinHeight && newMinWidth == lastMinWidth && newMinHeight == lastMinHeight && newMaxWidth == lastMaxWidth && newMaxHeight == lastMaxHeight) {
            return false;
        }
        lastAbsoluteMinWidth = newAbsoluteMinWidth;
        lastAbsoluteMinHeight = newAbsoluteMinHeight;
        lastMinWidth = newMinWidth;
        lastMinHeight = newMinHeight;
        lastMaxWidth = newMaxWidth;
        lastMaxHeight = newMaxHeight;
        return true;
    }

    private void rerender(WidgetErrorRecorder errorRecorder) {
        render(lastX, lastY, lastWidth, lastHeight, cachedContextParent.getScreen(), errorRecorder);
        try {
            cachedContextParent.getScreen().refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean boundariesAreDifferentOrRerender(WidgetErrorRecorder errorRecorder) {
        if (checkAndUpdateBoundariesDifference()) {
            return true;
        }
        rerender(errorRecorder);
        return false;
    }

    @Override
    public boolean shouldParentRerender(WidgetErrorRecorder errorRecorder) {
        if (lastRenderedState != controller.currentState) {
            lastRenderedState = controller.currentState;
            beforeStateRender(controller.currentState); // makes sure children also will get their new state
            return boundariesAreDifferentOrRerender(errorRecorder);
        }
        return false;
    }

    protected abstract void render(
            int x,
            int y,
            int width,
            int height,
            Screen screen,
            WidgetErrorRecorder errorRecorder
    );

    @Override
    final public void insertIntoWidgetTree(WidgetContext c) {
        cachedContextParent = c;
        if (c.widgetControllerHasEverBeenRegistered(id)) {
            controller = c.reinsertWidgetControllerInTree(id);
        } else {
            controller = createStateController();
            c.registerWidgetController(id, controller);
        }
        lastRenderedState = controller.currentState;
        beforeStateRender(controller.currentState);
    }

    protected abstract WidgetControllerType createStateController();

    protected abstract boolean hasRealComplexLayout();

    protected abstract int getRealMaxWidth(int maxAvailableWidth, int maxAvailableHeight);

    protected abstract int getRealMaxHeight(int maxAvailableWidth, int maxAvailableHeight);

    protected abstract int getRealMinHeight(int availableWidth);

    protected abstract int getRealMinWidth(int availableHeight);

    protected abstract int getRealAbsoluteMinHeight();

    protected abstract int getRealAbsoluteMinWidth();

    @Override
    public final boolean hasComplexLayout() {
        return hasRealComplexLayout();
    }

    @Override
    public final int getAbsoluteMinWidth() {
        return getRealAbsoluteMinWidth();
    }

    @Override
    public final int getAbsoluteMinHeight() {
        return getRealAbsoluteMinHeight();
    }

    @Override
    public final int getMaxWidth(int maxAvailableWidth, int maxAvailableHeight) {
        return getRealMaxWidth(maxAvailableWidth, maxAvailableHeight);
    }

    @Override
    public final int getMaxHeight(int maxAvailableWidth, int maxAvailableHeight) {
        return getRealMaxHeight(maxAvailableWidth, maxAvailableHeight);
    }

    @Override
    public final int getMinHeight(int availableWidth) {
        return getRealMinHeight(availableWidth);
    }

    @Override
    public final int getMinWidth(int availableHeight) {
        return getRealMinWidth(availableHeight);
    }
}
