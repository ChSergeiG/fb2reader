package ru.chsergeig.fb2reader.misc;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jodd.jerry.Jerry;
import jodd.util.Base64;
import ru.chsergeig.fb2reader.BookHolder;
import ru.chsergeig.fb2reader.mapping.fictionbook.FictionBook;
import ru.chsergeig.fb2reader.util.TextUtils;
import ru.chsergeig.fb2reader.util.enumeration.TextFontProvider;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;

import static ru.chsergeig.fb2reader.util.TextUtils.jerryToString;
import static ru.chsergeig.fb2reader.util.TextUtils.jerryToTexts;
import static ru.chsergeig.fb2reader.util.Utils.safeExecute;
import static ru.chsergeig.fb2reader.util.Utils.safeExtractValue;
import static ru.chsergeig.fb2reader.util.enumeration.InCaseOfFail.THROW_EXCEPTION;

public class BookContainer {

    private final BookContainer parent;
    private final Jerry element;
    private final List<BookContainer> children = new LinkedList<>();
    private List<javafx.scene.Node> content;
    private String title;

    public BookContainer(Jerry root) {
        this(null, root);
    }

    private BookContainer(BookContainer parent, Jerry elementToContain) {
        this.parent = parent;
        this.element = elementToContain;
        StreamSupport
                .stream(element.children().spliterator(), false)
                .filter(node -> node.get(0).getNodeName().equals("section"))
                .forEach(node -> children.add(new BookContainer(this, node)));
    }

    public static void showCover(TextFlow main) {
        Platform.runLater(() -> {
            ObservableList<Node> nodes = main.getChildren();
            FictionBook book = BookHolder.getFictionBook();
            nodes.clear();
            safeExecute(() -> {
                Text title = new Text(book.getBookTitle());
                title.setFont(TextFontProvider.TITLE.getFont());
                nodes.add(title);
                TextUtils.addEmptyLine(nodes);
            }, THROW_EXCEPTION);
            safeExecute(() -> {
                nodes.addAll(book.getMyHeader().getTexts());
                TextUtils.addEmptyLine(nodes);
            }, THROW_EXCEPTION);
            safeExecute(() -> {
                if (null != book.getCoverpage()) {
                    TextUtils.addEmptyLine(nodes);
                    ImageView image = new ImageView();
                    image.setImage(new Image(new ByteArrayInputStream(Base64.decode(book.getCoverpage().getValue()))));
                    image.setFitHeight(400.0d);
                    image.setPreserveRatio(true);
                    nodes.add(image);
                    TextUtils.addEmptyLine(nodes);
                }
            }, THROW_EXCEPTION);
            safeExecute(() -> {
                nodes.addAll(book.getMyFooter().getTexts());
                TextUtils.addEmptyLine(nodes);
            }, THROW_EXCEPTION);
        });
    }

    public List<BookContainer> getChildren() {
        return children;
    }

    public List<javafx.scene.Node> getContent() {
        if (null == content) {
            content = new LinkedList<>();
            content.addAll(jerryToTexts(element));
        }
        return content;
    }

    public String getTitle() {
        if (null == title) {
            title = safeExtractValue(() -> jerryToString(StreamSupport.stream(element.children().spliterator(), false)
                    .filter(node -> node.get(0).getNodeName().equals("title"))
                    .findFirst()
                    .get()), "---------");
        }
        return title;
    }

    public BookContainer getParent() {
        return parent;
    }

}
