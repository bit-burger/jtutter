import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import content.Text;
import content.TextStyle;
import layout.*;
import rendering.basic.BasicWidgetRenderer;

public class TextExample {
    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.BLACK, new Align(Alignment.topRight, new Text(
                """
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
                        Dein Kleid will mich was lehren!""",
                new TextStyle().setTextAlign(BasicAlignment.center).setBackgroundColor(TextColor.ANSI.RED)
        )));
        BasicWidgetRenderer.renderWidget(widget);
    }
}
