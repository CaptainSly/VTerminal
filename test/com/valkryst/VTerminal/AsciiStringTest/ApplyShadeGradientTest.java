package com.valkryst.VTerminal.AsciiStringTest;

import com.valkryst.VTerminal.AsciiCharacter;
import com.valkryst.VTerminal.AsciiString;
import com.valkryst.VTerminal.misc.IntRange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

public class ApplyShadeGradientTest {
    private AsciiString string;

    @Before
    public void initializeString() {
        string = new AsciiString("ABCDEFGHJIKLMNOP");
        string.setBackgroundColor(Color.WHITE);
        string.setForegroundColor(Color.BLACK);
    }

    @Test
    public void toBackgroundOfAllCharacters_withValidInput() {
        string.applyShadeGradient(Color.RED, true);

        for (final AsciiCharacter character : string.getCharacters()) {
            Assert.assertNotEquals(Color.WHITE, character.getBackgroundColor());
            Assert.assertEquals(Color.BLACK, character.getForegroundColor());
        }
    }

    @Test
    public void toForegroundOfAllCharacters_withValidInput() {
        string.applyShadeGradient(Color.RED,  false);

        for (final AsciiCharacter character : string.getCharacters()) {
            Assert.assertEquals(Color.WHITE, character.getBackgroundColor());
            Assert.assertNotEquals(Color.BLACK, character.getForegroundColor());
        }
    }

    @Test
    public void toBackgroundOfRange_withValidInput() {
        final IntRange range = new IntRange(0, string.getCharacters().length);

        string.applyShadeGradient(range, Color.RED,  true);

        for (final AsciiCharacter character : string.getCharacters()) {
            Assert.assertNotEquals(Color.WHITE, character.getBackgroundColor());
            Assert.assertEquals(Color.BLACK, character.getForegroundColor());
        }
    }

    @Test
    public void toForegroundOfRange_withValidInput() {
        final IntRange range = new IntRange(0, string.getCharacters().length);

        string.applyShadeGradient(range, Color.RED,  false);

        for (final AsciiCharacter character : string.getCharacters()) {
            Assert.assertEquals(Color.WHITE, character.getBackgroundColor());
            Assert.assertNotEquals(Color.BLACK, character.getForegroundColor());
        }
    }

    @Test(expected=NullPointerException.class)
    public void toRange_withNullColor() {
        final IntRange range = new IntRange(0, string.getCharacters().length);

        string.applyShadeGradient(range, null, true);
    }
}
