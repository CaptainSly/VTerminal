package com.valkryst.VTerminal.builder.component;

import com.valkryst.VTerminal.component.TextField;
import com.valkryst.radio.Radio;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.regex.Pattern;

public class TextFieldBuilder extends ComponentBuilder<TextField, TextFieldBuilder> {
    /** The width of the text field, in characters. */
    @Getter private int width;

    /** The radio to transmit events to. */
    @Getter private Radio<String> radio;

    /** The foreground color of the caret. */
    @Getter private Color caretForegroundColor;
    /** The background color of the caret. */
    @Getter private Color caretBackgroundColor;

    /** The foreground color of non-caret characters. */
    @Getter private Color foregroundColor;
    /** The background color of non-caret characters. */
    @Getter private Color backgroundColor;

    /** Whether or not the HOME key can be used to move the caret to the first index of the field. */
    @Getter private boolean homeKeyEnabled;
    /** Whether or not the END key can be used to move the caret to the last index of the field. */
    @Getter private boolean endKeyEnabled;
    /** Whether or not the DELETE key can be used to erase the character that the caret is on. */
    @Getter private boolean deleteKeyEnabled;
    /** Whether or not the LEFT ARROW key can be used to move the caret one index to the left. */
    @Getter private boolean leftArrowKeyEnabled;
    /** Whether or not the RIGHT ARROW key can be used to move the caret one index to the right. */
    @Getter private boolean rightArrowKeyEnabled;
    /** Whether or not the BACK SPACE key can be used to erase the character before the caret and move the caret backwards. */
    @Getter private boolean backSpaceKeyEnabled;

    /** The pattern used to determine which typed characters can be entered into the field. */
    @Getter @Setter private Pattern allowedCharacterPattern;

    @Override
    public TextField build() throws IllegalStateException {
        checkState();

        final TextField textField = new TextField(this);
        textField.registerEventHandlers(super.panel);
        super.panel.addComponent(textField);

        return textField;
    }

    /**
     * Checks the current state of the builder.
     *
     * @throws IllegalStateException
     *          If something is wrong with the builder's state.
     */
    protected void checkState() throws IllegalStateException {
        super.checkState();

        if (radio == null) {
            throw new IllegalStateException("The button must have a radio to transmit to.");
        }
    }

    /** Resets the builder to it's default state. */
    public void reset() {
        super.reset();

        width = 1;

        radio = null;

        caretForegroundColor = new Color(0x21B6A8);
        caretBackgroundColor = new Color(0x52F2EA);

        foregroundColor = new Color(0x52F2EA);
        backgroundColor = new Color(0x21B6A8);

        homeKeyEnabled = true;
        endKeyEnabled = true;
        deleteKeyEnabled = true;
        leftArrowKeyEnabled = true;
        rightArrowKeyEnabled = true;
        backSpaceKeyEnabled = true;

        allowedCharacterPattern = Pattern.compile("^[a-zA-z0-9$-/:-?{-~!\"^_`\\[\\]@# ]$");
    }

    public TextFieldBuilder setWidth(final int width) {
        if (width >= 1) {
            this.width = width;
        }

        return this;
    }

    public TextFieldBuilder setRadio(final Radio<String> radio) {
        if (radio != null) {
            this.radio = radio;
        }

        return this;
    }

    public TextFieldBuilder setCaretForegroundColor(final Color caretForegroundColor) {
        if (caretForegroundColor != null) {
            this.caretForegroundColor = caretForegroundColor;
        }

        return this;
    }

    public TextFieldBuilder setCaretBackgroundColor(final Color caretBackgroundColor) {
        if (caretBackgroundColor != null) {
            this.caretBackgroundColor = caretBackgroundColor;
        }

        return this;
    }

    public TextFieldBuilder setForegroundColor(final Color foregroundColor) {
        if (foregroundColor != null) {
            this.foregroundColor = foregroundColor;
        }

        return this;
    }

    public TextFieldBuilder setBackgroundColor(final Color backgroundColor) {
        if (backgroundColor != null) {
            this.backgroundColor = backgroundColor;
        }

        return this;
    }

    public TextFieldBuilder setHomeKeyEnabled(final boolean homeKeyEnabled) {
        this.homeKeyEnabled = homeKeyEnabled;
        return this;
    }

    public TextFieldBuilder setEndKeyEnabled(final boolean endKeyEnabled) {
        this.endKeyEnabled = endKeyEnabled;
        return this;
    }

    public TextFieldBuilder setDeleteKeyEnabled(final boolean deleteKeyEnabled) {
        this.deleteKeyEnabled = deleteKeyEnabled;
        return this;
    }

    public TextFieldBuilder setLeftArrowKeyEnabled(final boolean leftArrowKeyEnabled) {
        this.leftArrowKeyEnabled = leftArrowKeyEnabled;
        return this;
    }

    public TextFieldBuilder setRightArrowKeyEnabled(final boolean rightArrowKeyEnabled) {
        this.rightArrowKeyEnabled = rightArrowKeyEnabled;
        return this;
    }

    public TextFieldBuilder setBackSpaceKeyEnabled(final boolean backSpaceKeyEnabled) {
        this.backSpaceKeyEnabled = backSpaceKeyEnabled;
        return this;
    }

    public TextFieldBuilder setAllowedCharacterPattern(final Pattern allowedCharacterPattern) {
        if (allowedCharacterPattern != null) {
            this.allowedCharacterPattern = allowedCharacterPattern;
        }

        return this;
    }
}