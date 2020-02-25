package ru.chsergeig.fb2reader.util;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.chsergeig.fb2reader.BookHolder;

public enum StructureSupplier {

    // title-info
    GENRE("title-info > genre"),
    BOOK_AUTHOR("title-info > author"),
    BOOK_TITLE("title-info > book-title"),
    ANNOTATION("title-info > annotation > p"),
    KEYWORDS("title-info > keywords"),
    DATE("title-info > date"),
    COVERPAGE("title-info > coverpage > img"),
    LANG("title-info > lang"),
    SRC_LANG("title-info > src-lang"),
    TRANSLATOR("title-info > translator"),
    SEQUENCE("title-info > sequence"),
    // document-info
    FB2_AUTHOR("document-info > author"),
    FB2_PROGRAM_USED("document-info > program-used"),
    FB2_DATE("document-info > date"),
    FB2_SRC_URL("document-info > src-url"),
    FB2_SRC_OCR("document-info > src-ocr"),
    FB2_ID("document-info > id"),
    FB2_VERSION("document-info > version"),
    FB2_HISTORY("document-info > history"),
    // publish-info
    BOOK_NAME("publish-info > book-name"),
    PUBLISHER("publish-info > publisher"),
    CITY("publish-info > city"),
    YEAR("publish-info > year"),
    ISBN("publish-info > isbn"),
    // body
    MY_HEADER("myheader"),
    BODY("body"),
    MY_FOOTER("myfooter"),
    // binaries
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
