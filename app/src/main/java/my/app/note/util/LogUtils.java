package my.app.note.util;

import android.util.Log;

import java.util.Locale;

/**
 * Created by CCP on 2018.6.20 0020.
 */
public class LogUtils {

    private static final String TAG = "LogUtils";

    private static String buildMessage(String msg) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        String className = stackTrace.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        return String.format(Locale.US, "%s.%s(L:%d): %s", className, stackTrace.getMethodName(), stackTrace.getLineNumber(), msg);
    }

    // Verbose
    public static void v(String msg) {
        Log.v(TAG, buildMessage(msg));
    }

    // Debug
    public static void d(String msg) {
        Log.d(TAG, buildMessage(msg));
    }

    // Info
    public static void i(String msg) {
        Log.i(TAG, buildMessage(msg));
    }

    // Warn
    public static void w(String msg) {
        Log.w(TAG, buildMessage(msg));
    }

    // Error
    public static void e(String msg) {
        Log.e(TAG, buildMessage(msg));
    }

    // Assert
    public static void wtf(String msg) {
        Log.wtf(TAG, buildMessage(msg));
    }
}
