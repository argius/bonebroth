package bonebroth;

import static bonebroth.Messages.message;
import java.util.*;
import org.apache.commons.cli.*;

/**
 * A set of options.
 */
public final class OptionSet {

    private String template;
    private String input;
    private String output;
    private boolean generatesBuildInBean;
    private boolean doMkdirs;
    private boolean showVersion;
    private boolean help;

    private OptionSet() {
        //
    }

    public static OptionSet parseArguments(String... args) throws Exception {
        OptionSet.Parser parser = new OptionSet.Parser();
        return parser.parse(args);
    }

    public String getTemplate() {
        return template;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public boolean isGeneratesBuildInBean() {
        return generatesBuildInBean;
    }

    public boolean isDoMkdirs() {
        return doMkdirs;
    }

    public void setDoMkdirs(boolean doMkdirs) {
        this.doMkdirs = doMkdirs;
    }

    public boolean isShowVersion() {
        return showVersion;
    }

    public boolean isHelp() {
        return help;
    }

    /**
     * The parser for OptionSet.
     */
    public static final class Parser {

        private static final Log log = Log.logger(Parser.class);

        private static final String OPTION_TEMPLATE = "template";
        private static final String OPTION_INPUT = "input";
        private static final String OPTION_OUTPUT = "output";
        private static final String OPTION_BEAN = "bean";
        private static final String OPTION_MKDIRS = "mkdirs";
        private static final String OPTION_VERBOSE = "verbose";
        private static final String OPTION_VERSION = "version";
        private static final String OPTION_HELP = "help";

        private final Options options;

        public Parser() {
            this.options = new Options();
            option(OPTION_TEMPLATE, "t", true);
            option(OPTION_INPUT, "i", true);
            option(OPTION_OUTPUT, "o", true);
            option(OPTION_MKDIRS);
            option(OPTION_BEAN);
            option(OPTION_VERBOSE);
            option(OPTION_VERSION);
            option(OPTION_HELP);
        }

        public Options getOptions() {
            return options;
        }

        public OptionSet parse(String... args) throws Exception {
            OptionSet o = new OptionSet();
            CommandLineParser parser = new DefaultParser();
            CommandLine cl = parser.parse(options, args);
            o.template = stringValue(cl, OPTION_TEMPLATE);
            o.input = stringValue(cl, OPTION_INPUT);
            o.output = stringValue(cl, OPTION_OUTPUT);
            o.generatesBuildInBean = boolValue(cl, OPTION_BEAN);
            o.doMkdirs = boolValue(cl, OPTION_MKDIRS);
            o.showVersion = boolValue(cl, OPTION_VERSION);
            o.help = boolValue(cl, OPTION_HELP);
            log.debug(() -> "non-option args=" + cl.getArgList());
            return o;
        }

        Option option(String optionKey) {
            return option(optionKey, null, false);
        }

        Option option(String optionKey, boolean requiresArgument) {
            return option(optionKey, null, requiresArgument);
        }

        Option option(String optionKey, String shortKey) {
            return option(optionKey, shortKey, false);
        }

        Option option(String optionKey, String shortKey, boolean requiresArgument) {
            String desc = message("opt." + optionKey);
            Option opt = new Option(shortKey, optionKey, requiresArgument, desc);
            options.addOption(opt);
            return opt;
        }

        static boolean boolValue(CommandLine cl, String optionKey) {
            final boolean hasOption = cl.hasOption(optionKey);
            log.debug(() -> String.format("option: hasOption=%s, key=%s", (hasOption ? "T" : "F"), optionKey));
            return hasOption;
        }

        static OptionalInt optIntValue(CommandLine cl, String optionKey) {
            log.debug(() -> String.format("option: hasOption=%s, key=%s, value=%s",
                    (cl.hasOption(optionKey) ? "T" : "F"), optionKey, cl.getOptionValue(optionKey)));
            return optInt(cl, optionKey);
        }

        static OptionalInt optIntValue(CommandLine cl, String optionKey, String optionKey2, int value2) {
            log.debug(() -> String.format("option: hasOption=%s, key=%s, value=%s, key2=%s, value2=%d",
                    (cl.hasOption(optionKey) ? "T" : "F"), optionKey, cl.getOptionValue(optionKey), optionKey2,
                    value2));
            OptionalInt v = optInt(cl, optionKey);
            if (v.isPresent())
                return v;
            return boolValue(cl, optionKey2) ? OptionalInt.of(value2) : OptionalInt.empty();
        }

        private static OptionalInt optInt(CommandLine cl, String optionKey) {
            if (!cl.hasOption(optionKey))
                return OptionalInt.empty();
            String v = cl.getOptionValue(optionKey);
            try {
                return OptionalInt.of(Integer.parseUnsignedInt(v));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(message("e.argOptionMustPositiveNumber", optionKey, v), e);
            }
        }

        static String stringValue(CommandLine cl, String optionKey) {
            String value = cl.getOptionValue(optionKey);
            log.debug(() -> String.format("option: hasOption=%s, key=%s, values=%s",
                    (cl.hasOption(optionKey) ? "T" : "F"), optionKey, value));
            return value;
        }

        static List<String> stringValues(CommandLine cl, String optionKey) {
            String[] values = cl.getOptionValues(optionKey);
            String[] a = (values == null) ? new String[0] : values;
            log.debug(() -> String.format("option: hasOption=%s, key=%s, values=%s",
                    (cl.hasOption(optionKey) ? "T" : "F"), optionKey, Arrays.toString(a)));
            return Arrays.asList(a);
        }

    }

}
