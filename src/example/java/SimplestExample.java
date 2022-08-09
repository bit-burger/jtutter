import com.googlecode.lanterna.TextColor;
import widgets.*;

import java.io.IOException;

public class SimplestExample {
    public static void main(String[] args) {
        Widget widget = new MaxSizeBox(10, 5, new MinSizeBox(10, 5, new ExpandingColoredBox(TextColor.ANSI.RED)));
        BasicWidgetRenderer.renderWidget(widget);
    }
}
