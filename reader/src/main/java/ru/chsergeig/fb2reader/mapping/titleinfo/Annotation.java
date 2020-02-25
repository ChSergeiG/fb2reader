package ru.chsergeig.fb2reader.mapping.titleinfo;

import javafx.scene.Node;
import org.jsoup.nodes.Element;
import ru.chsergeig.fb2reader.util.TextUtils;

import java.util.List;

public class Annotation {

    private List<Node> texts;

    public Annotation(Element element) {
        texts = TextUtils.toTexts(element);
    }

    public List<Node> getTexts() {
        return texts;
    }
}
