package ru.chsergeig.fb2reader.mapping.common;

import org.jsoup.nodes.Element;

public class Binary {

    private String value;
    private String id;
    private String type;

    public Binary(Element element) {
        if (!element.tagName().equals("binary")) {
            return;
        }
        this.value = element.text().replaceAll("\\s*", "");
        this.id = element.attr("id");
        this.type = element.attr("content-type");
    }

    public Binary(String value, String id, String type) {
        this.value = value;
        this.id = id;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
