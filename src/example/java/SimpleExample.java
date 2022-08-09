import com.googlecode.lanterna.TextColor;
import widgets.*;

import java.io.IOException;

public class SimpleExample {
    public static void main(String[] args) throws IOException {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.BLACK,
                                                new Padding(
                                                        5,
                                                        new ExpandingColoredBox(TextColor.ANSI.RED,
                                                                                new Padding(2,
                                                                                            new ExpandingColoredBox(
                                                                                                    TextColor.ANSI.BLUE,
                                                                                                    new MaxSizeBox(3,
                                                                                                                   3,
                                                                                                                   new ExpandingColoredBox(
                                                                                                                           TextColor.ANSI.YELLOW)
                                                                                                    )
                                                                                            )
                                                                                )
                                                        )
                                                )
        );
        BasicWidgetRenderer.renderWidget(widget);
    }
}
