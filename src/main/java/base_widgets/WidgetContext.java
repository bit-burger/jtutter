package base_widgets;


import com.googlecode.lanterna.screen.Screen;

public interface WidgetContext {
    /**
     * Will throw a runtime exception if another state exists with the stateId in the current scope
     * (current StatefulWidget and above).
     * <p>
     * Will throw a runtime exception if from last build, there is still a State registered
     */
    <StateType, WidgetControllerType extends WidgetController<StateType>> void registerWidgetController(
            String stateId, WidgetControllerType state
    );

    /**
     * Will check if the stateId with the type WidgetControllerType has been in the tree the last build.
     */
    <StateType, WidgetControllerType extends WidgetController<StateType>> boolean widgetControllerHasEverBeenRegistered(
            String stateId
    );

    /**
     * Will reinsert state from last build in tree and give it back
     */
    <StateType, WidgetControllerType extends WidgetController<StateType>> WidgetControllerType reinsertWidgetControllerInTree(
            String stateId
    );

    Screen getScreen();

//    <T> T get(String contextId);
}
