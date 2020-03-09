package ru.chsergeig.fb2reader.util;

import ru.chsergeig.fb2reader.util.enumeration.InCaseOfFail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public final class Utils {

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

}
