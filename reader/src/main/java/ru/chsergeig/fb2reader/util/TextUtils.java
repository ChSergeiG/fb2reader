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

    public static <T> T safeExtractValue(Supplier<T> supplier) {
        return safeExtractValue(supplier, null);
    }

    public static <T> T safeExtractValue(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception ignore) {
            return defaultValue;
        }
    }

    public static <T> T safeExtractValue(Class<T> clazz, T defaultValue, Supplier<?>... suppliers) {
        try {
            Class<?>[] contructTypes = new Class<?>[suppliers.length];
            Object[] contructValues = new Object[suppliers.length];
            for (int i = 0; i < suppliers.length; i++) {
                Supplier<?> supplier = suppliers[i];
                Object o = safeExtractValue(supplier);
                if (null == o) {
                    return defaultValue;
                }
                contructValues[i] = o;
                contructTypes[i] = o.getClass();
            }
            Constructor<T> declaredConstructor = clazz.getDeclaredConstructor(contructTypes);
            return declaredConstructor.newInstance(contructValues);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignore) {
            return defaultValue;
        }
    }

    public static List<Text> filterTexts(List<Text> textToFilter) {
        boolean needIndent = true;
        List<Text> result = new ArrayList<>();
        for (Text text : textToFilter) {

        }
        return result;
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

//    public static List<Node> toTexts(Element element) {
//        List<Node> result = new ArrayList<>();
//        toTexts(element, result);
//        return result;
//    }
//
//    private static void toTexts(Element element, List<Node> texts) {
//        for (org.jsoup.nodes.Node node : element.childNodes()) {
//            if (node instanceof TextNode) {
//                Text text = new Text(((TextNode) node).text());
//                text.setFont(Font.font("Helvetica", 15));
//                texts.add(text);
//            } else if (node instanceof Element) {
//                switch (((Element) node).tagName()) {
//                    case "i":
//                        Text text = new Text(((Element) node).text());
//                        text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
//                        texts.add(text);
//                        break;
//                    case "strong":
//                        Text strongText = new Text(((Element) node).text());
//                        strongText.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
//                        texts.add(strongText);
//                        break;
//                    case "empty-line":
//                        Text emptyText = new Text("\r\n");
//                        emptyText.setFont(Font.font("Helvetica", 15));
//                        texts.add(emptyText);
//                        break;
//                    case "a":
//                        Hyperlink hyperText = new Hyperlink(((Element) node).text());
//                        hyperText.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
//                        try {
//                            URI link = new URI(node.attr("l:href"));
//                            hyperText.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//                                try {
//                                    Desktop.getDesktop().browse(link);
//                                } catch (IOException ignore) {
//                                }
//                            });
//                        } catch (URISyntaxException ignore) {
//                        }
//                        texts.add(hyperText);
//                        break;
//                    default:
//                        toTexts((Element) node, texts);
//                        break;
//                }
//            }
//        }
//    }
//
//    public static List<Node> convertHtmlToTexts(String html) {
//        // TITLE, CITE, LINK, BOLD, ITALIC
//        int modifier = 0b00000;
//        List<Node> result = new ArrayList<>();
//        String toTokenize = html;
//        int tokenStartPosition = toTokenize.indexOf('<');
//        int tokenFinishPosition = toTokenize.indexOf('>', tokenStartPosition);
//        String prevText = html.substring(0, tokenStartPosition);
//        if (prevText.trim().length() > 0) {
//            result.add(addTextWithModifiers(prevText, modifier));
//        }
//        String token = toTokenize.substring(tokenStartPosition + 1, tokenFinishPosition).trim();
//        if (token.contains("i")) {
//            if (token.contains("/")) {
//                modifier &= 0b11110;
//            } else {
//                modifier |= 0b00001;
//            }
//        } else if (token.contains("strong")) {
//            if (token.contains("/")) {
//                modifier &= 0b11101;
//            } else {
//                modifier |= 0b00010;
//            }
//
//        }
//
//        return result;
//    }
//
//    private static Node addTextWithModifiers(String prevText, int modifier) {
//        // title
//        if (0b1 == (modifier & 0b10000 >> 4)) {
//            Text titleText = new Text(prevText);
//            titleText.setFont(Font.font("Helvetica", 25.0));
//            titleText.setTextAlignment(TextAlignment.CENTER);
//            return titleText;
//        }
//        // cite
//        if (0b1 == (modifier & 0b1000 >> 3)) {
//            Text titleText = new Text(prevText);
//            titleText.setFont(Font.font("Helvetica", 15.0));
//            titleText.setTextAlignment(TextAlignment.RIGHT);
//            return titleText;
//        }
//        // link
//        if (0b10 == (modifier & 0b00010)) {
//            Hyperlink hyperText = new Hyperlink(prevText);
//            hyperText.setFont(ofModifier(modifier));
//            hyperText.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//                try {
//                    Desktop.getDesktop().browse(new URI(""));
//                } catch (IOException | URISyntaxException ignore) {
//                }
//            });
//            return hyperText;
//        }
//        Text text = new Text(prevText);
//        text.setFont(ofModifier(modifier));
//        text.setTextAlignment(TextAlignment.JUSTIFY);
//        return text;
//    }
//
//    private static Font ofModifier(int modifier) {
//        int masked = 0b00011 & modifier;
//        switch (masked) {
//            case 0b00:
//                return Font.font("Helvetica", 15.0);
//            case 0b01:
//                return Font.font("Helvetica", FontPosture.ITALIC, 15.0);
//            case 0b10:
//                return Font.font("Helvetica", FontWeight.BOLD, 15.0);
//            case 0b11:
//                return Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 15.0);
//        }
//        return null;
//    }

}
