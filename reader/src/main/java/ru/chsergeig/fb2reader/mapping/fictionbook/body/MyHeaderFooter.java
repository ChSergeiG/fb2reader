package ru.chsergeig.fb2reader.mapping.fictionbook.body;

import jodd.jerry.Jerry;
import ru.chsergeig.fb2reader.elements.TextFlowContainer;

import java.util.List;

import static ru.chsergeig.fb2reader.util.TextUtils.jerryToParagraphs;

public class MyHeaderFooter {

    private Jerry element;

    public MyHeaderFooter(Jerry element) {
        this.element = element;
    }

    public List<TextFlowContainer> getTexts() {
        return jerryToParagraphs(element);
    }

}
