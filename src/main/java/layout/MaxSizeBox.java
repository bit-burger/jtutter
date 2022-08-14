package layout;

import base_widgets.OptionalChildWidget;
import base_widgets.Widget;

public class MaxSizeBox extends OptionalChildWidget {
    private final int maxWidth;
    private final int maxHeight;

    /**
     * @see MaxSizeBox#MaxSizeBox(int, int, Widget) without child
     */
    public MaxSizeBox(int maxWidth, int maxHeight) {
        this(maxWidth, maxHeight, null);
    }

    /**
     * Specify max height and/or max width for a child widget.
     *
     * @param maxWidth  How much width this widget should max. have, if given 0 or less, this is ignored. Should not
     *                  be less than the minimum width of the child.
     * @param maxHeight How much height this widget should max. have, if given 0 or less, this is ignored. Should not
     *                  be less than the minimum height of the child.
     * @param child     The (optional) child, that will not be rendered bigger than maxWidth or maxHeight, if they
     *                  are not 0 or less.
     */
    public MaxSizeBox(int maxWidth, int maxHeight, Widget child) {
        super(child);
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
//        if (child != null) {
//            assert maxWidth == 0 || maxWidth >= child.getMinWidth() : "Max width should not be less than the child minimum width";
//            assert maxHeight == 0 || maxHeight >= child.getMinHeight() : "Max width should not be less than the child minimum width";
//        }
    }

    public static MaxSizeBox square(int size) {
        return square(size, null);
    }

    public static MaxSizeBox square(int size, Widget child) {
        return new MaxSizeBox(size, size, child);
    }

    public static MaxSizeBox width(int maxWidth) {
        return width(maxWidth, null);
    }

    public static MaxSizeBox width(int maxWidth, Widget child) {
        return new MaxSizeBox(maxWidth, 0, child);
    }

    public static MaxSizeBox height(int maxHeight) {
        return height(maxHeight, null);
    }

    public static MaxSizeBox height(int maxHeight, Widget child) {
        return new MaxSizeBox(0, maxHeight, child);
    }

    @Override
    public int getMaxWidth(int maxAvailableWidth, int maxAvailableHeight) {
        if(maxWidth > 0) {
            return Math.min(maxWidth, maxAvailableWidth);
        }
        return super.getMaxWidth(maxAvailableWidth, maxAvailableHeight);
    }

    @Override
    public int getMaxHeight(int maxAvailableWidth, int maxAvailableHeight) {
        if(maxHeight > 0) {
            return Math.min(maxHeight, maxAvailableHeight);
        }
        return super.getMaxHeight(maxAvailableWidth, maxAvailableHeight);
    }
}
