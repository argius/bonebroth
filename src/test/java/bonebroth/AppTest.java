package bonebroth;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.velocity.*;
import org.junit.*;
import org.junit.rules.*;

public final class AppTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void testGenBean() throws IOException {
        Generator g = Generator.createForResource();
        File f = tmpFolder.newFile("beanconf.csv");
        try (InputStream res = getClass().getResourceAsStream("beanconf.csv")) {
            List<String> lines = IOUtils.readLines(res, StandardCharsets.UTF_8);
            FileUtils.writeLines(f, lines);
        }
        VelocityContext ctx = new VelocityContext();
        ContextHelper.readFile(f, ctx);
        PrintWriter out = new PrintWriter(System.out, true);
        g.generate("bean", ctx, out);
        out.flush();
    }

}
