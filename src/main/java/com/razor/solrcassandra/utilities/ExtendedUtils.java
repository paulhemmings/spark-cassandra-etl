package com.razor.solrcassandra.utilities;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public final class ExtendedUtils {

    public static <T> T orElse (T value, T alt) {
        return Objects.isNull(value) ? alt : value;
    }

    public static String orEmpty(String value) {
        return orElse(value, "");
    }

    public static <T> List<T> orEmpty(List<T> value) {
        return orElse(value, Collections.emptyList());
    }

    public static String quote(String value, boolean toQuote) {
        return toQuote ? "'" + value.replace("'", "") + "'" : value;
    }

}
