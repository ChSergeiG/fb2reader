package ru.chsergeig.fb2reader.mapping.fictionbook.body;

import jodd.jerry.Jerry;

public class MyHeader {

    private String text;

    public MyHeader(Jerry element) {
        text = element.text();
    }

    public String getTexts() {
        return text;
    }

}
