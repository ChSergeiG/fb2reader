package ru.chsergeig.fb2reader.mapping.fictionbook.titleinfo;

import jodd.jerry.Jerry;

public class Annotation {

    private String text;

    public Annotation(Jerry element) {
        text = element.text();
    }

    public String getText() {
        return text;
    }
}
