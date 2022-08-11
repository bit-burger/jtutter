package widgets;

import java.util.ArrayList;
import java.util.List;

public class ColumnBuilder {
    private boolean autoFlex = false;
    private boolean useFullWidth = true;
    private boolean useFullHeight = true;
    private BasicAlignment mainAxisAlignment = BasicAlignment.start;
    private BasicAlignment crossAxisAlignment = BasicAlignment.start;
    private final List<Widget> children = new ArrayList<>();

    public ColumnBuilder withAutoFlex() {
        autoFlex = true;
        return this;
    }

    public ColumnBuilder useMinWidth() {
        useFullWidth = false;
        return this;
    }

    public ColumnBuilder useMinHeight() {
        useFullHeight = false;
        return this;
    }

    public ColumnBuilder setMainAxisAlignment(BasicAlignment mainAxisAlignment) {
        this.mainAxisAlignment = mainAxisAlignment;
        return this;
    }

    public ColumnBuilder setCrossAxisAlignment(BasicAlignment crossAxisAlignment) {
        this.crossAxisAlignment = crossAxisAlignment;
        return this;
    }

    public ColumnBuilder addChild(Widget child) {
        children.add(child);
        return this;
    }

    public ColumnBuilder addChildConditionally(boolean condition, Widget child) {
        if (condition) {
            children.add(child);
        }
        return this;
    }

    public ColumnBuilder repeat(int howOften) {
        for (int i = 0; i < howOften; i++) {
            children.add(children.get(children.size() - 1));
        }
        return this;
    }

    public ColumnBuilder repeatAll(int howOften) {
        List<Widget> repeated = new ArrayList<>(children.subList(0, children.size()));
        for (int i = 0; i < howOften; i++) {
            children.addAll(repeated);
        }
        return this;
    }

    public ColumnBuilder addSpace() {
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
