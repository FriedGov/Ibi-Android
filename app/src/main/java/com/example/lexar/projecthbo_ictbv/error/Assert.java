package com.example.lexar.projecthbo_ictbv.error;

import com.example.lexar.projecthbo_ictbv.BuildConfig;

/**
 * Used for throwing assertion errors because Android doesn't behave like Java.
 */
public class Assert {
    public static void that(boolean condition, String message) {
        if (BuildConfig.DEBUG && !condition) {
            throw new AssertionError(message);
        }
    }
}
