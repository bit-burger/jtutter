package test.content;

import content.Text;
import org.junit.Test;

import static org.junit.Assert.*;

public class TextTest {

    Text text = new Text(" _word1 wor,        \n wo rd3 word4");

    @Test
    public void testGetMinHeight() throws Exception {
        assertEquals(2, text.getMinHeight(100));
    }

    @Test
    public void testGetMinHeightSlimmer() throws Exception {
        assertEquals(4, text.getMinHeight(6));
    }

    @Test
    public void testGetMinWidthLowest() throws Exception {
        assertEquals(12, text.getMinWidth(2));
    }

    @Test
    public void testGetMinWidthLower() throws Exception {
        assertEquals(6, text.getMinWidth(5));
    }

    @Test
    public void testGetMinWidth() throws Exception {
        assertEquals(6, text.getMinWidth(100));
    }

    @Test
    public void testGetAbsoluteMinWidth() throws Exception {
        assertEquals(6, text.getAbsoluteMinWidth());
    }

    @Test
    public void testGetAbsoluteMinHeight() throws Exception {
        assertEquals(2, text.getAbsoluteMinHeight());
    }
}
