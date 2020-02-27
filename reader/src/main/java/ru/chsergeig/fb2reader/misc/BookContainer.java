package ru.chsergeig.fb2reader.misc;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jsoup.nodes.Element;
import ru.chsergeig.fb2reader.BookHolder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BookContainer {

    private final BookContainer parent;
    private final Element element;
    private final List<BookContainer> children = new LinkedList<>();
    private final List<javafx.scene.Node> content = new LinkedList<>();
    private String title;


    public BookContainer(Element root) {
        this(null, root);
    }

    private BookContainer(BookContainer parent, Element elementToContain) {
        this.parent = parent;
        this.element = elementToContain;
        createContent();
        elementToContain.children().stream()
                .filter(node -> node.tagName().equals("section"))
                .map(section -> new BookContainer(this, section))
                .forEach(children::add);
    }


    private void createContent() {
        Element titleNode = element.children().stream()
                .filter(node -> node.tagName().equals("title"))
                .findAny()
                .orElse(null);
        if (null != titleNode) {
            title = titleNode.text();
        } else {
            title = "---------";
        }
        content.addAll(toTexts(titleNode));
        element.children().stream()
                .filter(node -> node.tagName().equals("title"))
                .findAny()
                .orElse(null);
        content.addAll(toTexts(element));
    }

    public List<BookContainer> getChildren() {
        return children;
    }

    public List<javafx.scene.Node> getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    private List<javafx.scene.Node> toTexts(Element element) {
        if (null == element) {
            return Collections.emptyList();
        }
        Text text = new Text(element.html());
        text.setFont(Font.font("Consolas", BookHolder.fontSize));
        return Collections.singletonList(text);

    }

}
