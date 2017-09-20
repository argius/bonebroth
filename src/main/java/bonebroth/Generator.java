package bonebroth;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.apache.commons.lang3.builder.*;
import org.apache.velocity.*;
import org.apache.velocity.app.*;

/**
 * This is a wrapper class of Velocity.
 */
public final class Generator {

    private static final Log log = Log.logger(App.class);

    enum LoaderType {
        FILE, CLASSPATH;
    }

    private static final Class<?> CLASS = Generator.class;
    private static final String PACKAGE_NAME = CLASS.getPackage().getName();

    private final VelocityEngine velocityEngine;

    private Generator(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public static Generator createForFile() {
        return new Generator(createEngine(LoaderType.FILE));
    }

    public static Generator createForResource() {
        return new Generator(createEngine(LoaderType.CLASSPATH));
    }

    public void generate(String templateId, VelocityContext ctx, Writer writer) {
        final String templatePath = String.format("/%s/%s.vm", PACKAGE_NAME, templateId);
        merge(templatePath, ctx, writer);
    }

    public void generate(Path templatePath, VelocityContext ctx, PrintWriter writer) {
        final String parentPath;
        final String childPath;
        if (templatePath.isAbsolute()) {
            final String absolutePath = templatePath.toAbsolutePath().toString();
            // FIXME ad-hoc driver letter handling
            final int index;
            if (absolutePath.matches("^[A-Z]:\\\\.+")) {
                index = 3;
            }
            else {
                index = 1;
            }
            parentPath = absolutePath.substring(0, index);
            childPath = absolutePath.substring(index);
        }
        else {
            parentPath = "./";
            childPath = templatePath.toString();
        }
        velocityEngine.addProperty("FILE.resource.loader.path", parentPath);
        log.debug(() -> "template file: " + parentPath + " + " + childPath);
        merge(childPath, ctx, writer);
        writer.flush();
    }

    private void merge(final String templateLocation, VelocityContext ctx, Writer writer) {
        log.debug(() -> "ctx=" + ReflectionToStringBuilder.toString(ctx));
        VelocityContext newCtx = new VelocityContext(ctx);
        newCtx.put("generator", App.version());
        newCtx.put("util", ContextUtilities.class);
        velocityEngine.getTemplate(templateLocation, AppProperties.getCharsetForTemplate().name()).merge(newCtx, writer);
    }

    private static VelocityEngine createEngine(LoaderType loaderType) {
        return new VelocityEngine(getVelocityProperties(loaderType));
    }

    static Properties getVelocityProperties(LoaderType loaderType) {
        Properties p = new Properties();
        try (InputStream is = CLASS.getResourceAsStream("velocity.properties")) {
            p.load(is);
            p.put("resource.loader", loaderType.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return p;
    }

}
