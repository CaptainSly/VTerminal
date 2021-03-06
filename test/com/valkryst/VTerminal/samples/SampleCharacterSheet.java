package com.valkryst.VTerminal.samples;

import com.valkryst.VTerminal.AsciiCharacter;
import com.valkryst.VTerminal.AsciiString;
import com.valkryst.VTerminal.Panel;
import com.valkryst.VTerminal.builder.PanelBuilder;
import com.valkryst.VTerminal.font.Font;
import com.valkryst.VTerminal.font.FontLoader;

import java.io.IOException;
import java.net.URISyntaxException;

public class SampleCharacterSheet {
    public static void main(final String[] args) throws IOException, URISyntaxException, InterruptedException {
        final Font font = FontLoader.loadFontFromJar("Fonts/DejaVu Sans Mono/16pt/bitmap.png", "Fonts/DejaVu Sans Mono/16pt/data.fnt", 1);
        final Panel panel = new PanelBuilder().setFont(font).setWidthInCharacters(160).setHeightInCharacters(50).build();

        Thread.sleep(50);

        char counter = 33;

        for (int y = 0 ; y < panel.getHeightInCharacters() ; y++) {
            final AsciiString string = panel.getScreen().getString(y);
            final AsciiCharacter[] characters = string.getCharacters();

            for (int x = 0 ; x < panel.getWidthInCharacters() ; x++) {
                characters[x].setCharacter(counter);
                counter++;
            }
        }

        panel.draw();
    }
}
