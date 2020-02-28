package ru.chsergeig.fb2reader.mapping.body;

import jodd.jerry.Jerry;

public class MyFooter {
    private String text;

    public MyFooter(Jerry element) {
        text = element.text();
    }

    public String getTexts() {
        return text;
    }
}
