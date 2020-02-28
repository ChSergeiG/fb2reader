package ru.chsergeig.fb2reader;

import jodd.jerry.Jerry;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        File file = new File(Main.class.getClassLoader().getResource("123.fb2").toURI());
        Jerry jerry = Jerry.jerry(String.join("\n", Files.readAllLines(file.toPath(), Charset.forName("windows-1251"))));

        System.out.println(123);

    }

}
