import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.w3c.dom.Text;
import widgets.BasicWidgetRenderer;
import widgets.ColoredBox;
import widgets.Padding;
import widgets.Widget;

import java.io.IOException;

public class SimpleExample {
    public static void main(String[] args) throws IOException {
        Screen screen = new DefaultTerminalFactory().createScreen();
        Widget widget = new ColoredBox(TextColor.ANSI.BLACK,
                                       new Padding(5,
                                                   new ColoredBox(TextColor.ANSI.RED,
                                                                  new Padding(2, new ColoredBox(TextColor.ANSI.BLUE))
                                                   )
                                       )
        );
        screen.startScreen();
        BasicWidgetRenderer renderer = new BasicWidgetRenderer(screen, widget);
        renderer.startRenderingLoop();
    }
}
