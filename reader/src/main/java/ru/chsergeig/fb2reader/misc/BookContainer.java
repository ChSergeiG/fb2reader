package ru.chsergeig.fb2reader.misc;

import javafx.scene.Node;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class BookContainer {

    private BookContainer parent;

    private List<BookContainer> childrens;

    private List<javafx.scene.Node> content;

    public BookContainer(Element root) {
        this.parent = null;
//        org.jsoup.nodes.Node fbNode = root.childNodes().stream().filter(node -> node instanceof Element).findFirst().get();
//
//        org.jsoup.nodes.Node title = fbNoderoot.childNodes().stream().filter(node -> node instanceof Element).findFirst().get();;
//        content = operateNode(title);
//        childrens = fbNode.childNodes().stream().filter(node -> node instanceof Element && ((Element) node).tagName().equals("section")).map(node -> new BookContainer((Element) node)).collect(Collectors.toList());

    }

    private List<javafx.scene.Node> operateNode(org.jsoup.nodes.Node node) {
        List<javafx.scene.Node> result = new ArrayList<>();
        String toParse = node.outerHtml()
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("<\\s*([/]?)\\s*(\\w+)\\s*([/]?)\\s*>", "<$1$2$3>")
                .replaceAll("<([\\w\\d-]+)/>", "<$1></$1>");
        toTexts(result, toParse);
        return result;
    }

    private void toTexts(List<Node> result, String toParse) {
        toParse.replaceAll("<\\s*([/]?)\\s*(\\w+)\\s*([/]?)\\s*>", "<$1$2$3>");


    }
}
