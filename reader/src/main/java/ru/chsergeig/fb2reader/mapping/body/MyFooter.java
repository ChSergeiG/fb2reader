package ru.chsergeig.fb2reader.mapping.body;

import javafx.scene.Node;
import org.jsoup.nodes.Element;
import ru.chsergeig.fb2reader.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MyFooter {
    private List<Node> texts = new ArrayList<>();

    public MyFooter(Element element) {
//        texts = TextUtils.toTexts(element);
    }

    public List<Node> getTexts() {
        return texts;
    }
}
