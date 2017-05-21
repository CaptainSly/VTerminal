package com.valkryst.VTerminal.component;


import com.valkryst.VTerminal.AsciiCharacter;
import com.valkryst.VTerminal.AsciiPanel;
import com.valkryst.VTerminal.AsciiString;
import com.valkryst.VTerminal.builder.component.AsciiTextFieldBuilder;
import com.valkryst.radio.Radio;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AsciiTextField extends AsciiComponent {
    /** The foreground color of the caret. */
    @Getter @Setter private Color caretForegroundColor;
    /** The background color of the caret. */
    @Getter @Setter private Color caretBackgroundColor;

    /** The foreground color of non-caret characters. */
    @Getter @Setter private Color foregroundColor;
    /** The background color of non-caret characters. */
    @Getter @Setter private Color backgroundColor;

    /** Whether or not the HOME key can be used to move the caret to the first index of the field. */
    @Getter @Setter private boolean homeKeyEnabled;
    /** Whether or not the END key can be used to move the caret to the last index of the field. */
    @Getter @Setter private boolean endKeyEnabled;
    /** Whether or not the DELETE key can be used to erase the character that the caret is on. */
    @Getter @Setter private boolean deleteKeyEnabled;
    /** Whether or not the LEFT ARROW key can be used to move the caret one index to the left. */
    @Getter @Setter private boolean leftArrowKeyEnabled;
    /** Whether or not the RIGHT ARROW key can be used to move the caret one index to the right. */
    @Getter @Setter private boolean rightArrowKeyEnabled;
    /** Whether or not the BACK SPACE key can be used to erase the character before the caret and move the caret backwards. */
    @Getter @Setter private boolean backSpaceKeyEnabled;

    private int index_caret = 0;

    private int maxCharacters;

    /** The pattern used to determine which typed characters can be entered into the field. */
    @Getter @Setter private Pattern allowedCharacterPattern;

    /**
     * Constructs a new AsciiTextField.
     *
     * @param builder
     *         The builder to use.
     */
    public AsciiTextField(final AsciiTextFieldBuilder builder) {
        super(builder.getColumnIndex(), builder.getRowIndex(), builder.getWidth(), 1);

        super.setRadio(builder.getRadio());

        caretForegroundColor = builder.getCaretForegroundColor();
        caretBackgroundColor = builder.getCaretBackgroundColor();

        foregroundColor = builder.getForegroundColor();
        backgroundColor = builder.getBackgroundColor();

        homeKeyEnabled = builder.isHomeKeyEnabled();
        endKeyEnabled = builder.isEndKeyEnabled();
        deleteKeyEnabled = builder.isDeleteKeyEnabled();
        leftArrowKeyEnabled = builder.isLeftArrowKeyEnabled();
        rightArrowKeyEnabled = builder.isRightArrowKeyEnabled();
        backSpaceKeyEnabled = builder.isBackSpaceKeyEnabled();

        maxCharacters = builder.getWidth();

        allowedCharacterPattern = builder.getAllowedCharacterPattern();

        // Set the button's text:
        final AsciiString string = super.getStrings()[0];
        string.setAllCharacters(' ');
        string.setBackgroundAndForegroundColor(backgroundColor, foregroundColor);

        // Set initial caret position:
        changeCaretPosition(index_caret);
    }

    @Override
    public void registerEventHandlers(final AsciiPanel panel) {
        super.registerEventHandlers(panel);

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
                if (isFocused()) {
                    final char character = e.getKeyChar();
                    final Matcher matcher = allowedCharacterPattern.matcher(character + "");

                    if (matcher.matches()) {
                        changeCharacter(index_caret, character);
                        changeCaretPosition(index_caret + 1);
                        transmitDraw();
                    }
                }
            }

            @Override
            public void keyPressed(final KeyEvent e) {

            }

            @Override
            public void keyReleased(final KeyEvent e) {
                if (isFocused()) {
                    int keyCode = e.getKeyCode();

                    switch (keyCode) {
                        // Move the caret to the first position on the left:
                        case KeyEvent.VK_HOME: {
                            if (homeKeyEnabled) {
                                changeCaretPosition(0);
                                transmitDraw();
                            }
                            break;
                        }

                        // Move the caret to the last position on the right:
                        case KeyEvent.VK_END: {
                            if (endKeyEnabled) {
                                changeCaretPosition(maxCharacters);
                                transmitDraw();
                            }
                            break;
                        }

                        // Erase the current character:
                        case KeyEvent.VK_DELETE: {
                            if (deleteKeyEnabled) {
                                changeCharacter(index_caret, ' ');
                                transmitDraw();
                            }
                            break;
                        }

                        // Move the caret one position to the left:
                        case KeyEvent.VK_LEFT: {
                            boolean canWork = leftArrowKeyEnabled;
                            canWork &= index_caret > 0;

                            if (canWork) {
                                changeCaretPosition(index_caret - 1);
                                transmitDraw();
                            }
                            break;
                        }

                        // Move the caret one position to the right:
                        case KeyEvent.VK_RIGHT: {
                            boolean canWork = rightArrowKeyEnabled;
                            canWork &= index_caret < maxCharacters;

                            if (canWork) {
                                changeCaretPosition(index_caret + 1);
                                transmitDraw();
                            }
                            break;
                        }

                        // Delete the character to the left of the caret, then move the caret one position left:
                        case KeyEvent.VK_BACK_SPACE: {
                            boolean canWork = backSpaceKeyEnabled;
                            canWork &= index_caret > 0;

                            if (! canWork) {
                                break;
                            }

                            if (index_caret == maxCharacters - 1) {
                                final AsciiCharacter currentChar = AsciiTextField.super.strings[0].getCharacters()[index_caret];

                                if (currentChar.getCharacter() != ' ') {
                                    changeCharacter(index_caret, ' ');
                                    break;
                                }
                            }

                            changeCharacter(index_caret - 1, ' ');
                            changeCaretPosition(index_caret - 1);
                            transmitDraw();
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * Moves the caret to the specified index.
     *
     * @param newIndex
     *         The new index for the caret.
     */
    private void changeCaretPosition(int newIndex) {
        if (newIndex >= maxCharacters) {
            newIndex = maxCharacters - 1;
        }

        if (newIndex < 0) {
            newIndex = 0;
        }

        final AsciiCharacter[] characters = super.strings[0].getCharacters();

        characters[index_caret].setForegroundColor(foregroundColor);
        characters[index_caret].setBackgroundColor(backgroundColor);

        characters[newIndex].setForegroundColor(caretForegroundColor);
        characters[newIndex].setBackgroundColor(caretBackgroundColor);

        final Radio<String> radio = super.getRadio();

        if (radio != null) {
            characters[index_caret].disableBlinkEffect();
            characters[index_caret].setHidden(false);
            characters[newIndex].enableBlinkEffect((short) 1000, radio);
        }

        index_caret = newIndex;
    }

    /**
     * Changes the character at the specified index to the specified character.
     *
     * @param characterIndex
     *         The index.
     *
     * @param character
     *         The new character.
     */
    private void changeCharacter(final int characterIndex, final char character) {
        if (characterIndex < 0 || characterIndex > maxCharacters) {
            return;
        }

        final AsciiCharacter[] characters = super.getStrings()[0].getCharacters();
        characters[characterIndex].setCharacter(character);
    }
}