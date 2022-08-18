package simple_tests;

import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import content.ShrinkingColoredBox;
import content.Text;
import content.TextStyle;
import layout.*;
import rendering.basic.BasicWidgetRenderer;

public class TextExample {
    static final String text = """
            O Tannenbaum, o Tannenbaum,
            Wie treu sind deine Blätter!
            Du grünst nicht nur zur Sommerzeit,
            Nein, auch im Winter, wenn es schneit.
            O Tannenbaum, o Tannenbaum,
            Wie treu sind deine Blätter!

            O Tannenbaum, o Tannenbaum,
            Du kannst mir sehr gefallen!
            Wie oft hat nicht zur Weihnachtszeit
            Ein Baum von dir mich hoch erfreut!
            O Tannenbaum, o Tannenbaum,
            Du kannst mir sehr gefallen!

            O Tannenbaum, o Tannenbaum,
            Dein Kleid will mich was lehren:
            Die Hoffnung und Beständigkeit
            Gibt Mut und Kraft zu jeder Zeit!
            O Tannenbaum, o Tannenbaum,
            Dein Kleid will mich was lehren!""";

    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.GREEN,
                new Center(new Padding(2,
                        new ShrinkingColoredBox(TextColor.ANSI.RED,
                                new Padding(2,
                                        new Text(
                                                text,
                                                new TextStyle().setTextAlign(BasicAlignment.center).shrinkWidth()
                                        )
                                )
                        )
                ))
        );
        BasicWidgetRenderer.renderWidget(widget);
    }
}
