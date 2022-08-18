package base_widgets;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextGraphicsWriter;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.WrapBehaviour;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import rendering.basic.BasicWidgetErrorRecorder;

import java.io.IOException;
import java.util.List;

public class StatefulRenderer implements WidgetContext {
    private final StatefulWidget<?, ?> widgetToRun;
    private Screen screen;

    public StatefulRenderer(StatefulWidget<?, ?> widgetToRun) {
        this.widgetToRun = widgetToRun;
    }

    public void run() throws IOException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
        widgetToRun.insertIntoWidgetTree(this);
        startWidget();
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

    private void startWidget() {
        boolean isInitialRender = true;
        while (true) {
            TerminalSize size;
            if (isInitialRender) {
                isInitialRender = false;
                size = screen.getTerminalSize();
            } else {
                size = screen.doResizeIfNecessary();
            }
            if (size != null) {
                screen.clear();
                WidgetErrorRecorder errorRecorder = new BasicWidgetErrorRecorder();

                int availableWidth = size.getColumns();
                int availableHeight = size.getRows();

                int absoluteMinWidth = widgetToRun.getAbsoluteMinWidth();
                int absoluteMinHeight = widgetToRun.getAbsoluteMinHeight();

                int minWidth = widgetToRun.getMinWidth(Math.max(availableHeight, absoluteMinHeight));
                if (minWidth > availableWidth) {
                    errorRecorder.terminalToSlim(minWidth - availableWidth);
                }

                int minHeight = widgetToRun.getMinHeight(Math.max(availableWidth, absoluteMinWidth));
                if (minHeight > availableHeight) {
                    errorRecorder.terminalToLow(minHeight - availableHeight);
                }

                if (errorRecorder.getAllErrors().isEmpty()) {
                    widgetToRun.safeRender(0, 0, size.getColumns(), size.getRows(), screen, errorRecorder);
                }
                List<String> errors = errorRecorder.getAllErrors();
                if (!errors.isEmpty()) {
                    screen.clear();
                    writeErrorsOnScreen(errors, screen);
                }
                TextCharacter cursorCharacter = screen.getBackCharacter(0, 0);
                screen.setCharacter(
                        0,
                        0,
                        cursorCharacter
                                .withBackgroundColor(cursorCharacter.getForegroundColor())
                                .withForegroundColor(cursorCharacter.getBackgroundColor())
                );
                try {
                    screen.refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void stop() {

    }

    @Override
    public <StateType, WidgetControllerType extends WidgetController<StateType>> void registerWidgetController(
            String stateId, WidgetControllerType state
    ) {
    }

    @Override
    public <StateType, WidgetControllerType extends WidgetController<StateType>> boolean widgetControllerHasEverBeenRegistered(
            String stateId
    ) {
        return true;
    }

    @Override
    public <StateType, WidgetControllerType extends WidgetController<StateType>> WidgetControllerType reinsertWidgetControllerInTree(
            String stateId
    ) {
        throw new IllegalStateException();
    }

    @Override
    public Screen getScreen() {
        return screen;
    }
}
