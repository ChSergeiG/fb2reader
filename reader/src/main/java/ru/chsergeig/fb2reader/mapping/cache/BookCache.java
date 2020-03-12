package ru.chsergeig.fb2reader.mapping.cache;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            cacheEntry.hash = getHash(path);
            cacheEntry.bookTitle = title;
            books.add(cacheEntry);
        } else {
            Optional<BookCacheEntry> found = books.stream().filter(entry -> path.toString().equals(entry.fileName)).findFirst();
            if (found.isPresent()) {
                BookCacheEntry bookCacheEntry = found.get();
                bookCacheEntry.fileName = path.toString();
                bookCacheEntry.lastLoadedDate = time;
                bookCacheEntry.hash = getHash(path);
                bookCacheEntry.bookTitle = title;
            }
        }
    }

    public synchronized void addEntry(Path path, Path extractedPath, String title, LocalDateTime time) {
        if (books.stream().noneMatch(entry -> path.toString().equals(entry.fileName))) {
            BookCacheEntry cacheEntry = new BookCacheEntry();
            cacheEntry.fileName = path.toString();
            cacheEntry.lastLoadedDate = time;
            cacheEntry.hash = getHash(path);
            cacheEntry.bookTitle = title;
            cacheEntry.extractedPath = extractedPath.toString();
            books.add(cacheEntry);
        } else {
            Optional<BookCacheEntry> found = books.stream().filter(entry -> path.toString().equals(entry.fileName)).findFirst();
            if (found.isPresent()) {
                BookCacheEntry bookCacheEntry = found.get();
                bookCacheEntry.fileName = path.toString();
                bookCacheEntry.lastLoadedDate = time;
                bookCacheEntry.hash = getHash(path);
                bookCacheEntry.bookTitle = title;
                bookCacheEntry.extractedPath = extractedPath.toString();
            }
        }
    }

    private String getHash(Path path) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream fis = Files.newInputStream(path)) {
                byte[] buffer = new byte[1024];
                int read;
                do {
                    read = fis.read(buffer);
                    if (read > 0) {
                        md.update(buffer, 0, read);
                    }
                } while (read != -1);
            }
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException | IOException ignore) {
        }
        return "";
    }

    public synchronized void removeEntry(Path path) {
        Optional<BookCacheEntry> found = books.stream().filter(entry -> path.toString().equals(entry.fileName)).findFirst();
        if (found.isPresent()) {
            BookCacheEntry bookCacheEntry = found.get();
            books.remove(bookCacheEntry);
        }
    }

    public Path getByHash(String hash) {

    }

}
