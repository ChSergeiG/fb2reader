package ru.chsergeig.fb2reader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jodd.jerry.Jerry;
import ru.chsergeig.fb2reader.mapping.cache.BookCacheEntry;
import ru.chsergeig.fb2reader.mapping.fictionbook.FictionBook;
import ru.chsergeig.fb2reader.mapping.fictionbook.common.Author;
import ru.chsergeig.fb2reader.misc.BookInfoTableRow;
import ru.chsergeig.fb2reader.util.CacheUtils;
import ru.chsergeig.fb2reader.util.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static ru.chsergeig.fb2reader.util.Utils.readAsRegularFile;
import static ru.chsergeig.fb2reader.util.Utils.readAsZipFile;

public class BookHolder {

    public static double fontSize = 15.0d;
    private static Jerry book;
    private static FictionBook fictionBook;

    public static Jerry getBook() {
        return book;
    }

    public static void setBook(Path pathFile) {
        Optional<BookCacheEntry> bookCacheEntry = CacheUtils.getBookCache().getByFileName(pathFile.toString());
        if (bookCacheEntry.isPresent() && getExtension(pathFile.toString()).equals("zip")) {
            String extractedPath = bookCacheEntry.get().extractedPath;
            if (Files.exists(Paths.get(extractedPath))) {
                Utils.BookPathContainer bookPathContainer = readAsRegularFile(Paths.get(extractedPath));
                book = bookPathContainer.getBook();
                mapBookToFb2();
                CacheUtils.getBookCache().addEntry(pathFile, bookPathContainer.getPath().get(), fictionBook.getBookTitle(), LocalDateTime.now());
                return;
            }
        }


        if (pathFile.toString().toLowerCase().endsWith(".fb2")) {
            Utils.BookPathContainer bookPathContainer = readAsRegularFile(pathFile);
            book = bookPathContainer.getBook();
            mapBookToFb2();
            CacheUtils.getBookCache().addEntry(pathFile, fictionBook.getBookTitle(), LocalDateTime.now());
        } else if (pathFile.toString().toLowerCase().endsWith(".zip")) {
            Utils.BookPathContainer bookPathContainer = readAsZipFile(pathFile);
            book = bookPathContainer.getBook();
            mapBookToFb2();
            CacheUtils.getBookCache().addEntry(pathFile, bookPathContainer.getPath().orElse(null), fictionBook.getBookTitle(), LocalDateTime.now());
        }
    }

    public static FictionBook getFictionBook() {
        return fictionBook;
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
