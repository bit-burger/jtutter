package layout;

import base_widgets.Widget;

import java.util.ArrayList;
import java.util.List;

public class BaseListBuilder {
    protected boolean autoFlex = false;
    protected boolean useFullWidth = true;
    protected boolean useFullHeight = true;
    protected BasicAlignment mainAxisAlignment = BasicAlignment.start;
    protected BasicAlignment crossAxisAlignment = BasicAlignment.start;
    protected final List<Widget> children = new ArrayList<>();

    public BaseListBuilder withAutoFlex() {
        autoFlex = true;
        return this;
    }

    public BaseListBuilder useMinWidth() {
        useFullWidth = false;
        return this;
    }

    public BaseListBuilder useMinHeight() {
        useFullHeight = false;
        return this;
    }

    public BaseListBuilder setMainAxisAlignment(BasicAlignment mainAxisAlignment) {
        this.mainAxisAlignment = mainAxisAlignment;
        return this;
    }

    public BaseListBuilder setCrossAxisAlignment(BasicAlignment crossAxisAlignment) {
        this.crossAxisAlignment = crossAxisAlignment;
        return this;
    }

    public BaseListBuilder addChild(Widget child) {
        children.add(child);
        return this;
    }

    public BaseListBuilder addChildConditionally(boolean condition, Widget child) {
        if (condition) {
            children.add(child);
        }
        return this;
    }

    public BaseListBuilder repeatLast(int howOften) {
        for (int i = 0; i < howOften; i++) {
            children.add(children.get(children.size() - 1));
        }
        return this;
    }

    public BaseListBuilder repeatFirst(int howOften) {
        for (int i = 0; i < howOften; i++) {
            children.add(children.get(0));
        }
        return this;
    }

    public BaseListBuilder repeatAll(int howOften) {
        List<Widget> repeated = new ArrayList<>(children.subList(0, children.size()));
        for (int i = 0; i < howOften; i++) {
            children.addAll(repeated);
        }
        return this;
    }

    public BaseListBuilder addSpace() {
        addChild(BasicFlexWidget.spacer());
        return this;
    }

    public Column build() {
        return new Column(
                autoFlex,
                useFullWidth,
                useFullHeight,
                mainAxisAlignment,
                crossAxisAlignment,
                children.toArray(new Widget[0])
        );
    }
}
