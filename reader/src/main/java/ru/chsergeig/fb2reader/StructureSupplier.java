package ru.chsergeig.fb2reader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public enum StructureSupplier {

    /*
    http://www.fictionbook.org/index.php/%D0%9E%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5_%D1%84%D0%BE%D1%80%D0%BC%D0%B0%D1%82%D0%B0_FB2_%D0%BE%D1%82_Sclex
     */

    GENRE("title-info > genre"),
    AUTHOR("title-info > author"),
    BOOK_TITLE("title-info > book-title"),
    ANNOTATION("title-info > annotation > p"),










    BINARY("binary"),
    ;

    private String locator;

    StructureSupplier(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String getLocator(String additionalChain) {
        return locator + " > " + additionalChain;
    }

    public static Elements getElements(StructureSupplier supplier) {
        return getElements(BookHolder.getBook(), supplier);
    }

    public static Elements getElements(Document document, StructureSupplier supplier) {
        return document.select(supplier.getLocator());
    }

    public Elements getElements() {
        return getElements(BookHolder.getBook());
    }

    public Elements getElements(Document document) {
        return document.select(getLocator());
    }

    public static Elements getElements(StructureSupplier supplier, String additionalChain) {
        return getElements(BookHolder.getBook(), supplier, additionalChain);
    }

    public static Elements getElements(Document document, StructureSupplier supplier, String additionalChain) {
        return document.select(supplier.getLocator(additionalChain));
    }

    public Elements getElements(String additionalChain) {
        return getElements(BookHolder.getBook(), additionalChain);
    }

    public Elements getElements(Document document, String additionalChain) {
        return document.select(getLocator(additionalChain));
    }

    public static Element getFirstElement(StructureSupplier supplier) {
        return getFirstElement(BookHolder.getBook(), supplier);
    }

    public static Element getFirstElement(Document document, StructureSupplier supplier) {
        return document.select(supplier.getLocator()).first();
    }

    public Element getFirstElement() {
        return getFirstElement(BookHolder.getBook());
    }

    public Element getFirstElement(Document document) {
        return document.select(getLocator()).first();
    }

    public static Element getFirstElement(StructureSupplier supplier, String additionalChain) {
        return getFirstElement(BookHolder.getBook(), supplier, additionalChain);
    }

    public static Element getFirstElement(Document document, StructureSupplier supplier, String additionalChain) {
        return document.select(supplier.getLocator(additionalChain)).first();
    }

    public Element getFirstElement(String additionalChain) {
        return getFirstElement(BookHolder.getBook(), additionalChain);
    }

    public Element getFirstElement(Document document, String additionalChain) {
        return document.select(getLocator(additionalChain)).first();
    }

}
