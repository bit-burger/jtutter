package widgets;

import com.googlecode.lanterna.screen.Screen;

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
                    allWidgetsMinHeight += child.getMinHeight();
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
    public void rawRender(int x, int y, int width, int height, Screen screen) {
        if (usesFlex) {
            flexRender(x, y, width, height, screen);
        } else {
            nonFlexRender(x, y, width, height, screen);
        }
    }

    private void renderChild(Widget child, int x, int y, int availableWidth, int childHeight, Screen screen) {
        int childMaxWidth = child.getMaxWidth(availableWidth, childHeight);
        if (childMaxWidth >= availableWidth) {
            child.rawRender(x, y, availableWidth, childHeight, screen);
        } else {
            switch (crossAxisAlignment) {
                case center -> x += (int) Math.floor((availableWidth - childMaxWidth) / 2.0);
                case end -> x += availableWidth - childMaxWidth;
            }
            child.rawRender(x, y, childMaxWidth, childHeight, screen);
        }
    }

    private void flexRender(int x, int y, int width, int height, Screen screen) {
        int remainingHeightForFlex = height - allWidgetsMinHeight;
        int numberOfRoundUps = remainingHeightForFlex;
        final List<Double> roundUps = new ArrayList<>();
        for (double flexValue : flexValues) {
            double childHeight = remainingHeightForFlex * (flexValue / allWidgetsFlex);
            double childHeightCeil = Math.ceil(childHeight);
            double childHeightFloor = Math.floor(childHeight);
            if (childHeightCeil > childHeight) {
                double roundUp = childHeight - Math.floor(childHeight);
                roundUps.add(roundUp);
            }
            numberOfRoundUps -= childHeightFloor;
        }
        roundUps.sort(Double::compare);
        roundUps.sort(Comparator.reverseOrder());
        double lastRoundUp = roundUps.get(numberOfRoundUps - 1);

        for (int i = 0; i < children.length; i++) {
            Widget child = children[i];
            int childHeight;
            if (flexValues[i] == 0) {
                childHeight = child.getMinHeight();
            } else {
                double rawChildHeight = remainingHeightForFlex * (flexValues[i] / allWidgetsFlex);
                double roundUp = height - Math.floor(height);
                if (roundUp >= lastRoundUp && numberOfRoundUps > 0) {
                    numberOfRoundUps--;
                    childHeight = (int) Math.ceil(rawChildHeight);
                } else {
                    childHeight = (int) Math.floor(rawChildHeight);
                }
            }
            renderChild(child, x, y, width, childHeight, screen);
            y += childHeight;
        }
    }

    private void nonFlexRender(int x, int y, int width, int height, Screen screen) {
        switch (mainAxisAlignment) {
            case center -> y += Math.floor((height - allWidgetsMinHeight) / 2.0);
            case end -> y += height - allWidgetsMinHeight;
        }
        for (Widget child : children) {
            int childHeight = child.getMinHeight();
            renderChild(child, x, y, width, childHeight, screen);
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
    public int getMinWidth() {
        int minWidth = 0;
        for (Widget child : children) {
            int childMinWidth = child.getMinWidth();
            if (minWidth < childMinWidth) {
                minWidth = childMinWidth;
            }
        }
        return minWidth;
    }

    @Override
    public int getMinHeight() {
        if (!usesFlex) {
            return allWidgetsMinHeight;
        }
        int minHeight = 0;
        for (int i = 0; i < children.length; i++) {
            int childMinHeight = children[i].getMinHeight();
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
