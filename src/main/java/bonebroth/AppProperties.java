package bonebroth;

import java.nio.charset.*;
import org.apache.commons.lang3.*;

public final class AppProperties {

    private static final Log log = Log.logger(AppProperties.class);

    private static final String PREFIX = "bonebroth";

    private AppProperties() {
    }

    static Charset getCharset(String keyword) {
        final String propKey = PREFIX + ".encoding." + keyword;
        if (Boolean.getBoolean(propKey + ".default")) {
            return Charset.defaultCharset();
        }
        final String propValue = System.getProperty(propKey);
        if (StringUtils.isNotBlank(propValue)) {
            try {
                return Charset.forName(propValue);
            } catch (UnsupportedCharsetException e) {
                log.warn(() -> "at Charset.forName " + propKey, e);
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
