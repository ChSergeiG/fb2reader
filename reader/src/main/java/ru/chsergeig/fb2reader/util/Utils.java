package ru.chsergeig.fb2reader.util;

import jodd.jerry.Jerry;
import jodd.lagarto.dom.Attribute;
import jodd.lagarto.dom.Node;
import ru.chsergeig.fb2reader.util.enumeration.InCaseOfFail;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.charset.Charset.forName;

public final class Utils {

    private static final Charset[] ZIP_CHARSETS = {StandardCharsets.UTF_8, forName("CP866"), forName("CP437")};

    private Utils() {
    }

    public static void safeExecute(Runnable toExecute) {
        safeExecute(toExecute, InCaseOfFail.IGNORE);
    }

    public static void safeExecute(Runnable toExecute, InCaseOfFail inCaseOfFail) {
        try {
            toExecute.run();
        } catch (Throwable t) {
            switch (inCaseOfFail) {
                case THROW_EXCEPTION:
                    throw new RuntimeException(t);
                case IGNORE:
                    // do nothing
            }
        }
    }

    public static <T> T safeExtractValue(Supplier<T> supplier) {
        return safeExtractValue(supplier, null);
    }

    public static <T> T safeExtractValue(Supplier<T> supplier, T defaultValue) {
        try {
            T t = supplier.get();
            return null != t ? t : defaultValue;
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

    public static String getAttributeIgnoreNs(Jerry item, String postfix) {
        return getAttributeIgnoreNs(item.get(0), postfix);

    }

    public static String getAttributeIgnoreNs(Node jerryNode, String postfix) {
        String attribute = "";
        int i = 0;
        while (attribute != null) {
            Attribute jerryNodeAttribute;
            try {
                jerryNodeAttribute = jerryNode.getAttribute(i++);
                if (null == jerryNodeAttribute) {
                    return null;
                }
            } catch (NullPointerException ignore) {
                return null;
            }
            attribute = jerryNodeAttribute.getName();
            if (attribute.endsWith(postfix)) {
                return jerryNodeAttribute.getValue();
            }
        }
        return null;
    }

    public static BookPathContainer readAsRegularFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path, forName(TextUtils.getValidCharset(path)));
            return new BookPathContainer(Jerry.jerry(String.join("\n", lines)), path);
        } catch (IOException e) {
            throw new RuntimeException("Cant parse book", e);
        }
    }

    public static BookPathContainer readAsZipFile(Path path) {
        Path extractedPath = null;
        for (Charset charset : ZIP_CHARSETS) {
            try {
                ZipFile zipFile = new ZipFile(path.toFile(), charset);
                Optional<? extends ZipEntry> zipEntry = zipFile.stream()
                        .filter(entry -> entry.getName().toLowerCase().endsWith(".fb2"))
                        .findFirst();
                if (zipEntry.isPresent()) {
                    InputStream fileInputStream = zipFile.getInputStream(zipEntry.get());
                    extractedPath = Paths.get(System.getProperty("java.io.tmpdir"), "fb2r_" + UUID.randomUUID().toString() + ".fb2");
                    Files.copy(fileInputStream, extractedPath);
                }
                break;
            } catch (IOException e) {
                throw new RuntimeException("Cant load zip file", e);
            } catch (IllegalArgumentException ignore) {
            }
        }
        return readAsRegularFile(extractedPath);
    }

    public static class BookPathContainer {
        Jerry book;
        Path path;

        public BookPathContainer(Jerry book, Path path) {
            this.book = book;
            this.path = path;
        }

        public Jerry getBook() {
            return book;
        }

        public Optional<Path> getPath() {
            return null == path ? Optional.empty() : Optional.of(path);
        }
    }

}
