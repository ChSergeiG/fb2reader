package ru.chsergeig.fb2reader.mapping.documentinfo;

import jodd.jerry.Jerry;

public class History {

    private String text;

    public History(Jerry element) {
        text = element.text();
    }

    public String getText() {
        return text;
    }

}
