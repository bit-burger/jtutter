package simple_tests;

import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import content.ShrinkingColoredBox;
import content.Text;
import content.TextStyle;
import layout.*;
import rendering.basic.BasicWidgetRenderer;

public class ColumnTextExample {
    static final Widget text = new Text("""
            O Tannenbaum, o Tannenbaum,
            Wie treu sind deine Blätter!

            Du kannst mir sehr gefallen!

            Gibt Mut und Kraft zu jeder Zeit!""",
            new TextStyle().setBackgroundColor(TextColor.ANSI.RED).shrinkWidth().setTextAlign(BasicAlignment.center)
    );

    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.RED,
                new Center(new ShrinkingColoredBox(TextColor.ANSI.WHITE,
                        new ColumnBuilder().addSpace().addChild(new ShrinkingColoredBox(TextColor.ANSI.YELLOW,
                                new Padding(1, text)
                        )).repeatAll(1).repeatFirst(1).useMinWidth().build()
                ))
        );
        BasicWidgetRenderer.renderWidget(widget);
    }
}
