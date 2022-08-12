import base_widgets.Widget;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import layout.MaxSizeBox;
import layout.MinSizeBox;
import rendering.basic.BasicWidgetRenderer;

public class SimplestExample {
    public static void main(String[] args) {
        Widget widget = new MaxSizeBox(10, 5, new MinSizeBox(10, 5, new ExpandingColoredBox(TextColor.ANSI.RED)));
        BasicWidgetRenderer.renderWidget(widget);
    }
}
