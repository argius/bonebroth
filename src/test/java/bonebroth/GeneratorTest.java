package bonebroth;

import static org.junit.Assert.assertEquals;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.velocity.*;
import org.junit.*;
import org.junit.rules.*;

public final class GeneratorTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    private final ResultBuffer buf = new ResultBuffer();

    @Test
    public void testCreateForFile() {
        Generator g = Generator.createForFile();
        Path vmFile = createTemplateFile("#foreach( $i in [1, 3, 5] )$foreach.count-$i #end $msg");
        VelocityContext ctx = new VelocityContext();
        ctx.put("msg", "hello");
        g.generate(vmFile, ctx, buf.cleared());
        assertEquals("1-1 2-3 3-5  hello", StringUtils.chomp(buf.getResultString()));
        g.generate(vmFile.toAbsolutePath(), ctx, buf.cleared());
        assertEquals("1-1 2-3 3-5  hello", StringUtils.chomp(buf.getResultString()));
    }

    @Test
    public void testCreateForResource() throws IOException {
        Generator g = Generator.createForResource();
        File f = tmpFolder.newFile("beanconf.csv");
        TestUtils.writeResourceToFile("beanconf.csv", f);
        VelocityContext ctx = new VelocityContext();
        ContextHelper.readFile(f, ctx);
        g.generate("bean", ctx, buf.cleared());
    }

    @Test
    public void testGenerateStringVelocityContextWriter() {
        // skip
    }

    @Test
    public void testGeneratePathVelocityContextPrintWriter() {
        // skip
    }

    @Test
    public void testGetVelocityProperties() {
        // skip
    }

    Path createTemplateFile(String... lines) {
        try {
            final Path path = tmpFolder.newFile().toPath();
            Files.write(path, Arrays.asList(lines));
            return path;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @SuppressWarnings("unused")
    private static final class ResultBuffer extends PrintWriter {

        ResultBuffer() {
            super(new StringWriter(), true);
        }

        void clear() {
            cleared();
        }

        ResultBuffer cleared() {
            this.out = new StringWriter();
            return this;
        }

        String getResultString() {
            return out.toString();
        }

    }

}
