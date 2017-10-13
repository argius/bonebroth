package bonebroth;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.stream.*;
import org.apache.commons.collections4.*;
import org.apache.commons.csv.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;
import org.apache.velocity.*;
import com.typesafe.config.*;

/**
 * Utilities for <code>VelocityContext</code>.
 */
public final class ContextHelper {

    private ContextHelper() {
    }

    private static final String MESSAGES = "messages";
    private static final String CLASS_NAME = "className";
    private static final String PACKAGE = "package";
    private static final String CLASS_DESCRIPTION = "classDescription";
    private static final String IMPORT = "import";
    private static final String CTOR_DESCRIPTION = "constructorDescription";
    private static final String DEFAULT_VALUE = "defaultValue";
    private static final String VALUE = "value";

    public static VelocityContext read(File... files) {
        VelocityContext ctx = new VelocityContext();
        try {
            for (File file : files) {
                readFile(file, ctx);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return ctx;
    }

    static void readFile(File file, VelocityContext ctx) throws IOException {
        if (!file.canRead()) {
            throw new IllegalArgumentException("can't read file: " + file);
        }
        initializeContext(ctx);
        switch (FilenameUtils.getExtension(file.getName()).toLowerCase()) {
            case "csv":
                readCsv(file, ctx, ',');
                break;
            case "tsv":
            case "txt":
                readCsv(file, ctx, '\t');
                break;
            case "conf":
            case "cnf":
                readConfig(file, ctx);
                break;
            case "properties":
                readProperties(file, ctx);
                break;
            default:
                throw new IOException("unsupported file type: " + file);
        }
    }

    static void initializeContext(VelocityContext ctx) {
        // default settings
        Map<String, Object> m = new HashMap<>();
        m.put(MESSAGES, Messages.class);
        m.put("m", Messages.class); // alias
        m.put(CLASS_NAME, "UnnamedClass");
        m.put(PACKAGE, "com.example");
        m.put(CLASS_DESCRIPTION, "");
        m.put(CTOR_DESCRIPTION, "");
        for (Entry<String, Object> entry : m.entrySet()) {
            final String key = entry.getKey();
            if (!ctx.containsKey(key)) {
                ctx.put(key, entry.getValue());
            }
        }
    }

    static void readCsv(File file, VelocityContext ctx, char delimiter) throws IOException {
        final CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(delimiter);
        String defaultValue = "";
        try (CSVParser p = CSVParser.parse(file, AppProperties.getCharsetForInput(), csvFormat)) {
            List<List<String>> list = IteratorUtils.toList(p.iterator()).stream().map(x -> {
                List<String> a = IteratorUtils.toList(x.iterator());
                Collections.addAll(a, "", "", "");
                for (int i = 0; i < 2; i++) {
                    a.set(i, a.get(i).trim());
                }
                return a;
            }).collect(Collectors.toList());
            final List<String> imports = new ArrayList<>();
            final List<FieldItem> items = new ArrayList<>();
            for (List<String> r : list) {
                final String col1 = StringUtils.trimToEmpty(r.get(0));
                if (StringUtils.isBlank(col1) || col1.startsWith("#")) {
                    continue;
                }
                if (col1.startsWith("@")) {
                    // meta
                    final String directive = col1;
                    final String col2 = r.get(1);
                    final String keyword = StringUtils.removeStart(directive, "@");
                    switch (keyword) {
                        case CLASS_NAME:
                        case CLASS_DESCRIPTION:
                        case PACKAGE:
                        case CTOR_DESCRIPTION:
                            ctx.put(keyword, col2);
                            break;
                        case VALUE:
                            final String col3 = r.get(2);
                            ctx.put(col2, col3);
                            break;
                        case IMPORT:
                            imports.add(col2);
                            break;
                        case DEFAULT_VALUE:
                            defaultValue = col2;
                            break;
                        default:
                            System.err.println("warning: unknown directive: " + directive);
                            break;
                    }
                }
                else {
                    FieldItem accessor = new FieldItem();
                    int index = 0;
                    accessor.setId(RichValue.of(col1));
                    accessor.setType(StringUtils.defaultIfBlank(r.get(++index), "String"));
                    accessor.setName(StringUtils.defaultIfBlank(r.get(++index), col1));
                    accessor.setValue(StringUtils.defaultIfBlank(r.get(++index), defaultValue));
                    items.add(accessor);
                }
            }
            ctx.put("imports", imports);
            ctx.put("items", items);
        }
    }

    static void readConfig(File file, VelocityContext ctx) {
        Config config = ConfigFactory.parseFile(file);
        ctx.put("config", config);
        ctx.put("cfg", config); // alias
        for (Entry<String, Object> entry : config.root().unwrapped().entrySet()) {
            ctx.put(entry.getKey(), entry.getValue());
        }
    }

    static void readProperties(File file, VelocityContext ctx) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(is);
            for (String key : props.stringPropertyNames()) {
                ctx.put(key, RichValue.of(props.getProperty(key)));
            }
            ctx.put("props", props);
            ctx.put("p", props); // alias
        }
    }

}
