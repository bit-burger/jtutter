package stateful_tests;

import base_widgets.StatefulRenderer;
import base_widgets.StatefulWidget;
import base_widgets.Widget;
import base_widgets.WidgetController;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import content.Text;
import content.TextStyle;
import content.TextWrappingBehavior;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleAnimation extends StatefulWidget<String, SimpleAnimation.TextAnimationController> {
    public static void main(String[] args) throws IOException {
        new StatefulRenderer(new SimpleAnimation()).run();
    }

    public static class TextAnimationController extends WidgetController<String> {
        @Override
        public void init() {
            newState("");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    newState(state() + "0");
                }
            }, 0, 5000);
        }
    }

    @Override
    protected TextAnimationController createStateController() {
        return new TextAnimationController();
    }

    @Override
    protected Widget build(TextAnimationController stateController, String state) {
        System.out.println("state: '" + state + "'");
        return new ExpandingColoredBox(
                TextColor.ANSI.BLUE,
                new Text(state, new TextStyle().setWrappingBehavior(TextWrappingBehavior.characters))
        );
    }
}
