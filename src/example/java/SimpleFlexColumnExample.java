import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import layout.ColumnBuilder;
import layout.Padding;
import rendering.BasicWidgetRenderer;

import java.io.IOException;

public class SimpleFlexColumnExample {
    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.BLACK,
                new Padding(5, new ExpandingColoredBox(TextColor.ANSI.RED,
                        new ColumnBuilder()
                                .addChild(new ExpandingColoredBox(TextColor.ANSI.CYAN))
                                .addChild(new ExpandingColoredBox(TextColor.ANSI.RED))
                                .repeatFirst(1)
                                .withAutoFlex()
                                .build()
                ))
        );
        BasicWidgetRenderer.renderWidget(widget);
    }
}
