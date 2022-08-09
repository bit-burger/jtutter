import com.googlecode.lanterna.TextColor;
import widgets.ExpandingColoredBox;
import widgets.MaxSizeBox;
import widgets.Padding;
import widgets.Widget;

import java.io.IOException;

public class SimplestExample {
    public static void main(String[] args) {
        Widget widget = new ExpandingColoredBox(TextColor.ANSI.RED);
        new BaseExample(widget).start();
    }
}
