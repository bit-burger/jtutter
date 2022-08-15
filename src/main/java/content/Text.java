package content;

import base_widgets.Widget;
import base_widgets.WidgetErrorRecorder;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;

public class Text extends Widget {
    static private final TextStyle defaultTextStyle = new TextStyle();

    static private boolean stringContainsAtLeastOneNonWhiteSpaceCharacter(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '\n' && c != ' ') {
                return true;
            }
        }
        return false;
    }

    final private String data;
    final private String[] wrappingTokens;
    final private TextStyle style;

    public Text(String data) {
        this(data, defaultTextStyle);
    }

    public Text(String data, TextStyle style) {
        this.data = data;
        this.style = style;
        assert style.getWrappingBehavior() == TextWrappingBehavior.characters || stringContainsAtLeastOneNonWhiteSpaceCharacter(
                data) : "string should contain at least one character, that is not a linebreak or space, " + "if the "
                + "wrapping behavior is set to words";
        if (style.getWrappingBehavior() == TextWrappingBehavior.words) {
            wrappingTokens = getWordWrappingTokens();
        } else {
            wrappingTokens = getCharacterWrappingTokens();
        }
    }

    private String[] getCharacterWrappingTokens() {
        int wrappingTokensLength = 1;
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '\n') {
                wrappingTokensLength++;
            }
        }
        String[] wrappingTokens = new String[wrappingTokensLength];
        int currentWrappingTokenIndex = 0;
        int currentNonLinebreakTokenBegin = 0;
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == '\n') {
                wrappingTokens[currentWrappingTokenIndex] = data.substring(currentNonLinebreakTokenBegin, i);
                currentWrappingTokenIndex++;
                currentNonLinebreakTokenBegin = i + 1;
            }
        }
        wrappingTokens[currentWrappingTokenIndex] = data.substring(currentNonLinebreakTokenBegin);
        return wrappingTokens;
    }

    private int getWordWrappingTokensLength() {
        int wrappingTokensLength = 0;
        boolean isWord = false;
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == '\n' || c == ' ') {
                wrappingTokensLength++;
                isWord = false;
            } else if (!isWord) {
                wrappingTokensLength++;
                isWord = true;
            }
        }
        return wrappingTokensLength;

    }

    private String[] getWordWrappingTokens() {
        String[] wrappingTokens = new String[getWordWrappingTokensLength()];
        int currentWrappingTokenIndex = 0;
        int currentWrappingWordBegin = 0;
        boolean currentlyWrappingWord = false;
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == '\n' || c == ' ') {
                if (currentlyWrappingWord) {
                    wrappingTokens[currentWrappingTokenIndex] = data.substring(currentWrappingWordBegin, i);
                    currentlyWrappingWord = false;
                    currentWrappingTokenIndex++;
                }
                wrappingTokens[currentWrappingTokenIndex] = data.substring(i, i + 1);
                currentWrappingWordBegin = i + 1;
                currentWrappingTokenIndex++;
            } else if (!currentlyWrappingWord) {
                currentWrappingWordBegin = i;
                currentlyWrappingWord = true;
            }
        }
        if (currentlyWrappingWord) {
            wrappingTokens[currentWrappingTokenIndex] = data.substring(currentWrappingWordBegin);
        }
        return wrappingTokens;
    }

    private int getMinHeightCharacterWrapping(int availableWidth) {
        int lineCount = 0;
        for (String token : wrappingTokens) {
            if (token.isEmpty()) {
                lineCount++;
            } else {
                lineCount += Math.ceil(token.length() / (double) availableWidth);
            }
        }
        return lineCount;
    }

    private int getMinHeightWordWrapping(int availableWidth) {
        // TODO: will give 1 back if availableWidth is smaller than longest word (give as mistake in render maybe????)
        int lineCount = 1;
        boolean newLine = true;
        int currentLineCharacters = 0;
        for (String token : wrappingTokens) {
            if (token.equals(" ") && (newLine || currentLineCharacters == availableWidth)) {
                continue;
            }
            if (token.equals("\n")) {
                // TODO: multiple \n after eachother will have no effect, but in rendering? (possibly closed)
                if (currentLineCharacters < availableWidth) {
                    newLine = true;
                    currentLineCharacters = 0;
                    lineCount++;
                }
                continue;
            }
            newLine = false;
            currentLineCharacters += token.length();
            if (currentLineCharacters > availableWidth) {
                lineCount++;
                currentLineCharacters = token.length();
            }
        }
        return lineCount;
    }

    @Override
    public int getMinHeight(int availableWidth) {
        if (style.getWrappingBehavior() == TextWrappingBehavior.characters) {
            return getMinHeightCharacterWrapping(availableWidth);
        }
        return getMinHeightWordWrapping(availableWidth);
    }

    @Override
    public int getMinWidth(int availableHeight) {
        // TODO: replace inefficient algorithm (by giving better base width fe)
        int width = getAbsoluteMinWidth();

        while (true) {
            if (getMinHeight(width) <= availableHeight) {
                return width;
            }
            width++;
        }
    }

    @Override
    public boolean hasComplexLayout() {
        return true;
    }

    @Override
    public int getAbsoluteMinWidth() {
        if (style.getWrappingBehavior() == TextWrappingBehavior.characters) {
            return 1;
        }
        int maxTokenLength = 0;
        for (String token : wrappingTokens) {
            maxTokenLength = Math.max(token.length(), maxTokenLength);
        }
        return maxTokenLength;
    }

    @Override
    public int getAbsoluteMinHeight() {
        int amountOfLineBreaks = 0;
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '\n') {
                amountOfLineBreaks++;
            }
        }
        return amountOfLineBreaks + 1;
    }

    @Override
    public int getMaxWidth(int maxAvailableWidth, int maxAvailableHeight) {
        if(!style.isShrinkWidth()) {
            return maxAvailableWidth;
        }
        if (style.getWrappingBehavior() == TextWrappingBehavior.characters) {
            int maxTokenLength = 0;
            for (String token : wrappingTokens) {
                maxTokenLength = Math.max(maxTokenLength, token.length());
            }
            return Math.min(maxTokenLength, maxAvailableWidth);
        }
        int longestLineLength = 0;
        int currentLineLength = 0;
        int currentLineLengthWithEndSpaces = 0;
        for (String token : wrappingTokens) {
            switch (token) {
                case " ":
                    if (currentLineLengthWithEndSpaces > 0) {
                        currentLineLengthWithEndSpaces++;
                    }
                    break;
                default:
                    if (token.length() + currentLineLengthWithEndSpaces > maxAvailableWidth) {
                        longestLineLength = Math.max(longestLineLength, currentLineLength);
                        currentLineLengthWithEndSpaces = 0;
                    }
                    currentLineLengthWithEndSpaces += token.length();
                    currentLineLength = currentLineLengthWithEndSpaces;
                    break;
                case "\n":
                    longestLineLength = Math.max(longestLineLength, currentLineLength);
                    currentLineLength = 0;
                    currentLineLengthWithEndSpaces = 0;
            }
        }
        return Math.max(longestLineLength, currentLineLength);
    }

    @Override
    public int getMaxHeight(int maxAvailableWidth, int maxAvailableHeight) {
        return getMinHeight(maxAvailableWidth);
    }

    private void renderChar(int x, int y, char c, Screen screen) {
        screen.setCharacter(x, y, new TextCharacter(c, style.getColor(), style.getBackgroundColor()));
    }

    private void renderCharacterWrapLine(
            // tokenCharacterEnd is exclusive
            int x, int y, int maxWidth, String token, int tokenCharacterBegin, int tokenCharacterEnd, Screen screen
    ) {
        int lineLength = tokenCharacterEnd - tokenCharacterBegin;
        if (lineLength < maxWidth) {
            switch (style.getTextAlign()) {
                case center -> x += ((maxWidth - lineLength) / 2);
                case end -> x += (maxWidth - lineLength);
            }
        }
        for (int tokenCharacterIndex = tokenCharacterBegin; tokenCharacterIndex < tokenCharacterEnd; tokenCharacterIndex++) {
            renderChar(x, y, token.charAt(tokenCharacterIndex), screen);
            x++;
        }
    }

    private void renderCharacterWrap(int x, int y, int width, Screen screen) {
        int currentLine = 0;
        for (String token : wrappingTokens) {
            if(token.isEmpty()) {
                currentLine++;
                continue;
            }
            int lastRenderedCharacterIndex = 0; // exclusive
            while (lastRenderedCharacterIndex < token.length()) {
                int howManyCharactersToRender = Math.min(token.length() - lastRenderedCharacterIndex, width);
                renderCharacterWrapLine(
                        x,
                        y + currentLine,
                        width,
                        token,
                        lastRenderedCharacterIndex,
                        lastRenderedCharacterIndex + howManyCharactersToRender,
                        screen
                );
                lastRenderedCharacterIndex += howManyCharactersToRender;
                currentLine++;
            }
        }
    }

    private void renderWordWrapLine(
            // endTokenOfLine is exclusive
            int x, int y, int lineLength, int maxWidth, int firstTokenOfLine, int endTokenOfLine, Screen screen
    ) {
        if (lineLength < maxWidth) {
            switch (style.getTextAlign()) {
                case center -> x += ((maxWidth - lineLength) / 2);
                case end -> x += (maxWidth - lineLength);
            }
        }
        for (int currentTokenIndex = firstTokenOfLine; currentTokenIndex < endTokenOfLine; currentTokenIndex++) {
            String currentToken = wrappingTokens[currentTokenIndex];
            for (int currentTokenCharIndex = 0; currentTokenCharIndex < currentToken.length(); currentTokenCharIndex++) {
                renderChar(x, y, currentToken.charAt(currentTokenCharIndex), screen);
                x++;
            }
        }
    }

    private void renderWordWrap(int x, int y, int width, Screen screen) {
        int currentLine = 0;

        int currentLineLength = 0;
        int currentLineLengthWithEndSpaces = 0;

        int currentLineFirstTokenIndex = 0;
        int currentLineEndTokenIndex = 0;

        boolean isInLine = false;
        for (int tokenIndex = 0; tokenIndex < wrappingTokens.length; tokenIndex++) {
            String token = wrappingTokens[tokenIndex];
            switch (token) {
                case " ":
                    if (currentLineLengthWithEndSpaces > 0) {
                        currentLineLengthWithEndSpaces++;
                    }
                    break;
                default:
                    isInLine = true;
                    if (token.length() + currentLineLengthWithEndSpaces > width) {
                        renderWordWrapLine(
                                x,
                                y + currentLine,
                                currentLineLength,
                                width,
                                currentLineFirstTokenIndex,
                                currentLineEndTokenIndex,
                                screen
                        );
                        currentLineFirstTokenIndex = tokenIndex;
                        currentLineLengthWithEndSpaces = 0;
                        currentLine++;
                    }
                    currentLineEndTokenIndex = tokenIndex + 1;
                    currentLineLengthWithEndSpaces += token.length();
                    currentLineLength = currentLineLengthWithEndSpaces;
                    break;
                case "\n":
                    if (isInLine) {
                        renderWordWrapLine(
                                x,
                                y + currentLine,
                                currentLineLength,
                                width,
                                currentLineFirstTokenIndex,
                                currentLineEndTokenIndex,
                                screen
                        );
                    }
                    isInLine = false;
                    currentLine++;
                    currentLineLength = 0;
                    currentLineLengthWithEndSpaces = 0;
                    currentLineFirstTokenIndex = tokenIndex + 1;
                    currentLineEndTokenIndex = tokenIndex + 2;
            }
        }
        if (isInLine) {
            renderWordWrapLine(
                    x,
                    y + currentLine,
                    currentLineLength,
                    width,
                    currentLineFirstTokenIndex,
                    currentLineEndTokenIndex,
                    screen
            );
        }
    }

    @Override
    public void rawRender(
            int x, int y, int width, int height, Screen screen, WidgetErrorRecorder errorRecorder
    ) {
        if (style.getWrappingBehavior() == TextWrappingBehavior.characters) {
            renderCharacterWrap(x, y, width, screen);
        } else {
            renderWordWrap(x, y, width, screen);
        }
    }
}
