package ru.chsergeig.fb2reader;

import org.jsoup.nodes.Document;

public class BookHolder {

    private static Document book;

    public static void setBook(Document book) {
        BookHolder.book = book;
    }

    public static Document getBook() {
        return book;
    }
}
