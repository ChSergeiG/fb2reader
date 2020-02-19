package ru.chsergeig.fb2reader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        File f = Paths.get(Main.class.getClassLoader().getResource("123.fb2").toURI()).toFile();
        Document book = Jsoup.parse(f, "utf-8");
        BookHolder.setBook(book);

        ImageIO.read(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(book.select("binary").first().text().replaceAll("\\s*", ""))));
    }

}
