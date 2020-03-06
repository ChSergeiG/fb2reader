package ru.chsergeig.fb2reader.mapping.fictionbook.titleinfo;

import jodd.jerry.Jerry;

import static ru.chsergeig.fb2reader.util.Utils.safeExtractValue;

public class Sequence {

    private String name;
    private String number;

    public Sequence(Jerry element) {
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
