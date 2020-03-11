package ru.chsergeig.fb2reader.elements;

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class TextFlowContainer extends TextFlow {

    private TextAlignment alignment = TextAlignment.LEFT;

    public TextFlowContainer addTab() {
        this.getChildren().add(new Text("    "));
        return this;
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    public TextAlignment getAlignment() {
        return alignment;
    }
}
