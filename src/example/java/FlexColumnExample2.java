import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import layout.*;
import rendering.basic.BasicWidgetRenderer;

public class FlexColumnExample2 {
    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.WHITE,
                new Padding(3, new ExpandingColoredBox(TextColor.ANSI.BLACK, new Padding(3, new ColumnBuilder()
                        .addChild(new BasicFlexWidget(1,
                                MaxSizeBox.width(40, new ExpandingColoredBox(TextColor.ANSI.RED))
                        ))
                        .addChild(new Padding(1,
                                0,
                                MaxSizeBox.width(10,
                                        MinSizeBox.height(1, new ExpandingColoredBox(TextColor.ANSI.WHITE))
                                )
                        ))
                        .addChild(new BasicFlexWidget(2,
                                MaxSizeBox.width(40, new ExpandingColoredBox(TextColor.ANSI.BLUE))
                        ))
                        .setCrossAxisAlignment(BasicAlignment.center)
                        .build())))
        );
        BasicWidgetRenderer.renderWidget(widget);
    }
}
