package base_widgets;

public abstract class WidgetStateController<StateType> {
    public WidgetStateController(StateType initialState) {
        currentState = initialState;
    }

    volatile StateType currentState; // use guarded blocks instead?
    volatile private boolean hasBeenInitialized = false;

    protected StateType state() {
        assert currentState != null : "cannot get state, is still null";
        return currentState;
    }

    protected void emit(StateType state) {
        currentState = state;
    }

    void safeInit() {
        if(hasBeenInitialized) {
            throw new IllegalStateException("Cannot init a StateController twice");
        }
        hasBeenInitialized = true;
        init();
    }

    public abstract void init();

    /* volatile*/ int x, y, width, height;

    void updateRenderCoordinates(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
