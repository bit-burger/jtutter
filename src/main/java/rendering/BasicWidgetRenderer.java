package rendering;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextGraphicsWriter;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.WrapBehaviour;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import base_widgets.Widget;

import java.io.IOException;

public class BasicWidgetRenderer {
    final private Screen screen;
    final private Widget widgetToRender;
    private boolean shouldContinueRenderLoop;

    public BasicWidgetRenderer(Screen screen, Widget widgetToRender) {
        this.screen = screen;
        this.widgetToRender = widgetToRender;
    }

    private void writeErrorOnScreen(String errorMessage, Screen screen) {
        TextGraphics textGraphics = screen.newTextGraphics();
        TextGraphicsWriter textGraphicsWriter = new TextGraphicsWriter(textGraphics);
        textGraphicsWriter.setForegroundColor(TextColor.ANSI.RED);
        textGraphicsWriter.setWrapBehaviour(WrapBehaviour.WORD);
        textGraphicsWriter.putString(errorMessage);
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
                    if (widgetToRender.getMinWidth() > size.getColumns()) {
                        writeErrorOnScreen("Terminal not wide enough, increase width to remove this error", screen);
                    } else if (widgetToRender.getMinHeight() > size.getRows()) {
                        writeErrorOnScreen("Terminal not high enough, increase height to remove this error", screen);
                    } else {
                        widgetToRender.safeRender(0, 0, size.getColumns(), size.getRows(), screen);
                    }
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
