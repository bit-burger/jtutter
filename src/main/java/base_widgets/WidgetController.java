package base_widgets;

public abstract class WidgetController<State> {
    public interface Listener<State> {
        void update(State s);
    }

    State currentState;

    protected State state() {
        assert currentState != null : "cannot get state, is still null";
        return currentState;
    }

    protected void newState(State state) {
        currentState = state;
        if (stateListener != null) {
            stateListener.update(state);
        }
    }

    public abstract void init();

//    void dispose() {
//    }

    private Listener<State> stateListener;

    void listen(Listener<State> stateListener) {
        if (this.stateListener != null) {
            throw new IllegalStateException("Only one Listener can listen to a WidgetController");
        }
        this.stateListener = stateListener;
    }

    void stopListening(Listener<State> stateListener) {
        if (this.stateListener == stateListener) {
            this.stateListener = null;
        }
    }

    int x, y, width, height;

    void updateRenderCoordinates(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
