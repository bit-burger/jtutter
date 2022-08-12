package rendering.basic;

import base_widgets.WidgetErrorRecorder;

import java.util.ArrayList;
import java.util.List;

public class BasicWidgetErrorRecorder extends WidgetErrorRecorder {
    private final List<String> errors = new ArrayList<>();

    @Override
    public void errorOccurred(String errorMessage) {
        errors.add(errorMessage);
    }

    @Override
    public List<String> getAllErrors() {
        return errors;
    }
}
