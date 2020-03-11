package ru.chsergeig.fb2reader.misc;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import jodd.jerry.Jerry;
import jodd.util.Base64;
import ru.chsergeig.fb2reader.BookHolder;
import ru.chsergeig.fb2reader.elements.MainFlowPane;
import ru.chsergeig.fb2reader.elements.TextFlowContainer;
import ru.chsergeig.fb2reader.mapping.fictionbook.FictionBook;
import ru.chsergeig.fb2reader.util.enumeration.TextFontProvider;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;

import static ru.chsergeig.fb2reader.util.TextUtils.jerryToParagraphs;
import static ru.chsergeig.fb2reader.util.TextUtils.jerryToString;
import static ru.chsergeig.fb2reader.util.Utils.safeExecute;
import static ru.chsergeig.fb2reader.util.Utils.safeExtractValue;
import static ru.chsergeig.fb2reader.util.enumeration.InCaseOfFail.IGNORE;

public class BookContainer {

    private final BookContainer parent;
    private final Jerry element;
    private final List<BookContainer> children = new LinkedList<>();
    private List<TextFlowContainer> content;
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

    public static void showCover(MainFlowPane pane) {
        Platform.runLater(() -> {
            List<TextFlowContainer> containers = new ArrayList<>();
            FictionBook book = BookHolder.getFictionBook();
            safeExecute(() -> {
                Text title = new Text(book.getBookTitle());
                title.setFont(TextFontProvider.TITLE_BOLD.getFont());
                TextFlowContainer titleContainer = new TextFlowContainer();
                titleContainer.setAlignment(TextAlignment.CENTER);
                titleContainer.getChildren().add(title);
                containers.add(titleContainer);
            }, IGNORE);
            safeExecute(() -> {
                containers.addAll(book.getMyHeader().getTexts());
            }, IGNORE);
            safeExecute(() -> {
                if (null != book.getCoverpage()) {
                    ImageView image = new ImageView();
                    Image raster = new Image(new ByteArrayInputStream(Base64.decode(book.getCoverpage().getValue())));
                    if (raster.getHeight() > 400d) {
                        image.setFitHeight(400d);
                        image.setPreserveRatio(true);
                    }
                    if (raster.getWidth() > 400d) {
                        image.setFitWidth(400d);
                        image.setPreserveRatio(true);
                    }
                    image.setImage(raster);
                    image.setFitHeight(400.0d);
                    TextFlowContainer imgContainer = new TextFlowContainer();
                    imgContainer.getChildren().add(image);
                    imgContainer.setAlignment(TextAlignment.CENTER);
                    containers.add(imgContainer);
                }
            }, IGNORE);
            safeExecute(() -> {
                containers.addAll(book.getMyFooter().getTexts());
            }, IGNORE);
            pane.showParagraphs(containers);
        });
    }

    public List<BookContainer> getChildren() {
        return children;
    }

    public List<TextFlowContainer> getContent() {
        if (null == content) {
            content = new LinkedList<>();
            content.addAll(jerryToParagraphs(element));
        }
        return content;
    }

    public String getTitle() {
        if (null == title) {
            String rawTitle = safeExtractValue(
                    () -> jerryToString(
                            StreamSupport.stream(element.children().spliterator(), false)
                                    .filter(node -> node.get(0).getNodeName().equals("title"))
                                    .findFirst()
                                    .get()),
                    safeExtractValue(
                            () -> element.get(0).getChildNodes()[0].getNodeName(),
                            "------"));
            this.title = String.join(". ", rawTitle.trim().split("\\s*\n\\s*"));
        }
        return title;
    }

    public BookContainer getParent() {
        return parent;
    }

}
