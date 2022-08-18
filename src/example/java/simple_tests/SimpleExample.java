package simple_tests;

import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import layout.Align;
import layout.Alignment;
import layout.MaxSizeBox;
import layout.Padding;
import rendering.basic.BasicWidgetRenderer;

public class SimpleExample {
    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.BLACK,
                new Padding(5,
                        new ExpandingColoredBox(TextColor.ANSI.RED,
                                new Padding(2,
                                        new ExpandingColoredBox(TextColor.ANSI.BLUE,
                                                new Align(
                                                        Alignment.topRight,
                                                        new MaxSizeBox(3,
                                                                3,
                                                                new ExpandingColoredBox(TextColor.ANSI.YELLOW)
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        BasicWidgetRenderer.renderWidget(widget);
    }
}
