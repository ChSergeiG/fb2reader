package ru.chsergeig.fb2reader.util;

import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final String[] CHARSETS_TO_TEST = {"windows-1251", "utf-8"};

    private TextUtils() {
    }

    public static String getValidCharset(Path file) {
        for (String charset : CHARSETS_TO_TEST) {
            String line;
            try {
                BufferedReader bufferedReader = Files.newBufferedReader(file, Charset.forName(charset));
                line = bufferedReader.readLine();
            } catch (Exception e) {
                continue;
            }
            Pattern encoding = Pattern.compile("encoding\\s*=\\s*\"(.+?)\"");
            Matcher matcher = encoding.matcher(line);
            if (!matcher.find()) {
                continue;
            }
            return matcher.group(1);
        }
        throw new RuntimeException("Cant determine charset");
    }


}
