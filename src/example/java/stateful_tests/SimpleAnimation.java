package stateful_tests;

import base_widgets.StatefulWidgetRenderer;
import base_widgets.StatefulWidget;
import base_widgets.Widget;
import base_widgets.WidgetStateController;
import com.googlecode.lanterna.TextColor;
import content.ExpandingColoredBox;
import content.Text;
import content.TextStyle;
import content.TextWrappingBehavior;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleAnimation extends StatefulWidget<String, SimpleAnimation.TextAnimationController> {
    public static void main(String[] args) throws IOException {
        new StatefulWidgetRenderer(new SimpleAnimation()).start();
    }

    public static class TextAnimationController extends WidgetStateController<String> {
        TextAnimationController() {
            super("");
        }

        static final String message = "this shit hard, this shit hard, this shit hard, this shit hard, this shit hard";
        boolean up = true;

        private void tick() {
            if (up) {
                emit(state() + message.charAt(state().length()));
                if (state().length() == message.length()) {
                    up = false;
                }
            } else {
                emit(state().substring(0, state().length() - 1));
                if (state().length() == 0) {
                    up = true;
                }
            }
        }

        final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

        @Override
        public void init() {
            timer.scheduleWithFixedDelay(this::tick, 10, 10, TimeUnit.MILLISECONDS);
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
