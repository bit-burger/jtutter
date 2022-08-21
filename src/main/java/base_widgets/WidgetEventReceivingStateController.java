package base_widgets;

public abstract class WidgetEventReceivingStateController<StateType> extends WidgetStateController<StateType> {
    public WidgetEventReceivingStateController(StateType initialState) {
        super(initialState);
    }
}
