package com.valkryst.VTerminal;

import com.valkryst.VTerminal.font.Font;
import com.valkryst.radio.Radio;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;


public class AsciiCharacter {
    /** The character. */
	@Getter @Setter private char character;
	/** Whether or not the foreground should be drawn using the background color. */
	@Getter @Setter private boolean isHidden = false;
    /** The background color. Defaults to black. */
    @Getter private Color backgroundColor = Color.BLACK;
	/** The foreground color. Defaults to white. */
	@Getter private Color foregroundColor = Color.WHITE;
	/** The bounding box of the character's area. */
	@Getter private final Rectangle boundingBox = new Rectangle();

	/** Whether or not to draw the character as underlined. */
	@Getter @Setter private boolean isUnderlined = false;
    /** The thickness of the underline to draw beneath the character. */
	@Getter private byte underlineThickness = 2;

	/** Whether or not the character should be flipped horizontally when drawn. */
	@Getter @Setter private boolean isFlippedHorizontally = false;
	/** Whether or not the character should be flipped vertically when drawn. */
	@Getter @Setter private boolean isFlippedVertically = false;

	private Timer blinkTimer;
	/** The amount of time, in milliseconds, before the blink effect can occur. */
	private short millsBetweenBlinks = 0;

    /**
     * Constructs a new AsciiCharacter.
     *
     * @param character
     *         The character.
     */
	public AsciiCharacter(final char character) {
	    this.character = character;
    }

    @Override
    public String toString() {
        return "Character:\n" +
                "\tCharacter: '" + character + "\n" +
                "\tBackground Color: " + backgroundColor + "\n" +
                "\tForeground Color: " + foregroundColor + "\n";
    }

    @Override
    public boolean equals(final Object object) {
	    if (object instanceof AsciiCharacter == false) {
	        return false;
        }

        final AsciiCharacter otherCharacter = (AsciiCharacter) object;

	    if (character != otherCharacter.character) {
	        return false;
        }

        if (backgroundColor.equals(otherCharacter.backgroundColor) == false) {
            return false;
        }

        if (foregroundColor.equals(otherCharacter.foregroundColor) == false) {
	        return false;
        }

        return true;
    }

    /**
     * Draws the character onto the specified context.
     *
     * @param gc
     *         The context on which to draw.
     *
     * @param font
     *         The font to draw with.
     *
     * @param columnIndex
     *         The x-axis (column) coordinate where the character is to be drawn.
     *
     * @param rowIndex
     *         The y-axis (row) coordinate where the character is to be drawn.
     */
    public void draw(final Graphics gc, final Font font, int columnIndex, int rowIndex) {
        BufferedImage bufferedImage = font.getCharacterImages().get(character);

        // Handle Horizontal/Vertical Flipping:
        if (isFlippedHorizontally || isFlippedVertically) {
            AffineTransform tx;

            if (isFlippedHorizontally && isFlippedVertically) {
                tx = AffineTransform.getScaleInstance(-1, -1);
                tx.translate(-bufferedImage.getWidth(), -bufferedImage.getHeight());
            } else if (isFlippedHorizontally) {
                tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-bufferedImage.getWidth(), 0);
            } else  {
                tx = AffineTransform.getScaleInstance(1, -1);
                tx.translate(0, -bufferedImage.getHeight());
            }

            final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = op.filter(bufferedImage, null);
        }

        // Retrieve character image & set colors:
        Image image = bufferedImage;

        final ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                if (isHidden) {
                    return backgroundColor.getRGB();
                }

                if (rgb == 0xFFFFFFFF) {
                    return foregroundColor.getRGB();
                } else {
                    return backgroundColor.getRGB();
                }
            }
        };

        image = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), filter));

        // Draw character:
	    final int fontWidth = font.getWidth();
	    final int fontHeight = font.getHeight();

	    columnIndex *= fontWidth;
	    rowIndex *= fontHeight;

        gc.drawImage(image, columnIndex, rowIndex,null);

        boundingBox.setLocation(columnIndex, rowIndex);
        boundingBox.setSize(fontWidth, fontHeight);

        // Draw underline:
        if (isUnderlined) {
            gc.setColor(foregroundColor);

            final int y = rowIndex + fontHeight - underlineThickness;
            gc.fillRect(columnIndex, y, fontWidth, underlineThickness);
        }
    }

    /**
     * Enables the blink effect.
     *
     * @param millsBetweenBlinks
     *         The amount of time, in milliseconds, before the blink effect can occur.
     *
     * @param radio
     *         The Radio to transmit a DRAW event to whenever a blink occurs.
     */
    public void enableBlinkEffect(final short millsBetweenBlinks, final Radio<String> radio) {
        if (millsBetweenBlinks <= 0) {
            this.millsBetweenBlinks = 1000;
        } else {
            this.millsBetweenBlinks = millsBetweenBlinks;
        }

        blinkTimer = new Timer(this.millsBetweenBlinks, e -> {
            isHidden = !isHidden;
            radio.transmit("DRAW");
        });
        blinkTimer.setInitialDelay(this.millsBetweenBlinks);
        blinkTimer.setRepeats(true);
        blinkTimer.start();
    }

    /** Resumes the blink effect. */
    public void resumeBlinkEffect() {
        if (blinkTimer != null) {
            if (blinkTimer.isRunning() == false) {
                blinkTimer.start();
            }
        }
    }

    /** Pauses the blink effect. */
    public void pauseBlinkEffect() {
        if (blinkTimer != null) {
            if (blinkTimer.isRunning()) {
                isHidden = false;
                blinkTimer.stop();
            }
        }
    }

    /** Disables the blink effect. */
    public void disableBlinkEffect() {
        this.millsBetweenBlinks = 0;

        if (blinkTimer != null) {
            blinkTimer.stop();
            blinkTimer = null;
        }
    }

    /** Swaps the background and foreground colors. */
    public void invertColors() {
        final Color temp = backgroundColor;
        setBackgroundColor(foregroundColor);
        setForegroundColor(temp);
    }

    /**
     * Sets the new background color.
     *
     * Does nothing if the specified color is null.
     *
     * @param color
     *         The new background color.
     */
    public void setBackgroundColor(final Color color) {
	    boolean canProceed = color != null;
	    canProceed &= backgroundColor.equals(color) == false;

	    if (canProceed) {
            this.backgroundColor = color;
        }
    }

    /**
     * Sets the new foreground color.
     *
     * Does nothing if the specified color is null.
     *
     * @param color
     *         The new foreground color.
     */
    public void setForegroundColor(final Color color) {
	    boolean canProceed = color != null;
	    canProceed &= foregroundColor.equals(color) == false;

	    if (canProceed) {
	        this.foregroundColor = color;
        }
    }

    /**
     * Sets the new underline thickness.
     *
     * If the specified thickness is negative, then the thickness is set to 1.
     * If the specified thickness is greater than the font height, then the thickness is set to the font height.
     * If the font height is greater than Byte.MAX_VALUE, then the thickness is set to Byte.MAX_VALUE.
     *
     * @param underlineThickness
     *         The new underline thickness.
     */
    private void setUnderlineThickness(final byte underlineThickness) {
        if (underlineThickness > boundingBox.getHeight()) {
            final double maxHeight = boundingBox.getHeight();

            if (maxHeight > Byte.MAX_VALUE) {
                this.underlineThickness = Byte.MAX_VALUE;
            } else {
                this.underlineThickness = (byte) boundingBox.getHeight();
            }
        }

        if (underlineThickness <= 0) {
            this.underlineThickness = 1;
        } else {
            this.underlineThickness = underlineThickness;
        }
    }
}