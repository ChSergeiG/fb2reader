package ru.chsergeig.fb2reader.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.chsergeig.fb2reader.mapping.cache.BookCache;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class CacheUtils {

    private static final Path CACHE_PATH = Paths.get(System.getProperty("java.io.tmpdir"), "fb2reader_cache.json");
    private static BookCache bookCache;

    private CacheUtils() {
    }

    public static BookCache getBookCache() {
        checkCacheFileExistence();
        readCacheFromFile();
        return bookCache;
    }

    public static void writeCacheToFile() {
        ObjectMapper objectMapper = getMapper();
        try {
            objectMapper.writeValue(Files.newOutputStream(CACHE_PATH), bookCache);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void validateCache() {
        List<Path> wrongEntries = bookCache.books.stream()
                .map(entry -> Paths.get(entry.fileName))
                .filter(path -> !Files.exists(path))
                .collect(Collectors.toList());
        for (Path wrongPath : wrongEntries) {
            bookCache.removeEntry(wrongPath);
        }
    }

    // region private
    private static void checkCacheFileExistence() {
        if (!Files.exists(CACHE_PATH)) {
            try {
                Files.createFile(CACHE_PATH);
                Files.write(CACHE_PATH, "{\"books\":[]}".getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE);
            } catch (IOException e) {
                throw new RuntimeException("Cant create temp file");
            }
        }
    }

    private static void readCacheFromFile() {
        if (null == bookCache) {
            ObjectMapper objectMapper = getMapper();
            try {
                bookCache = objectMapper.readValue(Files.newInputStream(CACHE_PATH), BookCache.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    // endregion
}
