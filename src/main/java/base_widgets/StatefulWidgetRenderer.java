package base_widgets;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextGraphicsWriter;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.WrapBehaviour;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import rendering.basic.BasicWidgetErrorRecorder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatefulWidgetRenderer extends Thread implements WidgetContext {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final StatefulWidget<?, ?> widgetToRun;
    private Screen screen;
    // TODO: switching and reinsering
    private HashMap<String, WidgetStateController<?>> oldChildWidgetControllers;
    private boolean rebuilding = false;
    private HashMap<String, WidgetStateController<?>> childWidgetControllers = new HashMap<>();
    private boolean stop = false;

    public StatefulWidgetRenderer(StatefulWidget<?, ?> widgetToRun) {
        this.widgetToRun = widgetToRun;
    }

    public void run() {
        try {
            screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            widgetToRun.insertIntoWidgetTree(this);
            startWidget();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void writeErrorsOnScreen(List<String> errorMessages, Screen screen) {
        TextGraphics textGraphics = screen.newTextGraphics();
        TextGraphicsWriter textGraphicsWriter = new TextGraphicsWriter(textGraphics);
        textGraphicsWriter.setForegroundColor(TextColor.ANSI.RED);
        textGraphicsWriter.setWrapBehaviour(WrapBehaviour.WORD);
        for (String errorMessage : errorMessages) {
            textGraphicsWriter.putString(" - " + errorMessage + "\n");
        }
    }

    private void renderWidget(TerminalSize size) {
        WidgetErrorRecorder errorRecorder = new BasicWidgetErrorRecorder();
        widgetToRun.safeRender(0, 0, size.getColumns(), size.getRows(), screen, errorRecorder);
        List<String> errors = errorRecorder.getAllErrors();
        if (!errors.isEmpty()) {
            screen.clear();
            writeErrorsOnScreen(errors, screen);
        }
        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startWidget() {
        boolean isInitialRender = true;
        while (!stop) {
            // TODO: when asking for state in each loop,
            // maybe make sure widget controller can decide if they always want to then emit state,
            // for an animation, that always wants to render on next one
            TerminalSize size;
            if (isInitialRender) {
                isInitialRender = false;
                size = screen.getTerminalSize();
            } else {
                size = screen.doResizeIfNecessary();
            }
            oldChildWidgetControllers = childWidgetControllers;
            childWidgetControllers = new HashMap<>(oldChildWidgetControllers.size());
            rebuilding = true;
            if (size != null) {
                screen.clear();
                renderWidget(size);
            } else {
                WidgetErrorRecorder errorRecorder = new BasicWidgetErrorRecorder();
                boolean shouldRerender = widgetToRun.shouldParentRerender(errorRecorder);
                try {
                    List<String> errors = errorRecorder.getAllErrors();
                    if (!errors.isEmpty()) {
                        screen.clear();
                        writeErrorsOnScreen(errors, screen);
                        screen.refresh();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (shouldRerender) {
                    renderWidget(screen.getTerminalSize());
                }
            }
            rebuilding = false;
        }
    }

    public void stopRunning() {
        stop = true;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }
// TODO: still not always adding state, should be added in shouldParentRerender and normal rendering
    @Override
    public <_StateType, _WidgetControllerType extends WidgetStateController<_StateType>> void registerWidgetController(
            String stateId, _WidgetControllerType stateController
    ) {
        assert rebuilding : "can only insert, if in rebuilding";
        executorService.execute(stateController::safeInit);
        childWidgetControllers.put(stateId, stateController);
    }

    @Override
    public <_StateType, _WidgetControllerType extends WidgetStateController<_StateType>> boolean widgetControllerHasEverBeenRegistered(
            String stateId
    ) {
        assert rebuilding : "can only insert, if in rebuilding";
        if (oldChildWidgetControllers == null) {
            return false;
        }
        _WidgetControllerType widgetController = (_WidgetControllerType) oldChildWidgetControllers.get(stateId);
        return widgetController != null;
    }

    @Override
    public <_StateType, _WidgetControllerType extends WidgetStateController<_StateType>> _WidgetControllerType reinsertWidgetControllerInTree(
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
