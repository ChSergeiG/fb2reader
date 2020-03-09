package ru.chsergeig.fb2reader.util.enumeration;

import jodd.jerry.Jerry;
import ru.chsergeig.fb2reader.BookHolder;

public enum StructureSupplier {

    // title-info
    GENRE("title-info > genre"),
    BOOK_AUTHOR("title-info > author"),
    BOOK_TITLE("title-info > book-title"),
    ANNOTATION("title-info > annotation > p"),
    KEYWORDS("title-info > keywords"),
    DATE("title-info > date"),
    COVERPAGE("title-info > coverpage > image"),
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
    FICTIONBOOK("fictionbook"),
    BODY("body"),
    MY_FOOTER("myfooter"),
    // binaries
    BINARY("binary"),
    ;

    private String locator;

    StructureSupplier(String locator) {
        this.locator = locator;
    }

    public static Jerry getElements(StructureSupplier supplier) {
        return getElements(BookHolder.getBook(), supplier);
    }

    public static Jerry getElements(Jerry document, StructureSupplier supplier) {
        return document.find(supplier.getLocator());
    }

    public static Jerry getElements(StructureSupplier supplier, String additionalChain) {
        return getElements(BookHolder.getBook(), supplier, additionalChain);
    }

    public static Jerry getElements(Jerry document, StructureSupplier supplier, String additionalChain) {
        return document.find(supplier.getLocator(additionalChain));
    }

    public static Jerry getFirstElement(StructureSupplier supplier) {
        return getFirstElement(BookHolder.getBook(), supplier);
    }

    public static Jerry getFirstElement(Jerry document, StructureSupplier supplier) {
        return document.find(supplier.getLocator()).first();
    }

    public static Jerry getFirstElement(StructureSupplier supplier, String additionalChain) {
        return getFirstElement(BookHolder.getBook(), supplier, additionalChain);
    }

    public static Jerry getFirstElement(Jerry document, StructureSupplier supplier, String additionalChain) {
        return document.find(supplier.getLocator(additionalChain)).first();
    }

    public String getLocator() {
        return locator;
    }

    public String getLocator(String additionalChain) {
        return locator + " > " + additionalChain;
    }

    public Jerry getElements() {
        return getElements(BookHolder.getBook());
    }

    public Jerry getElements(Jerry document) {
        return document.find(getLocator());
    }

    public Jerry getElements(String additionalChain) {
        return getElements(BookHolder.getBook(), additionalChain);
    }

    public Jerry getElements(Jerry document, String additionalChain) {
        return document.find(getLocator(additionalChain));
    }

    public Jerry getFirstElement() {
        return getFirstElement(BookHolder.getBook());
    }

    public Jerry getFirstElement(Jerry document) {
        return document.find(getLocator()).first();
    }

    public Jerry getFirstElement(String additionalChain) {
        return getFirstElement(BookHolder.getBook(), additionalChain);
    }

    public Jerry getFirstElement(Jerry document, String additionalChain) {
        return document.find(getLocator(additionalChain)).first();
    }

}
