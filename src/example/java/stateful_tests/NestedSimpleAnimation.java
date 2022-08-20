package stateful_tests;

import base_widgets.StatefulWidget;
import base_widgets.StatefulWidgetRenderer;
import base_widgets.Widget;
import base_widgets.WidgetController;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import layout.ColumnBuilder;
import layout.MinSizeBox;
import layout.Padding;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class NestedSimpleAnimation extends StatefulWidget<Integer, NestedSimpleAnimation.NumberAnimationController> {
    public static void main(String[] args) throws IOException {
        new StatefulWidgetRenderer(new NestedSimpleAnimation()).run();
    }

    public static class NumberAnimationController extends WidgetController<Integer> {
        boolean up = true;

        private void tick() {
            if (up) {
                newState(state() + 1);
                if (state() == 5) {
                    up = false;
                }
            } else {
                newState(state() - 1);
                if (state() == 0) {
                    up = true;
                }
            }
        }

        @Override
        public void init() {
            newState(0);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    tick();
                }
            }, 100, 100);
        }
    }

    @Override
    protected NumberAnimationController createStateController() {
        return new NumberAnimationController();
    }

    @Override
    protected Widget build(Integer count) {
        return new ExpandingColoredBox(TextColor.ANSI.BLACK, new Padding(1, new ColumnBuilder()
                .addChildConditionally(
                        count > 0,
                        MinSizeBox.height(1, new ExpandingColoredBox(TextColor.ANSI.RED))
                )
                .addChildConditionally(count > 0, MinSizeBox.height(1, new ExpandingColoredBox(TextColor.ANSI.BLUE)))
                .repeatAll(count - 1)
                .addChild(new SimpleAnimation("simple-animation"))
                .build()));
    }
}
