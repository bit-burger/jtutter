package layout;

import base_widgets.OptionalChildWidget;
import base_widgets.Widget;

public class BasicFlexWidget extends OptionalChildWidget implements FlexWidget {
    static final BasicFlexWidget spacerBasicFlexWidget = new BasicFlexWidget(1);

    private final double flex;

    private BasicFlexWidget(int flex) {
        this.flex = flex;
    }

    private BasicFlexWidget(double flex) {
        this.flex = flex;
    }

    public BasicFlexWidget(int flex, Widget child) {
        super(child);
        this.flex = flex;
    }

    public BasicFlexWidget(double flex, Widget child) {
        super(child);
        this.flex = flex;
    }

    static BasicFlexWidget spacer(double flex) {
        return new BasicFlexWidget(flex);
    }

    static BasicFlexWidget spacer(int flex) {
        return new BasicFlexWidget(flex);
    }


    static BasicFlexWidget spacer() {
        return spacerBasicFlexWidget;
    }

    @Override
    public double getFlex() {
        return flex;
    }
}
