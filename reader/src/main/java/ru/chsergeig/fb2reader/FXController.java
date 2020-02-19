package ru.chsergeig.fb2reader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;


public class FXController {

    @FXML
    public Button button;

    @FXML
    public TextFlow main;

    @FXML
    private void handleButtonPressed(ActionEvent event) {

        main.getChildren().addAll(toTexts(StructureSupplier.ANNOTATION.getFirstElement()));
    }

    private List<Text> toTexts(Element element) {
        List<Text> result = new ArrayList<>();
        toTexts(element, result);
        return result;
    }

    private void toTexts(Element element, List<Text> texts) {
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                Text text = new Text(((TextNode) node).text());
                text.setFont(Font.font("Helvetica", 15));
                texts.add(text);
            } else if (node instanceof Element) {
                if (((Element) node).tagName().equals("i")) {
                    Text text = new Text(((Element) node).text());
                    text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
                    texts.add(text);
                } else {
                    toTexts((Element) node, texts);
                }
            }
        }
    }


}
