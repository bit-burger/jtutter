package content;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.screen.Screen;
import base_widgets.OneChildWidget;
import base_widgets.Widget;

public class ShrinkingColoredBox extends OneChildWidget {
    final TextColor color;

    public ShrinkingColoredBox(TextColor color, Widget widget) {
        super(widget);
        this.color = color;
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen) {
        screen.newTextGraphics().drawImage(new TerminalPosition(x, y),
                                           new BasicTextImage(new TerminalSize(width, height),
                                                              new TextCharacter(' ', null, color)
                                           )
        );
        super.rawRender(x, y, width, height, screen);
    }
}
