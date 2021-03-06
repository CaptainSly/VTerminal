package com.valkryst.VTerminal.AsciiCharacterTest;

import com.valkryst.VTerminal.AsciiCharacter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

public class SetBackgroundColorTest {
    private AsciiCharacter character;

    @Before
    public void initializeCharacter() {
        character = new AsciiCharacter('A');
    }

    @Test
    public void withValidInput() {
        character.setBackgroundColor(Color.RED);
        Assert.assertEquals(character.getBackgroundColor(), Color.RED);
    }

    @Test(expected=NullPointerException.class)
    public void withNullColor() {
        character.setBackgroundColor(Color.RED);
        character.setBackgroundColor(null);
    }

    @Test
    public void withColorThatIsTheSameAsTheExistingBackgroundColor() {
        character.setBackgroundColor(character.getBackgroundColor());
    }
}
