package com.valkryst;

import com.valkryst.AsciiPanel.AsciiCharacter;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

public class AsciiCharacterTest {
    @Test
    public void AsciiCharacter() {
        for (char c = 0 ; c < Character.MAX_VALUE ; c++) {
            final AsciiCharacter character = new AsciiCharacter(c);

            boolean passed = character.getCharacter() == c;
            passed &= character.getBackgroundColor().equals(Color.BLACK);
            passed &= character.getForegroundColor().equals(Color.WHITE);

            if (passed == false) {
                System.out.println("Other Character: '" + c + "'");
                System.out.println(character);
            }

            Assert.assertTrue(passed);
        }
    }

    @Test
    public void equals() {
        final AsciiCharacter characterA = new AsciiCharacter('a');
        final AsciiCharacter characterB = new AsciiCharacter('a');
        Assert.assertTrue(characterA.equals(characterB));
    }

    @Test
    public void setBackgroundColorA() {
        final AsciiCharacter character = new AsciiCharacter('a');
        character.setBackgroundColor(Color.RED);
        Assert.assertTrue(character.getBackgroundColor().equals(Color.RED));
    }

    @Test
    public void setBackgroundColorB() {
        final AsciiCharacter character = new AsciiCharacter('a');
        character.setBackgroundColor(null);
        Assert.assertTrue(character.getBackgroundColor().equals(Color.BLACK));
    }

    @Test
    public void setForegroundColorA() {
        final AsciiCharacter character = new AsciiCharacter('a');
        character.setForegroundColor(Color.RED);
        Assert.assertTrue(character.getForegroundColor().equals(Color.RED));
    }

    @Test
    public void setForegroundColorB() {
        final AsciiCharacter character = new AsciiCharacter('a');
        character.setBackgroundColor(null);
        Assert.assertTrue(character.getForegroundColor().equals(Color.WHITE));
    }
}