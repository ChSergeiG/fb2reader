package ru.chsergeig.fb2reader.mapping.documentinfo;

import javafx.scene.Node;
import org.jsoup.select.Elements;
import ru.chsergeig.fb2reader.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class History {

    private List<Node> texts = new ArrayList<>();

    public History(Elements elements) {
        elements.stream().map(TextUtils::toTexts).forEach(texts::addAll);
    }

    public List<Node> getTexts() {
        return texts;
    }

}
