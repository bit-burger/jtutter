package layout;

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
     * Column is as high as allWidgetsMinHeight, except if usesFlex is true, then column is as high as possible.
     */
    final private int allWidgetsMinHeight;
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
        int allWidgetsMinHeight = 0;
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
                    int childMinHeight = child.getMinHeight(0);// TODO;
                    assert childMinHeight > 0 : "min height of child " + child + " cannot be 0, if no flex is used";
                    allWidgetsMinHeight += childMinHeight;
                }
            }
            allWidgetsFlex += flex;
            flexValues[i] = flex;
        }
        this.usesFlex = usesFlex;
        this.allWidgetsMinHeight = allWidgetsMinHeight;
        this.allWidgetsFlex = allWidgetsFlex;
    }

    @Override
    public void rawRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        if (usesFlex) {
            flexRender(x, y, width, height, screen, errorRecorder);
        } else {
            nonFlexRender(x, y, width, height, screen, errorRecorder);
        }
    }

    private void renderChild(
            Widget child,
            int x,
            int y,
            int availableWidth,
            int childHeight,
            Screen screen,
            WidgetErrorRecorder errorRecorder
    ) {
        int childMaxWidth = child.getMaxWidth(availableWidth, childHeight);
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

    private void flexRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        int remainingHeightForFlex = height - allWidgetsMinHeight;
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
                childUsedHeight = child.getMinHeight(width);
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
                childRealHeight = child.getMaxHeight(width, childUsedHeight);
            }
            renderChild(child, x, y, width, childRealHeight, screen, errorRecorder);
            y += childUsedHeight;
        }
    }

    private void nonFlexRender(int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder) {
        switch (mainAxisAlignment) {
            case center -> y += Math.floor((height - allWidgetsMinHeight) / 2.0);
            case end -> y += height - allWidgetsMinHeight;
        }
        for (Widget child : children) {
            int childHeight = child.getMinHeight(width);
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
            int childMaxWidth = child.getMaxWidth(maxAvailableWidth, maxAvailableHeight);
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
            int childMinWidth = child.getAbsoluteMinWidth();
            if (minWidth < childMinWidth) {
                minWidth = childMinWidth;
            }
        }
        return minWidth;
    }

    @Override
    public int getMinHeight(int availableWidth) {
        if (!usesFlex) {
            return allWidgetsMinHeight;
        }
        // TODO: does not use same method as renderer
        int minHeight = 0;
        for (int i = 0; i < children.length; i++) {
            int childMinHeight = children[i].getMinHeight(availableWidth);
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
        return allWidgetsMinHeight;
    }
}
