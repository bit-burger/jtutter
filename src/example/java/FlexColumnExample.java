import com.googlecode.lanterna.TextColor;
import widgets.*;

import java.io.IOException;

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
