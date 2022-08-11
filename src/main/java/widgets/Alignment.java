package widgets;

public enum Alignment {
    topLeft(BasicAlignment.start, BasicAlignment.start),
    topCenter(BasicAlignment.start, BasicAlignment.center),
    topRight(BasicAlignment.start, BasicAlignment.end),

    centerLeft(BasicAlignment.center, BasicAlignment.start),
    center(BasicAlignment.center, BasicAlignment.center),
    centerRight(BasicAlignment.center, BasicAlignment.end),

    bottomLeft(BasicAlignment.end, BasicAlignment.start),
    bottomCenter(BasicAlignment.end, BasicAlignment.center),
    bottomRight(BasicAlignment.end, BasicAlignment.end);



    private final BasicAlignment verticalAlignment;
    private final BasicAlignment horizontalAlignment;

    Alignment(BasicAlignment verticalAlignment, BasicAlignment horizontalAlignment) {
        this.verticalAlignment = verticalAlignment;
        this.horizontalAlignment = horizontalAlignment;
    }

    public BasicAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public BasicAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }
}
