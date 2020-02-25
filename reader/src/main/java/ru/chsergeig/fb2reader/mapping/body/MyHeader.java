package ru.chsergeig.fb2reader.mapping.body;

import javafx.scene.Node;
import org.jsoup.nodes.Element;
import ru.chsergeig.fb2reader.util.TextUtils;

import java.util.List;

public class MyHeader {

    private List<Node> texts;

    public MyHeader(Element element) {
        texts = TextUtils.toTexts(element);
    }

    public List<Node> getTexts() {
        return texts;
    }

}
