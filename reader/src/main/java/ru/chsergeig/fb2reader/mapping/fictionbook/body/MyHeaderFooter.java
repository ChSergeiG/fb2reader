package ru.chsergeig.fb2reader.mapping.fictionbook.body;

import javafx.scene.Node;
import jodd.jerry.Jerry;

import java.util.List;

import static ru.chsergeig.fb2reader.util.TextUtils.jerryToTexts;

public class MyHeaderFooter {

    private Jerry element;

    public MyHeaderFooter(Jerry element) {
        this.element = element;
    }

    public List<Node> getTexts() {
        return jerryToTexts(element);
    }

}
