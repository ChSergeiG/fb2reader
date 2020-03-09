package ru.chsergeig.fb2reader.util.enumeration;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.Stack;

public enum TextFontProvider {
    TITLE("Helvetica", FontWeight.BOLD, null, 1.5d),
    NORMAL("Helvetica", null, null, 1.0d),
    ITALIC("Helvetica", null, FontPosture.ITALIC, 1.0d),
    BOLD("Helvetica", FontWeight.BOLD, null, 1.0d),
    BOLD_ITALIC("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 1.0d),
    CITE_NORMAL("Consolas", null, null, 1.0d),
    CITE_ITALIC("Consolas", null, FontPosture.ITALIC, 1.0d),
    CITE_BOLD("Consolas", FontWeight.BOLD, null, 1.0d),
    CITE_BOLD_ITALIC("Consolas", FontWeight.BOLD, FontPosture.ITALIC, 1.0d),
    TITLE_NORMAL("Helvetica", null, null, 1.5d),
    TITLE_ITALIC("Helvetica", null, FontPosture.ITALIC, 1.5d),
    TITLE_BOLD("Helvetica", FontWeight.BOLD, null, 1.5d),
    TITLE_BOLD_ITALIC("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 1.5d),
    TITLE_CITE_NORMAL("Consolas", null, null, 1.5d),
    TITLE_CITE_ITALIC("Consolas", null, FontPosture.ITALIC, 1.5d),
    TITLE_CITE_BOLD("Consolas", FontWeight.BOLD, null, 1.5d),
    TITLE_CITE_BOLD_ITALIC("Consolas", FontWeight.BOLD, FontPosture.ITALIC, 1.5d),
    SUBTITLE_NORMAL("Helvetica", null, null, 1.2d),
    SUBTITLE_ITALIC("Helvetica", null, FontPosture.ITALIC, 1.2d),
    SUBTITLE_BOLD("Helvetica", FontWeight.BOLD, null, 1.2d),
    SUBTITLE_BOLD_ITALIC("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 1.2d),
    SUBTITLE_CITE_NORMAL("Consolas", null, null, 1.2d),
    SUBTITLE_CITE_ITALIC("Consolas", null, FontPosture.ITALIC, 1.2d),
    SUBTITLE_CITE_BOLD("Consolas", FontWeight.BOLD, null, 1.2d),
    SUBTITLE_CITE_BOLD_ITALIC("Consolas", FontWeight.BOLD, FontPosture.ITALIC, 1.2d),
    ;

    private static Double fontSize = 20d;

    private final String fontFamily;
    private final FontWeight weight;
    private final FontPosture posture;
    private final Double multiplier;

    TextFontProvider(String fontFamily, FontWeight weight, FontPosture posture, Double multiplier) {
        this.weight = weight;
        this.posture = posture;
        this.multiplier = multiplier;
        this.fontFamily = fontFamily;
    }

    public static void setFontSize(Double fontSize) {
        TextFontProvider.fontSize = fontSize;
    }

    public static TextFontProvider of(Stack<TextModifiers> modifiers) {
        int modifier =
                (modifiers.contains(TextModifiers.BOLD) ? 0b1 : 0)
                        + (modifiers.contains(TextModifiers.ITALIC) ? 0b10 : 0)
                        + (modifiers.contains(TextModifiers.CITE) ? 0b100 : 0)
                        + (modifiers.contains(TextModifiers.TITLE) ? 0b1000 : 0)
                        + (modifiers.contains(TextModifiers.SUBTITLE) ? 0b10000 : 0);
        switch (modifier) {
            case 0b00000:
                return NORMAL;
            case 0b00001:
                return BOLD;
            case 0b00010:
                return ITALIC;
            case 0b00011:
                return BOLD_ITALIC;
            case 0b00100:
                return CITE_NORMAL;
            case 0b00101:
                return CITE_BOLD;
            case 0b00110:
                return CITE_ITALIC;
            case 0b00111:
                return CITE_BOLD_ITALIC;
            case 0b01000:
                return TITLE_NORMAL;
            case 0b01001:
                return TITLE_BOLD;
            case 0b01010:
                return TITLE_ITALIC;
            case 0b01011:
                return TITLE_BOLD_ITALIC;
            case 0b01100:
                return TITLE_CITE_NORMAL;
            case 0b01101:
                return TITLE_CITE_BOLD;
            case 0b01110:
                return TITLE_CITE_ITALIC;
            case 0b01111:
                return TITLE_CITE_BOLD_ITALIC;
            case 0b10000:
                return SUBTITLE_NORMAL;
            case 0b10001:
                return SUBTITLE_BOLD;
            case 0b10010:
                return SUBTITLE_ITALIC;
            case 0b10011:
                return SUBTITLE_BOLD_ITALIC;
            case 0b10100:
                return SUBTITLE_CITE_NORMAL;
            case 0b10101:
                return SUBTITLE_CITE_BOLD;
            case 0b10110:
                return SUBTITLE_CITE_ITALIC;
            case 0b10111:
                return SUBTITLE_CITE_BOLD_ITALIC;
        }
        return NORMAL;
    }

    public Font getFont() {
        return Font.font(fontFamily, weight, posture, fontSize * multiplier);
    }

}
