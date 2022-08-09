import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import widgets.BasicWidgetRenderer;
import widgets.Widget;

import java.io.IOException;

public class BaseExample {
    final protected Widget widget;

    public BaseExample(Widget widget) {
        this.widget = widget;
    }

    public void start() {
        try {
            Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            BasicWidgetRenderer renderer = new BasicWidgetRenderer(screen, widget);
            renderer.startRenderingLoop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
