package rendering.basic;

import base_widgets.WidgetErrorRecorder;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextGraphicsWriter;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.WrapBehaviour;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import base_widgets.Widget;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;

import java.io.IOException;
import java.util.List;

public class BasicWidgetRenderer {
    final private Screen screen;
    final private Widget widgetToRender;
    private boolean shouldContinueRenderLoop;

    public BasicWidgetRenderer(Screen screen, Widget widgetToRender) {
        this.screen = screen;
        this.widgetToRender = widgetToRender;
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

    public void startRenderingLoop() {
        shouldContinueRenderLoop = true;
        new Thread(() -> {
            boolean isInitialRender = true;
            while (shouldContinueRenderLoop) {
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

                    int absoluteMinWidth = widgetToRender.getAbsoluteMinWidth();
                    int absoluteMinHeight = widgetToRender.getAbsoluteMinHeight();

                    int minWidth = widgetToRender.getMinWidth(Math.max(availableHeight, absoluteMinHeight));
                    if (minWidth > availableWidth) {
                        errorRecorder.terminalToSlim(minWidth - availableWidth);
                    }

                    int minHeight = widgetToRender.getMinHeight(Math.max(availableWidth, absoluteMinWidth));
                    if (minHeight > availableHeight) {
                        errorRecorder.terminalToLow(minHeight - availableHeight);
                    }

                    if (errorRecorder.getAllErrors().isEmpty()) {
                        widgetToRender.safeRender(0, 0, size.getColumns(), size.getRows(), screen, errorRecorder);
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
        }).start();
    }

    public void stopRenderingLoop() {
        shouldContinueRenderLoop = false;
    }

    public static BasicWidgetRenderer renderWidget(Widget widget) {
        try {
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            BasicWidgetRenderer renderer = new BasicWidgetRenderer(screen, widget);
            renderer.startRenderingLoop();
            return renderer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
