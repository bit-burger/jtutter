package simple_tests;

import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import content.ShrinkingColoredBox;
import layout.*;
import rendering.basic.BasicWidgetRenderer;

public class FlexColumnExample {
    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(
                TextColor.ANSI.BLACK,
                new Padding(
                        5,
                        new ExpandingColoredBox(TextColor.ANSI.CYAN_BRIGHT, new Padding(1, new ColumnBuilder()
                                .addChild(new BasicFlexWidget(2,
                                        new ExpandingColoredBox(TextColor.ANSI.YELLOW,
                                                new Padding(
                                                        1,
                                                        new Center(new ShrinkingColoredBox(TextColor.ANSI.RED,
                                                                new Padding(1,
                                                                        MaxSizeBox.square(3,
                                                                                MinSizeBox.square(3,
                                                                                        new ExpandingColoredBox(
                                                                                                TextColor.ANSI.YELLOW_BRIGHT)
                                                                                )
                                                                        )
                                                                )
                                                        ))
                                                )
                                        )
                                ))
                                .addSpace()
                                .addChild(MaxSizeBox.width(10, new ExpandingColoredBox(TextColor.ANSI.CYAN)))
                                .setCrossAxisAlignment(BasicAlignment.center)
                                .withAutoFlex()
                                .build()))
                )
        );
        BasicWidgetRenderer.renderWidget(widget);
    }
}
