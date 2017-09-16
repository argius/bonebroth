package bonebroth;

import static bonebroth.Messages.message;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.apache.commons.cli.*;
import org.apache.commons.io.output.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;

public final class App {

    private static final Log log = Log.logger(App.class);

    private App() {
    }

    static void generate(OptionSet opts) {
        log.debug(() -> "generate start");
        final String template = StringUtils.defaultString(opts.getTemplate());
        // TODO input allows csv array
        String.join(",", opts.getInput());
        final String input = StringUtils.defaultString(opts.getInput());
        final Optional<String> outputOpt = Optional.ofNullable(opts.getOutput());
        if (opts.isDoMkdirs() && outputOpt.isPresent()) {
            mkdirsIfDirNotExists(Paths.get(outputOpt.get()).getParent());
        }
        try (PrintWriter out = getPrintWriter(outputOpt)) {
            Generator g = Generator.createForFile();
            Path path = Paths.get(template);
            g.generate(path, ContextHelper.read(input), out);
        }
        log.debug(() -> "generate end");
    }

    static void generateBean(OptionSet opts) {
        log.debug(() -> "generateBean start");
        final String input = opts.getInput();
        final Optional<String> outputOpt = Optional.ofNullable(opts.getOutput());
        if (opts.isDoMkdirs() && outputOpt.isPresent()) {
            mkdirsIfDirNotExists(Paths.get(outputOpt.get()).getParent());
        }
        try (PrintWriter out = getPrintWriter(outputOpt)) {
            Generator g = Generator.createForResource();
            g.generate("bean", ContextHelper.read(input), out);
            out.flush();
        }
        log.debug(() -> "generateBean end");
    }

    // for shield
    @SuppressWarnings("resource")
    static PrintWriter getPrintWriter(Optional<String> outputOpt) {
        try {
            return outputOpt.isPresent() ? new PrintWriter(outputOpt.get(), AppProperties.getCharsetForOutput().name())
                    : new PrintWriter(new CloseShieldOutputStream(System.out));
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void mkdirsIfDirNotExists(Path dir) {
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                log.error(() -> "failed to create directories", e);
                throw new UncheckedIOException(e);
            }
        }
    }

    static void showHelp() {
        log.debug(() -> "start showHelp");
        HelpFormatter hf = new HelpFormatter();
        hf.setSyntaxPrefix(message("i.usagePrefix"));
        String usage = message("i.usage");
        String header = message("help.header");
        String footer = message("help.footer");
        hf.printHelp(80, usage, header, new OptionSet.Parser().getOptions(), footer);
        log.debug(() -> "end showHelp");
    }

    public static String version() {
        StringBuilder sb = new StringBuilder();
        sb.append(message(".productName")).append(" version ");
        try (InputStream is = App.class.getResourceAsStream("version")) {
            if (is == null) {
                sb.append("???");
            }
            else {
                @SuppressWarnings("resource")
                Scanner sc = new Scanner(is);
                sb.append(sc.nextLine());
            }
        } catch (IOException | NoSuchElementException e) {
            log.warn(() -> "App.version", e);
            sb.append("?");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        log.info(() -> "start (version: " + version() + ")");
        log.debug(() -> "args=" + Arrays.asList(args));
        try {
            OptionSet opts = OptionSet.parseArguments(args);
            log.info(() -> "opts=" + ReflectionToStringBuilder.toString(opts));
            if (opts.isShowVersion()) {
                System.err.println(version());
            }
            else if (opts.isHelp()) {
                showHelp();
            }
            else if (opts.isGeneratesBuildInBean()) {
                if (opts.getInput() == null) {
                    throw new IllegalArgumentException("bean-mode requires input");
                }
                generateBean(opts);
            }
            else {
                if (StringUtils.isBlank(opts.getTemplate()) || StringUtils.isBlank(opts.getInput())) {
                    throw new IllegalArgumentException("normal-mode requires template and input");
                }
                generate(opts);
            }
        } catch (Exception e) {
            log.error(() -> "", e);
            System.err.println(message("e.0", e.getMessage()));
        } finally {
            log.info(() -> "end");
        }
    }

}
