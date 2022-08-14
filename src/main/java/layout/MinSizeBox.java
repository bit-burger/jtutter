package layout;

import base_widgets.OneChildWidget;
import base_widgets.Widget;

public class MinSizeBox extends OneChildWidget {
    private final int minWidth;
    private final int minHeight;

    /**
     * Specify min height and/or min width for a child widget.
     *
     * @param minWidth  How much width this widget should min. have, should be at least 0,
     *                  if it is smaller than the child's {@link Widget#getMinWidth(int)}, that will be used instead.
     * @param minHeight How much height this widget should min. have, should be at least 0,
     *                  if it is smaller than the child's {@link Widget#getMinHeight(int)}, that will be used instead.
     * @param child     The child, that will not be rendered smaller than minWidth or minHeight.
     */
    public MinSizeBox(int minWidth, int minHeight, Widget child) {
        super(child);
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public static MinSizeBox square(int size, Widget child) {
        return new MinSizeBox(size, size, child);
    }

    public static MinSizeBox width(int minWidth, Widget child) {
        return new MinSizeBox(minWidth, 0, child);
    }

    public static MinSizeBox height(int minHeight, Widget child) {
        return new MinSizeBox(0, minHeight, child);
    }

    @Override
    public int getMinWidth(int availableHeight) {
        return Math.max(minWidth, super.getMinWidth(availableHeight));
    }

    @Override
    public int getMinHeight(int availableWidth) {
        return Math.max(minHeight, super.getMinHeight(availableWidth));
    }

    @Override
    public boolean hasComplexLayout() {
        if(minHeight != 0 && minWidth != 0) {
            return false;
        }
        return super.hasComplexLayout();
    }

    @Override
    public int getAbsoluteMinWidth() {
        if(minWidth != 0) {
            return minWidth;
        }
        return super.getAbsoluteMinWidth();
    }

    @Override
    public int getAbsoluteMinHeight() {
        if(minHeight != 0) {
            return minHeight;
        }
        return super.getAbsoluteMinHeight();
    }
}
