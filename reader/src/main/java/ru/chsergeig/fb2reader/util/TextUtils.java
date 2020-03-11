package ru.chsergeig.fb2reader.util;

import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import jodd.jerry.Jerry;
import jodd.util.Base64;
import ru.chsergeig.fb2reader.BookHolder;
import ru.chsergeig.fb2reader.elements.TextFlowContainer;
import ru.chsergeig.fb2reader.util.enumeration.TextFontProvider;
import ru.chsergeig.fb2reader.util.enumeration.TextModifiers;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.chsergeig.fb2reader.util.Utils.safeExecute;
import static ru.chsergeig.fb2reader.util.Utils.safeExtractValue;
import static ru.chsergeig.fb2reader.util.enumeration.InCaseOfFail.IGNORE;

public class TextUtils {

    private static final String[] CHARSETS_TO_TEST = {"windows-1251", "utf-8"};

    private TextUtils() {
    }

    public static String getValidCharset(Path file) {
        for (String charset : CHARSETS_TO_TEST) {
            String line;
            try {
                BufferedReader bufferedReader = Files.newBufferedReader(file, Charset.forName(charset));
                line = bufferedReader.readLine();
            } catch (Exception e) {
                continue;
            }
            Pattern encoding = Pattern.compile("encoding\\s*=\\s*\"(.+?)\"");
            Matcher matcher = encoding.matcher(line);
            if (!matcher.find()) {
                continue;
            }
            return matcher.group(1);
        }
        throw new RuntimeException("Cant determine charset");
    }

    public static List<TextFlowContainer> jerryToParagraphs(Jerry jerry) {
        List<TextFlowContainer> result = new LinkedList<>();
        jerryToParagraphs(jerry.get(0), result, new Stack<>());
        return result;
    }

    public static String jerryToString(Jerry jerry) {
        List<String> result = new LinkedList<>();
        jerryToString(jerry.get(0), result);
        return String.join(" ", result)
                .replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<")
                .replaceAll("</?[\\w\\d=\":\\-\\s]*/?>", "");
    }

    // region privates

    private static void jerryToParagraphs(jodd.lagarto.dom.Node jerryNode, List<TextFlowContainer> containers, Stack<TextModifiers> modifiers) {
        TextFlowContainer container = null;
        switch (jerryNode.getNodeName()) {
            case "title":
                modifiers.push(TextModifiers.TITLE);
                container = new TextFlowContainer();
                container.setAlignment(TextAlignment.CENTER);
                break;
            case "subtitle":
                modifiers.push(TextModifiers.SUBTITLE);
                container = new TextFlowContainer().addTab();
                break;
            case "p":
                modifiers.push(TextModifiers.P);
                container = new TextFlowContainer().addTab();
                break;
            case "epigraph":
                modifiers.push(TextModifiers.CITE);
                break;
            case "text-author":
                modifiers.push(TextModifiers.ITALIC);
                container = new TextFlowContainer().addTab();
                break;
            case "img":
                modifiers.push(TextModifiers.NONE);
                container = new TextFlowContainer();
                container.setAlignment(TextAlignment.CENTER);
                break;
            default:
                modifiers.push(TextModifiers.NONE);
        }
        if (null == container) {
            for (jodd.lagarto.dom.Node childNode : jerryNode.getChildNodes()) {
                jerryToParagraphs(childNode, containers, modifiers);
            }
        } else {
            jerryAsParagraph(jerryNode, container, modifiers);
            containers.add(container);
        }
        modifiers.pop();
    }

    private static void jerryAsParagraph(jodd.lagarto.dom.Node jerryNode, TextFlowContainer container, Stack<TextModifiers> modifiers) {
        if (jerryNode instanceof jodd.lagarto.dom.Text) {
            container.getChildren().add(createElement(jerryNode, modifiers));
        } else {
            switch (jerryNode.getNodeName()) {
                case "empty-line":
                    Text text = new Text("\n");
                    text.setFont(TextFontProvider.of(modifiers).getFont());
                    container.getChildren().add(text);
                    return;
                case "a":
                    modifiers.push(TextModifiers.LINK);
                    break;
                case "strong":
                    modifiers.push(TextModifiers.BOLD);
                    break;
                default:
                    modifiers.push(TextModifiers.NONE);
            }
            for (jodd.lagarto.dom.Node childNode : jerryNode.getChildNodes()) {
                jerryAsParagraph(childNode, container, modifiers);
            }
            modifiers.pop();
        }
    }

    private static void jerryToString(jodd.lagarto.dom.Node jerryNode, List<String> appends) {
        if (null == jerryNode) {
            return;
        }
        String nodeValue = jerryNode.getNodeValue();
        if (null != nodeValue) {
            appends.add(nodeValue);
        }
        for (jodd.lagarto.dom.Node childNode : jerryNode.getChildNodes()) {
            jerryToString(childNode, appends);
        }
    }

    private static javafx.scene.Node createElement(jodd.lagarto.dom.Node jerryNode, Stack<TextModifiers> modifiers) {
        if (modifiers.contains(TextModifiers.LINK)) {
            Hyperlink hyperlink = new Hyperlink(jerryNode.getNodeValue());
            hyperlink.setFont(TextFontProvider.of(modifiers).getFont());
            try {
                URI link = new URI(extractHref(jerryNode));
                hyperlink.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    try {
                        Desktop.getDesktop().browse(link);
                    } catch (IOException ignore) {
                    }
                });
            } catch (URISyntaxException ignore) {
            }
            return hyperlink;
        } else if (modifiers.contains(TextModifiers.SUBTITLE) || modifiers.contains(TextModifiers.TITLE)) {
            Text text = new Text(jerryNode.getNodeValue()
                    .replaceAll("&gt;", ">")
                    .replaceAll("&lt;", "<")
                    .replaceAll("</?[\\w\\d=\":\\-\\s]*/?>", ""));
            text.setFont(TextFontProvider.of(modifiers).getFont());
            return text;
        } else {
            Text text = new Text(jerryNode.getNodeValue());
            text.setFont(TextFontProvider.of(modifiers).getFont());
            return text;
        }
    }

    private static String extractHref(jodd.lagarto.dom.Node jerryNode) {
        String href = "";
        jodd.lagarto.dom.Node nodeToCheck = jerryNode;
        while (null != nodeToCheck.getParentNode()) {
            href = nodeToCheck.getAttribute("l:href");
            if (null != href && href.length() != 0) {
                break;
            }
            nodeToCheck = nodeToCheck.getParentNode();
        }
        return href;
    }

    private static ImageView jerryToImage(jodd.lagarto.dom.Node jerryNode) {
        String imgHref = extractHref(jerryNode);
        String imgValue = safeExtractValue(() -> BookHolder.getFictionBook().getBinaries().get(imgHref).getValue(), null);
        final ImageView image = new ImageView();
        safeExecute(() -> {
            if (null != imgValue) {
                image.setImage(new Image(new ByteArrayInputStream(Base64.decode(imgValue))));
                image.setFitHeight(400.0d);
                image.setPreserveRatio(true);
            }
        }, IGNORE);
        return image;
    }

    // endregion

}
