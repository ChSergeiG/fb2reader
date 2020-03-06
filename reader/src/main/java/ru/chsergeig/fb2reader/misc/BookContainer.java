package ru.chsergeig.fb2reader.misc;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import jodd.jerry.Jerry;
import ru.chsergeig.fb2reader.BookHolder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;

import static ru.chsergeig.fb2reader.util.Utils.safeExtractValue;

public class BookContainer {

    private final BookContainer parent;
    private final Jerry element;
    private final List<BookContainer> children = new LinkedList<>();
    private final List<javafx.scene.Node> content = new LinkedList<>();
    private String title;


    public BookContainer(Jerry root) {
        this(null, root);
    }

    private BookContainer(BookContainer parent, Jerry elementToContain) {
        this.parent = parent;
        this.element = elementToContain;
        createContent();
        StreamSupport
                .stream(element.children().spliterator(), false)
                .filter(node -> node.get(0).getNodeName().equals("section"))
                .forEach(node -> children.add(new BookContainer(this, node)));
    }


    private void createContent() {
        title = safeExtractValue(() -> StreamSupport.stream(element.children().spliterator(), false)
                .filter(node -> node.get(0).getNodeName().equals("title"))
                .findFirst()
                .get()
                .text(), "---------");

        content.addAll(toText(title));
//        element.children().stream()
//                .filter(node -> node.tagName().equals("title"))
//                .findAny()
//                .orElse(null);
//        content.addAll(toTexts(element));
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

    private List<javafx.scene.Node> toTexts(Jerry element) {
        if (null == element) {
            return Collections.emptyList();
        }
        Text text = new Text(element.html());
        text.setFont(Font.font("Consolas", BookHolder.fontSize));
        return Collections.singletonList(text);

    }

    private List<javafx.scene.Node> toText(String element) {
        Text text = new Text(element);
        text.setFont(Font.font("Consolas", BookHolder.fontSize));
        return Collections.singletonList(text);
    }

}
