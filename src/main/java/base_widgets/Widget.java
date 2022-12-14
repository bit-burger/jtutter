package base_widgets;

import com.googlecode.lanterna.screen.Screen;
import layout.Padding;
import net.jcip.annotations.Immutable;

/**
 * The base class for all widgets of the jTutter widget api.
 * <p>
 * Inside the overridable method {@link Widget#rawRender},
 * the Widget renders itself on a {@link Screen} in the given boundaries.
 * The boundaries should not be bigger sized than
 * {@link Widget#getMaxWidth(int, int)} and {@link Widget#getMaxHeight(int, int)} and not smaller than
 * {@link Widget#getMinHeight(int)} and {@link Widget#getMinHeight(int)}.
 * </p>
 * <p>
 * Alternatively use the the {@link Widget#safeRender(int, int, int, int, Screen, WidgetErrorRecorder)} method,
 * to automatically size the boundaries appropriately, if they become too big.
 * It still cannot change anything, if the boundaries are too small.
 * This means that before rendering a Widget, the renderer should always first check
 * {@link Widget#getMinHeight(int)} and {@link Widget#getMinHeight(int)}.
 * </p>
 * <p>
 * As many Widgets will use other widgets as their children to render on top of them,
 * they can use {@link Widget#getMaxWidth(int, int)}, {@link Widget#getMaxHeight(int, int)},
 * {@link Widget#getMinWidth(int)}, and {@link Widget#getMinHeight(int)} from their child widget,
 * to decide how much space their child widget is allowed to render in. They should not however,
 * render their child using only {@link Widget#getMinWidth(int)} and {@link Widget#getMinHeight(int)},
 * as some layout widgets will not work then.
 * </p>
 * <p>
 * If a Widget features another Widget as its child,
 * it should also make sure that {@link Widget#getMinWidth(int)} and {@link Widget#getMinHeight(int)},
 * leave enough space for their child and additional width and height possibly caused by the widget.
 * See {@link Padding} for an example, as more padding, decreases the usable space for the child widget.
 * </p>
 */
@Immutable
public abstract class Widget {
    /**
     * Let the widget render itself in the given boundaries.
     * <p>
     * Never give bigger width or height than {@link Widget#getMaxWidth(int, int)}
     * and {@link Widget#getMaxHeight(int, int)}, as well as never give smaller width or height than
     * {@link Widget#getMinWidth(int)} and {@link Widget#getMinHeight(int)}.
     * </p>
     *
     * @param screen The {@link Screen} on which to render, only render in the boundaries defined by x, y, width, and
     *               height
     * @see Widget#safeRender(int, int, int, int, Screen, WidgetErrorRecorder) for an alterntive,
     * that does not care about a width or height that is too big.
     */
    public abstract void rawRender(
            int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder
    );

    /**
     * Calls {@link Widget#rawRender(int, int, int, int, Screen, WidgetErrorRecorder)},
     * use instead of {@link Widget#rawRender(int, int, int, int, Screen, WidgetErrorRecorder)} directly,
     * as this method does not care about boundaries being bigger
     * than {@link Widget#getMaxWidth(int, int)} and {@link Widget#getMaxHeight(int, int)},
     * which is the limit for a Widget to render in.
     * The method will simply resize the boundaries to the top left corner, if the boundaries are too big.
     */
    public void safeRender(
            int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder
    ) {
        int maxWidth = getMaxWidth(width, height);
        int maxHeight = getMaxHeight(width, height);
        if (maxWidth < width) {
            width = maxWidth;
        }
        if (maxHeight < height) {
            height = maxHeight;
        }
        rawRender(x, y, width, height, screen, errorRecorder);
    }

    // do not override getMinWidth or getMinHeight, if hasComplexLayout is false

    /**
     * Returns the minimum width the Widget needs to render.
     *
     * @return The minimum width this widget needs, if the minimum width is bigger
     * than the available width, the widget renderer should display an error, or at least not render the Widget.
     * @apiNote If the widget is rendered in the minimum width,
     * do not use render the widget in the minimum height as well,
     * Widgets like AspectRatio depend on it.
     * @apiNote If the renderer finds the widget does not have enough space,
     * the renderer should display something else, such as an error instead of rendering the widget.
     */
    public int getMinWidth(int availableHeight) {
        return getAbsoluteMinWidth();
    }

    /**
     * Returns the minimum height the Widget needs to render.
     *
     * @return The minimum height this widget needs, if the minimum height is bigger
     * than the available space, the widget renderer should display an error, or at least not render the Widget.
     * @apiNote If the Widget is rendered in the minimum height,
     * do not use render the widget in the minimum width as well,
     * Widgets like AspectRatio depend on it.
     * @apiNote If the renderer finds the widget does not have enough space,
     * the renderer should display something else, such as an error instead of rendering the widget.
     */
    public int getMinHeight(int availableWidth) {
        return getAbsoluteMinHeight();
    }

    /**
     * If true, getAbsoluteMinWidth is only applicable, if the height is infinity, and vice versa.
     * <p>
     * hasComplexLayout should also be true if the absolute min width and height
     * are not the same as the standard min width and height
     */
    public boolean hasComplexLayout() {
        return false;
    }

    /**
     * {@link Widget#getMinHeight(int)} should never be called with a width smaller than this
     */
    public int getAbsoluteMinWidth() {
        return 0;
    }

    /**
     * {@link Widget#getMinWidth(int)} should never be called with a height smaller than this
     */
    public int getAbsoluteMinHeight() {
        return 0;
    }

    /**
     * Returns the maximum width the Widget is allowed to render in.
     *
     * @param maxAvailableWidth  The available width
     * @param maxAvailableHeight The available height (only of relevance for Widgets like AspectRatio)
     * @return The max width the widget can be rendered in for the given available width and height.
     * The given back width should be bigger than 0 and smaller or equals to the maxAvailableWidth.
     * @apiNote Never render a Widget in more width than getMaxWidth.
     */
    public int getMaxWidth(int maxAvailableWidth, int maxAvailableHeight) {
        return maxAvailableWidth;
    }

    /**
     * Returns the maximum height the Widget is allowed to render in.
     *
     * @param maxAvailableWidth  The available width (only of relevance for Widgets like AspectRatio)
     * @param maxAvailableHeight The available height
     * @return The max height the widget can be rendered in for the given available width and height.
     * The given back height should be bigger than 0 and smaller or equals to the maxAvailableHeight.
     * @apiNote Never render a Widget in more height than getMaxHeight.
     */
    public int getMaxHeight(int maxAvailableWidth, int maxAvailableHeight) {
        return maxAvailableHeight;
    }

    /**
     * Will be called by some renderers if inserted into a tree with a context.
     * <p>
     * Widgets working with the Context, should not be inserted multiple times.
     */
    public void insertIntoWidgetTree(WidgetContext c) {
    }

    /**
     * Will be called by the same renderers as in {@link Widget#insertIntoWidgetTree(WidgetContext)},
     * when the widget is taken out of the widget tree
     */
    public void takeOutOfWidgetTree() {
    }

    public boolean shouldParentRerender(WidgetErrorRecorder errorRecorder) {
        return false;
    }
}
