package ru.chsergeig.fb2reader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jodd.jerry.Jerry;
import ru.chsergeig.fb2reader.mapping.fictionbook.FictionBook;
import ru.chsergeig.fb2reader.mapping.fictionbook.common.Author;
import ru.chsergeig.fb2reader.misc.BookInfoTableRow;
import ru.chsergeig.fb2reader.util.CacheUtils;
import ru.chsergeig.fb2reader.util.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BookHolder {

    public static double fontSize = 15.0d;
    private static Jerry book;
    private static FictionBook fictionBook;
    private static String validCharset;

    public static Jerry getBook() {
        return book;
    }

    public static void setBook(Path pathFile) {
        if (pathFile.toString().toLowerCase().endsWith(".fb2")) {
            validCharset = TextUtils.getValidCharset(pathFile);
            try {
                book = Jerry.jerry(String.join("\n", Files.readAllLines(pathFile, Charset.forName(validCharset))));
            } catch (IOException e) {
                throw new RuntimeException("Cant parse book", e);
            }
        }
        if (pathFile.toString().toLowerCase().endsWith(".zip")) {
            Path extractedPath = null;
            try {
                ZipFile zipFile = new ZipFile(pathFile.toFile());
                Optional<? extends ZipEntry> zipEntry = zipFile.stream()
                        .filter(entry -> entry.getName().toLowerCase().endsWith(".fb2"))
                        .findFirst();
                if (zipEntry.isPresent()) {
                    InputStream fileInputStream = zipFile.getInputStream(zipEntry.get());
                    extractedPath = Paths.get(System.getProperty("java.io.tmpdir"), "fb2r_" + UUID.randomUUID().toString() + ".fb2");
                    Files.copy(fileInputStream, extractedPath);
                }
            } catch (IOException e) {
                throw new RuntimeException("Cant load zip file", e);
            }
            validCharset = TextUtils.getValidCharset(extractedPath);
            try {
                book = Jerry.jerry(String.join("\n", Files.readAllLines(extractedPath, Charset.forName(validCharset))));
            } catch (IOException e) {
                throw new RuntimeException("Cant parse book", e);
            }
        }
        mapBookToFb2();
        CacheUtils.getBookCache().addEntry(pathFile, fictionBook.getBookTitle(), LocalDateTime.now());
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
