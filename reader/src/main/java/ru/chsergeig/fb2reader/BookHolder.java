package ru.chsergeig.fb2reader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jodd.jerry.Jerry;
import ru.chsergeig.fb2reader.mapping.FictionBook;
import ru.chsergeig.fb2reader.mapping.common.Author;
import ru.chsergeig.fb2reader.misc.BookInfoTableRow;
import ru.chsergeig.fb2reader.util.TextUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.stream.Collectors;

public class BookHolder {

    private static Jerry book;
    private static FictionBook fictionBook;
    private static String validCharset;
    public static double fontSize = 15.0d;

    public static void setBook(Path file) {
        validCharset = TextUtils.getValidCharset(file);
        try {
            book = Jerry.jerry(String.join("\n", Files.readAllLines(file, Charset.forName(validCharset))));
        } catch (IOException e) {
            throw new RuntimeException("Cant parse book", e);
        }
        mapBookToFb2();
    }

    public static Jerry getBook() {
        return book;
    }

    public static FictionBook getFictionBook() {
        return fictionBook;
    }

    public static String getValidCharset() {
        return validCharset;
    }

    private static void mapBookToFb2() {
        fictionBook = new FictionBook();
    }

    public static ObservableList<BookInfoTableRow> getCurrentBookInfo() {
        List<BookInfoTableRow> result = new ArrayList<>();
        result.add(new BookInfoTableRow("Genre", fictionBook.getGenre()));
        result.add(new BookInfoTableRow("Book author", fictionBook.getBookAuthors().stream().map(Author::toString).collect(Collectors.joining(", "))));
        result.add(new BookInfoTableRow("Book title", fictionBook.getBookTitle()));
        result.add(new BookInfoTableRow("Keywords", fictionBook.getKeywords()));
        result.add(new BookInfoTableRow("Date", fictionBook.getDate()));
        result.add(new BookInfoTableRow("Language", fictionBook.getLang()));
        result.add(new BookInfoTableRow("Source language", fictionBook.getSrcLang()));
        result.add(new BookInfoTableRow("Translator", fictionBook.getTranslator().toString()));
        result.add(new BookInfoTableRow("Sequence", fictionBook.getSequence().toString()));
        result.add(new BookInfoTableRow("FB2 author", fictionBook.getFb2Author().toString()));
        result.add(new BookInfoTableRow("Program used", fictionBook.getProgramUsed()));
        result.add(new BookInfoTableRow("FB2 date", fictionBook.getFb2Date()));
        result.add(new BookInfoTableRow("FB2 version", fictionBook.getFb2Version()));
        result.add(new BookInfoTableRow("Book name", fictionBook.getBookName()));
        result.add(new BookInfoTableRow("Publisher", fictionBook.getPublisher()));
        result.add(new BookInfoTableRow("City", fictionBook.getCity()));
        result.add(new BookInfoTableRow("ISBN", fictionBook.getIsbn()));
        return FXCollections.observableArrayList(result);
    }

}
