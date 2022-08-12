import com.googlecode.lanterna.TextColor;
import widgets.*;

import java.io.IOException;

public class SimpleFlexColumnExample {
    public static void main(String[] args) throws IOException {
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
