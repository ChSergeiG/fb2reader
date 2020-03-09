package ru.chsergeig.fb2reader.mapping.cache;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class BookCacheEntry implements Comparable<BookCacheEntry> {

    @JsonProperty("title")
    public String bookTitle;
    @JsonProperty("name")
    public String fileName;
    @JsonProperty("hash")
    public String hash;
    @JsonProperty("date")
    public LocalDateTime lastLoadedDate;

    @Override
    public int compareTo(BookCacheEntry entry) {
        return lastLoadedDate.compareTo(entry.lastLoadedDate);
    }
}
