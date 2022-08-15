package base_widgets;

import java.util.List;

public abstract class WidgetErrorRecorder {
    public abstract void errorOccurred(String errorMessage);

    public abstract List<String> getAllErrors();

    public void terminalToLow(int toLittle) {
        errorOccurred("terminal not high enough, increase height by " + toLittle + " to remove this error");
    }

    public void terminalToLow() {
        errorOccurred("terminal not high enough");
    }

    public void terminalToSlim() {
        errorOccurred("terminal not wide enough");
    }

    public void terminalToSlim(int toLittle) {
        errorOccurred("terminal not wide enough, increase width by " + toLittle + " to remove this error");
    }
}
