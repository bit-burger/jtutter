package simple_tests;

import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import layout.*;
import rendering.basic.BasicWidgetRenderer;

public class ColumnExample {
    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.BLACK,
                new Padding(5, new ExpandingColoredBox(TextColor.ANSI.RED,
                        new ColumnBuilder()
                                .addChild(MaxSizeBox.width(10, MinSizeBox.square(1, new ExpandingColoredBox(TextColor.ANSI.CYAN))))
                                .addChild(MaxSizeBox.width(5, MinSizeBox.square(1, new ExpandingColoredBox(TextColor.ANSI.YELLOW))))
                                .repeatAll(4)
                                .repeatFirst(1)
                                .setMainAxisAlignment(BasicAlignment.center)
                                .setCrossAxisAlignment(BasicAlignment.center)
                                .build()
                ))
        );
        BasicWidgetRenderer.renderWidget(widget);
    }
}
