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
     *                  if it is smaller than the child's {@link Widget#getMinWidth()}, that will be used instead.
     * @param minHeight How much height this widget should min. have, should be at least 0,
     *                  if it is smaller than the child's {@link Widget#getMinHeight()}, that will be used instead.
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
    public int getMinWidth() {
        return Math.max(minWidth, child.getMinWidth());
    }

    @Override
    public int getMinHeight() {
        return Math.max(minHeight, child.getMinWidth());
    }
}
