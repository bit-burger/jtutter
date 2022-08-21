package layout;

import base_widgets.WidgetContext;
import base_widgets.WidgetErrorRecorder;
import com.googlecode.lanterna.screen.Screen;
import base_widgets.Widget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class Column extends Widget {
    /**
     * Is true if at least one element is flex (or auto flex is turned on)
     */
    final private boolean usesFlex;
    /**
     * Is automatically true if usesFlex is true
     */
    final private boolean useFullHeight;
    final private boolean useFullWidth;
    /**
     * All flex widgets flex value added up
     */
    final private double allWidgetsFlex;
    final private BasicAlignment mainAxisAlignment;
    final private BasicAlignment crossAxisAlignment;
    final private Widget[] children;
    final private double[] flexValues;

    public Column(
            boolean autoFlex,
            boolean useFullWidth,
            boolean useFullHeight,
            BasicAlignment mainAxisAlignment,
            BasicAlignment crossAxisAlignment,
            Widget[] children
    ) {
        assert children.length > 0;

        this.useFullWidth = useFullWidth;
        this.useFullHeight = useFullHeight;
        this.mainAxisAlignment = mainAxisAlignment;
        this.crossAxisAlignment = crossAxisAlignment;
        this.children = children;

        this.flexValues = new double[children.length];

        boolean usesFlex = false;
        double allWidgetsFlex = 0;
        for (int i = 0; i < children.length; i++) {
            Widget child = children[i];
            double flex = 0;
            if (child instanceof FlexWidget) {
                flex = ((FlexWidget) child).getFlex();
                assert flex > 0;
                usesFlex = true;
            } else {
                if (autoFlex) {
                    flex = 1;
                    usesFlex = true;
                } else {
                    assert getChildAbsoluteMinHeight(child) > 0 : "absolute min height of child " + child + " cannot " +
                            "be 0, if no flex is used";
                }
            }
            allWidgetsFlex += flex;
            flexValues[i] = flex;
        }
        this.usesFlex = usesFlex;
        this.allWidgetsFlex = allWidgetsFlex;
    }

    protected int getChildAbsoluteMinHeight(Widget child) {
        return child.getAbsoluteMinHeight();
    }

    protected int getChildMinHeight(Widget child, int availableWidth) {
        return child.getMinHeight(availableWidth);
    }

    protected int getChildMinWidth(Widget child, int availableHeight) {
        return child.getMinWidth(availableHeight);
    }

    protected int getChildMaxWidth(Widget child, int availableWidth, int availableHeight) {
        return child.getMaxWidth(availableWidth, availableHeight);
    }

    protected int getChildMaxHeight(Widget child, int availableWidth, int availableHeight) {
        return child.getMaxHeight(availableWidth, availableHeight);
    }

    /**
     * Column is as high as getAllWidgetsMinHeight, except if usesFlex is true, then column is as high as possible.
     */
    private int getAllWidgetsMinHeight(int availableWidth) {
        int allWidgetsMinHeight = 0;
        for (Widget child : children) {
            allWidgetsMinHeight += getChildMinHeight(child, availableWidth);
        }
        return allWidgetsMinHeight;
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        if (usesFlex) {
            flexRender(x, y, width, height, screen, errorRecorder);
        } else {
            nonFlexRender(x, y, width, height, screen, errorRecorder);
        }
    }

    protected void renderChild(
            Widget child,
            int x,
            int y,
            int availableWidth,
            int childHeight,
            Screen screen,
            WidgetErrorRecorder errorRecorder
    ) {
        if (child.hasComplexLayout()) {
            int childMinWidth = getChildMinWidth(child, childHeight);
            if (childMinWidth > availableWidth) {
                errorRecorder.terminalToSlim(childMinWidth - availableWidth);
                return;
            }
        }
        int childMaxWidth = getChildMaxWidth(child, availableWidth, childHeight);
        if (childMaxWidth >= availableWidth) {
            child.rawRender(x, y, availableWidth, childHeight, screen, errorRecorder);
        } else {
            switch (crossAxisAlignment) {
                case center -> x += (int) Math.floor((availableWidth - childMaxWidth) / 2.0);
                case end -> x += availableWidth - childMaxWidth;
            }
            child.rawRender(x, y, childMaxWidth, childHeight, screen, errorRecorder);
        }
    }

    protected void flexRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        int remainingHeightForFlex = height - getAllWidgetsMinHeight(width);
        int numberOfAllowedRoundUps = remainingHeightForFlex;
        final List<Double> roundUps = new ArrayList<>();
        for (double flexValue : flexValues) {
            double childHeight = remainingHeightForFlex * (flexValue / allWidgetsFlex);
            double childHeightCeil = Math.ceil(childHeight);
            double childHeightFloor = Math.floor(childHeight);
            if (childHeightCeil > childHeight) {
                double roundUp = childHeight - childHeightFloor;
                roundUps.add(roundUp);
            }
            numberOfAllowedRoundUps -= childHeightFloor;
        }
        roundUps.sort(Double::compare);
        roundUps.sort(Comparator.reverseOrder());
        double lastRoundUp = 0;
        if (numberOfAllowedRoundUps > 0) {
            lastRoundUp = roundUps.get(numberOfAllowedRoundUps - 1);
        }

        for (int i = 0; i < children.length; i++) {
            Widget child = children[i];
            int childUsedHeight;
            int childRealHeight;
            if (flexValues[i] == 0) {
                childUsedHeight = getChildMinHeight(child, width);
                childRealHeight = childUsedHeight;
            } else {
                double rawChildHeight = remainingHeightForFlex * (flexValues[i] / allWidgetsFlex);
                double roundUp = rawChildHeight - Math.floor(rawChildHeight);
                if (numberOfAllowedRoundUps > 0 && roundUp >= lastRoundUp) {
                    numberOfAllowedRoundUps--;
                    childUsedHeight = (int) Math.ceil(rawChildHeight);
                } else {
                    childUsedHeight = (int) Math.floor(rawChildHeight);
                }
                childRealHeight = getChildMaxHeight(child, width, childUsedHeight);
            }
            renderChild(child, x, y, width, childRealHeight, screen, errorRecorder);
            y += childUsedHeight;
        }
    }

    protected void nonFlexRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        int allWidgetsMinHeight = getAllWidgetsMinHeight(width);
        switch (mainAxisAlignment) {
            case center -> y += Math.floor((height - allWidgetsMinHeight) / 2.0);
            case end -> y += height - allWidgetsMinHeight;
        }
        for (Widget child : children) {
            int childHeight = getChildMinHeight(child, width);
            renderChild(child, x, y, width, childHeight, screen, errorRecorder);
            y += childHeight;
        }
    }

    @Override
    public int getMaxWidth(int maxAvailableWidth, int maxAvailableHeight) {
        if (useFullWidth) {
            return maxAvailableWidth;
        }
        int maxWidth = 0;
        for (Widget child : children) {
            int childMaxWidth = getChildMaxWidth(child, maxAvailableWidth, maxAvailableHeight);
            if (maxWidth < childMaxWidth) {
                maxWidth = childMaxWidth;
            }
        }
        return maxWidth;
    }

    @Override
    public int getMinWidth(int availableHeight) {
        int minWidth = 0;
        for (Widget child : children) {
            int childMinWidth = getChildAbsoluteMinHeight(child); // use absolute min width, and give error in
            // rendering,
            // if not enough width
            if (minWidth < childMinWidth) {
                minWidth = childMinWidth;
            }
        }
        return minWidth;
    }

    @Override
    public int getMinHeight(int availableWidth) {
        int allWidgetsMinHeight = getAllWidgetsMinHeight(availableWidth);
        if (!usesFlex) {
            return allWidgetsMinHeight;
        }
        // TODO: does not use same method as renderer
        int minHeight = 0;
        for (int i = 0; i < children.length; i++) {
            int childMinHeight = getChildMinHeight(children[i], availableWidth);
            final double flex = flexValues[i];
            if (flex != 0) {
                final double flexPercentage = flex / allWidgetsFlex;
                childMinHeight = ((int) Math.ceil(childMinHeight / flexPercentage)) + allWidgetsMinHeight;
            }
            if (childMinHeight > minHeight) {
                minHeight = childMinHeight;
            }
        }
        return minHeight;
    }

    @Override
    public int getMaxHeight(int maxAvailableWidth, int maxAvailableHeight) {
        if (usesFlex || useFullHeight) {
            return maxAvailableHeight;
        }
        return getAllWidgetsMinHeight(maxAvailableWidth);
    }

    @Override
    public void insertIntoWidgetTree(WidgetContext c) {
        for (Widget child : children) {
            child.insertIntoWidgetTree(c);
        }
    }

    @Override
    public void takeOutOfWidgetTree() {
        for (Widget child : children) {
            child.takeOutOfWidgetTree();
        }
    }

    @Override
    public boolean shouldParentRerender(WidgetErrorRecorder errorRecorder) {
        for (Widget child : children) {
            if(child.shouldParentRerender(errorRecorder)) {
                return true;
            }
        }
        return false;
    }
}
