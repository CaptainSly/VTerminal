package com.valkryst.VTerminal.samples;

import com.valkryst.VTerminal.Panel;
import com.valkryst.VTerminal.builder.PanelBuilder;
import com.valkryst.VTerminal.font.Font;
import com.valkryst.VTerminal.font.FontLoader;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;

public class SampleDrawTime {
    public static void main(final String[] args) throws IOException, URISyntaxException, InterruptedException {
        final Font font = FontLoader.loadFontFromJar("Fonts/DejaVu Sans Mono/18pt/bitmap.png", "Fonts/DejaVu Sans Mono/18pt/data.fnt", 1);
        final Panel panel = new PanelBuilder().setFont(font).build();

        int temp = 45;

        long total = 1;
        long counter = 1;

        while(true) {
            panel.getScreen().clear((char)temp);
            panel.getScreen().setForegroundColor(new Color(255, 155, temp, 255));
            panel.getScreen().setBackgroundColor(new Color(temp, 155, 255, 255));

            temp++;

            if (temp > 55) {
                temp = 45;
            }

            final long bef = System.currentTimeMillis();
            panel.draw();
            final long res = System.currentTimeMillis() - bef;
            counter++;
            total += res;

            if (temp == 45) {
                System.out.println("Draw Took:\t" + res + "ms\t\tAvg Is:\t" + (total / counter) + "ms");
            }

            Thread.sleep(100);
        }
    }
}
