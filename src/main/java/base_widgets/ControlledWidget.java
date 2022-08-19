package base_widgets;

import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.UUID;


abstract class ControlledWidget<StateType, WidgetControllerType extends WidgetController<StateType>> extends Widget {

    private final String id;
    //    private final boolean rerenderBackground;
    private WidgetControllerType controller;
    Screen cachedScreen;
    WidgetContext cachedContextParent;
    private WidgetErrorRecorder cachedErrorRecorder;
    private RerenderParent cachedRerenderParent; // can be null
    private int lastX = -1, lastY = -1, lastWidth = -1, lastHeight = -1;
    private boolean lastHasComplexLayout;
    private int lastAbsoluteMinWidth = -1, lastAbsoluteMinHeight = -1, lastMinWidth = -1, lastMinHeight = -1,
            lastMaxWidth = -1, lastMaxHeight = -1;
    private boolean hasHadFirstRender = false;
    private boolean stateHasUpdatedBeforeFirstRender = false;

    protected ControlledWidget(String id) {
        this.id = id;
    }

    protected ControlledWidget() {
        id = UUID.randomUUID().toString();
    }

    public StateType state() {
        return controller.state();
    }

    public WidgetControllerType controller() {
        return controller;
    }

    /**
     * rerender, either with state changes or if sub ControlledWidget changed dimensions
     * called by implementation of ControlledWidget or by sub ControlledWidget
     * <p>
     * will either call parent to rerender, or will rerender directly
     */
    void rerender() {
        boolean newHasComplexLayout = hasRealComplexLayout();
        int newAbsoluteMinWidth = getRealAbsoluteMinWidth();
        int newAbsoluteMinHeight = getRealAbsoluteMinHeight();
        int newMinWidth = getRealMinWidth(lastHeight);
        int newMinHeight = getRealMinHeight(lastWidth);
        int newMaxWidth = getRealMaxWidth(lastWidth, lastHeight);
        int newMaxHeight = getRealMaxHeight(lastWidth, lastHeight);
        if (cachedRerenderParent == null || (newHasComplexLayout == lastHasComplexLayout && newAbsoluteMinWidth == lastAbsoluteMinWidth && newAbsoluteMinHeight == lastAbsoluteMinHeight && newMinWidth == lastMinWidth && newMinHeight == lastMinHeight && newMaxWidth == lastMaxWidth && newMaxHeight == lastMaxHeight)) {
            render(lastX, lastY, lastWidth, lastHeight, cachedErrorRecorder);
            lastAbsoluteMinWidth = newAbsoluteMinWidth;
            lastAbsoluteMinHeight = newAbsoluteMinHeight;
            lastMinWidth = newMinWidth;
            lastMinHeight = newMinHeight;
            lastMaxWidth = newMaxWidth;
            lastMaxHeight = newMaxHeight;
            try {
                cachedScreen.refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            cachedRerenderParent.refresh();
        }
    }


    private void update(StateType s) {
        if (!hasHadFirstRender) {
            stateHasUpdatedBeforeFirstRender = true;
        } else {
            beforeStateRender(s, false);
            rerender();
        }
    }

    abstract protected void beforeStateRender(StateType s, boolean isInitialRender);

    @Override
    final public void rawRender(
            int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder
    ) {
        controller.updateRenderCoordinates(x, y, width, height);
        lastX = x;
        lastY = y;
        lastWidth = width;
        lastHeight = height;
        this.cachedScreen = screen;
        if (!hasHadFirstRender) {
            if(stateHasUpdatedBeforeFirstRender) {
                beforeStateRender(controller.state(), false);
            }
            hasHadFirstRender = true;
        }
        render(x, y, width, height, errorRecorder);
    }

    protected abstract void render(int x, int y, int width, int height, WidgetErrorRecorder errorRecorder);

    @Override
    final public void insertIntoWidgetTree(WidgetContext c, RerenderParent rerenderParent) {
        this.cachedRerenderParent = rerenderParent;
        insertIntoWidgetTree(c);
    }

    @Override
    final public void insertIntoWidgetTree(WidgetContext c) {
        cachedScreen = c.getScreen();
        if (c.widgetControllerHasEverBeenRegistered(id)) {
            controller = createStateController();
            controller.init();
            c.registerWidgetController(id, controller);
        } else {
            controller = c.reinsertWidgetControllerInTree(id);
        }
        controller.listen(this::update);
        beforeStateRender(controller.state(), true);
    }

    @Override
    public void takeOutOfWidgetTree() {
        controller.stopListening(this::update);
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
