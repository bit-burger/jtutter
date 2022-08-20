package stateful_tests;

import base_widgets.StatefulWidgetRenderer;
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
        new StatefulWidgetRenderer(new SimpleAnimation()).run();
    }

    public static class TextAnimationController extends WidgetController<String> {
        static final String message = "this shit hard, this shit hard, this shit hard, this shit hard, this shit hard";
        boolean up = true;

        private void tick() {
            if (up) {
                newState(state() + message.charAt(state().length()));
                if (state().length() == message.length()) {
                    up = false;
                }
            } else {
                newState(state().substring(0, state().length() - 1));
                if (state().length() == 0) {
                    up = true;
                }
            }
        }

        @Override
        public void init() {
            newState("");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    tick();
                }
            }, 0, 11);
        }
    }

    public SimpleAnimation() {
        super();
    }

    public SimpleAnimation(String id) {
        super(id);
    }

    @Override
    protected TextAnimationController createStateController() {
        return new TextAnimationController();
    }

    @Override
    protected Widget build(String state) {
        return new ExpandingColoredBox(
                TextColor.ANSI.BLUE,
                new Text(state, new TextStyle().setWrappingBehavior(TextWrappingBehavior.characters))
        );
    }
}
