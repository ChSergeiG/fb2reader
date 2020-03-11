package ru.chsergeig.fb2reader.elements;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.util.List;
import java.util.stream.Collectors;

public class MainFlowPane extends FlowPane {

    private Button previous;
    private Button next;

    private int position;

    public void init(Button previous, Button next, int position) {
        this.previous = previous;
        this.next = next;
        this.position = position;
    }

    public void showParagraphs(List<TextFlowContainer> paragraphs) {
        double width = this.prefWidthProperty().add(-20).get();
        ObservableList<Node> nodes = this.getChildren();
        nodes.clear();
        nodes.addAll(paragraphs.stream().peek(paragraph -> {
            paragraph.setMinWidth(width);
            paragraph.setPrefWidth(width);
            paragraph.setMaxWidth(width);
            paragraph.setTextAlignment(paragraph.getAlignment());
        }).collect(Collectors.toList()));
    }

}
