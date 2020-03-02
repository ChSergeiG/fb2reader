package ru.chsergeig.fb2reader.util;

public final class Utils {

    private Utils() {
    }

    public static void safeExecute(Runnable toExecute) {
        try {
            toExecute.run();
        } catch (Throwable ignore) {
        }
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

}
