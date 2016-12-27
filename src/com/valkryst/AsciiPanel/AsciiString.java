package com.valkryst.AsciiPanel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import lombok.Getter;

public class AsciiString {
    /** The characters of the string. */
    @Getter private AsciiCharacter[] characters;

    /**
     * Constructs a new AsciiString of the specified length with all characters set to ' '.
     *
     * @param length
     *         The length to make the string.
     */
    public AsciiString(final int length) {
        characters = new AsciiCharacter[length];

        for (int i = 0; i < characters.length; i++) {
            characters[i] = new AsciiCharacter(' ');
        }
    }

    /**
     * Constructs a new AsciiString.
     *
     * @param string
     *         The string.
     */
    public AsciiString(final String string) {
        if (string == null) {
            characters = new AsciiCharacter[0];
        } else {
            characters = new AsciiCharacter[string.length()];

            for (int i = 0; i < characters.length; i++) {
                characters[i] = new AsciiCharacter(string.charAt(i));
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0 ; i < characters.length ; i++) {
            builder.append(characters[i].getCharacter());
        }

        return builder.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof AsciiString == false) {
            return false;
        }

        final AsciiString otherString = (AsciiString) object;

        if (characters.length != otherString.getCharacters().length) {
            return false;
        }

        for (int i = 0 ; i < characters.length ; i++) {
           final AsciiCharacter thisChar = characters[i];
           final AsciiCharacter otherChar = otherString.getCharacters()[i];

           if (thisChar.equals(otherChar) == false) {
               return false;
           }
        }

        return true;
    }

    /**
     * Draws the characters of the string onto the specified context.
     *
     * @param gc
     *         The context on which to draw.
     *
     * @param font
     *         The font to draw with.
     *
     * @param rowIndex
     *         The y-axis (row) coordinate where the characters are to be drawn.
     */
    public void draw(final GraphicsContext gc, final AsciiFont font, final int rowIndex) {
        for (int columnIndex = 0 ; columnIndex < characters.length ; columnIndex++) {
            characters[columnIndex].draw(gc, font, columnIndex, rowIndex);
        }
    }

    /**
     * Replaces the character at the specified position with the specified character.
     *
     * @param columnIndex
     *         The x-axis (column) coordinate of the character to be replaced.
     *
     * @param character
     *         The new character.
     */
    public void replaceCharacter(final int columnIndex, final AsciiCharacter character) {
        boolean canProceed = columnIndex >= 0;
        canProceed &= columnIndex < characters.length;
        canProceed &= character != null;

        if (canProceed) {
            characters[columnIndex] = character;
        }
    }

    /**
     * Sets all characters to either be hidden or visible.
     *
     * @param isHidden
     *         Whether or not the characters are to be hidden.
     */
    public void setHidden(final boolean isHidden) {
        for (final AsciiCharacter c : characters) {
            c.setHidden(isHidden);
        }
    }

    /** Swaps the background and foreground colors of every character. */
    public void invertColors() {
        for (final AsciiCharacter c : characters) {
            c.invertColors();
        }
    }

    /**
     * Sets the background color of all characters.
     *
     * @param color
     *         The new background color.
     */
    public void setBackgroundColor(final Paint color) {
        for (final AsciiCharacter c : characters) {
            c.setBackgroundColor(color);
        }
    }

    /**
     * Sets the foreground color of all characters.
     *
     * @param color
     *         The new foreground color.
     */
    public void setForegroundColor(final Paint color) {
        for (final AsciiCharacter c : characters) {
            c.setForegroundColor(color);
        }
    }
}
