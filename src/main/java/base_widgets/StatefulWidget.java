package base_widgets;

import com.googlecode.lanterna.screen.Screen;

import java.util.HashMap;

public abstract class StatefulWidget<StateType, WidgetControllerType extends WidgetController<StateType>> extends ControlledWidget<StateType, WidgetControllerType> implements WidgetContext {
    protected StatefulWidget(String id) {
        super(id);
    }

    protected StatefulWidget() {
    }

    private HashMap<String, WidgetController<?>> oldChildWidgetControllers;
    private boolean rebuilding = false;
    private HashMap<String, WidgetController<?>> childWidgetControllers = new HashMap<>();
    private Widget currentWidgetTree;

    @Override
    protected void beforeStateRender(StateType s, boolean isInitialRender) {
        if (!isInitialRender) {
            oldChildWidgetControllers = childWidgetControllers;
            childWidgetControllers = new HashMap<>();
            currentWidgetTree.takeOutOfWidgetTree();
        }
        rebuilding = true;
        currentWidgetTree = build(s);
        currentWidgetTree.insertIntoWidgetTree(this, this::rerender);
        rebuilding = false;
    }

    protected abstract Widget build(StateType state);

    @Override
    protected void render(int x, int y, int width, int height, WidgetErrorRecorder errorRecorder) {
        currentWidgetTree.rawRender(x, y, width, height, cachedScreen, errorRecorder);
    }

    @Override
    public Screen getScreen() {
        return cachedScreen;
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

    @Override
    public <_StateType, _WidgetControllerType extends WidgetController<_StateType>> void registerWidgetController(
            String stateId, _WidgetControllerType state
    ) {
        assert rebuilding : "can only insert, if in rebuilding";
        childWidgetControllers.put(stateId, state);
    }

    @Override
    public <_StateType, _WidgetControllerType extends WidgetController<_StateType>> boolean widgetControllerHasEverBeenRegistered(
            String stateId
    ) {
        assert rebuilding : "can only insert, if in rebuilding";
        if(oldChildWidgetControllers == null) {
            return false;
        }
        _WidgetControllerType widgetController = (_WidgetControllerType) oldChildWidgetControllers.get(stateId);
        return widgetController != null;
    }

    @Override
    public <_StateType, _WidgetControllerType extends WidgetController<_StateType>> _WidgetControllerType reinsertWidgetControllerInTree(
            String stateId
    ) {
        assert rebuilding : "can only insert, if in rebuilding";
        _WidgetControllerType oldWidget = null;
        if (oldChildWidgetControllers != null) {
            oldWidget = (_WidgetControllerType) oldChildWidgetControllers.get(stateId);
        }
        if (oldWidget == null) {
            throw new IllegalStateException("Cannot reinsert state, if it does not exist");
        }
        childWidgetControllers.put(stateId, oldWidget);
        return oldWidget;
    }
}
