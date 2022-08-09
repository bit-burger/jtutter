package widgets;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.screen.Screen;

public class ExpandingColoredBox extends OptionalChildWidget {
    final TextColor color;

    public ExpandingColoredBox(TextColor color) {
        this.color = color;
    }

    public ExpandingColoredBox(TextColor color, Widget widget) {
        super(widget);
        this.color = color;
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen) {
        screen.newTextGraphics().drawImage(new TerminalPosition(x, y),
                                           new BasicTextImage(new TerminalSize(width, height),
                                                              new TextCharacter(' ', color, color)
                                           )
        );
        if(child != null) {
            child.safeRender(x, y, width, height, screen);
        }
    }

    @Override
    public int getMaxWidth(int maxAvailableWidth, int maxAvailableHeight) {
        return maxAvailableWidth;
    }

    @Override
    public int getMaxHeight(int maxAvailableWidth, int maxAvailableHeight) {
        return maxAvailableHeight;
    }
}
