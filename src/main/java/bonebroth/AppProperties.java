package bonebroth;

import java.nio.charset.*;

public final class AppProperties {

    private static final Log log = Log.logger(AppProperties.class);

    private static final String PREFIX = "bonebroth";

    private AppProperties() {
    }

    static Charset getCharset(String keyword) {
        if (Boolean.getBoolean(PREFIX + ".encoding." + keyword + ".default")) {
            return Charset.defaultCharset();
        }
        if (Boolean.getBoolean(PREFIX + ".encoding." + keyword)) {
            try {
                return Charset.forName(System.getProperty(""));
            } catch (UnsupportedCharsetException e) {
                log.warn(() -> "at Charset.forName " + PREFIX + ".encoding." + keyword, e);
                // ignore
            }
        }
        return StandardCharsets.UTF_8;
    }

    public static Charset getCharsetForInput() {
        return getCharset("input");
    }

    public static Charset getCharsetForOutput() {
        return getCharset("output");
    }

    public static Charset getCharsetForTemplate() {
        return getCharset("template");
    }

}
