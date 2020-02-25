package ru.chsergeig.fb2reader.mapping.titleinfo;

import org.jsoup.nodes.Element;

import static ru.chsergeig.fb2reader.util.TextUtils.safeExtractValue;

public class Sequence {

    private String name;
    private String number;

    public Sequence(Element element) {
        this.name = safeExtractValue(() -> element.attr("name"), "");
        this.number = safeExtractValue(() -> element.attr("number"), "");
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return name + " [" + number + "]";
    }
}
