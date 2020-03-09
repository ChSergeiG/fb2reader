package ru.chsergeig.fb2reader.mapping.cache;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCache {

    @JsonProperty("books")
    public List<BookCacheEntry> books = new ArrayList<>();

    public synchronized void addEntry(Path path, String title, LocalDateTime time) {
        if (books.stream().noneMatch(entry -> path.toString().equals(entry.fileName))) {
            BookCacheEntry cacheEntry = new BookCacheEntry();
            cacheEntry.fileName = path.toString();
            cacheEntry.lastLoadedDate = time;
            cacheEntry.hash = "";
            cacheEntry.bookTitle = title;
            books.add(cacheEntry);
        } else {
            Optional<BookCacheEntry> found = books.stream().filter(entry -> path.toString().equals(entry.fileName)).findFirst();
            if (found.isPresent()) {
                BookCacheEntry bookCacheEntry = found.get();
                bookCacheEntry.fileName = path.toString();
                bookCacheEntry.lastLoadedDate = time;
                bookCacheEntry.hash = "";
                bookCacheEntry.bookTitle = title;
            }
        }
    }

    public synchronized void removeEntry(Path path) {
        Optional<BookCacheEntry> found = books.stream().filter(entry -> path.toString().equals(entry.fileName)).findFirst();
        if (found.isPresent()) {
            BookCacheEntry bookCacheEntry = found.get();
            books.remove(bookCacheEntry);
        }
    }

}
