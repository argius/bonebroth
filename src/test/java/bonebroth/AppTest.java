package bonebroth;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import org.apache.commons.io.*;
import org.junit.*;
import org.junit.rules.*;

public final class AppTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void testMain() throws IOException {
        // bean mode
        File f = tmpFolder.newFile("beanconf.csv");
        try (InputStream res = getClass().getResourceAsStream("beanconf.csv")) {
            List<String> lines = IOUtils.readLines(res, StandardCharsets.UTF_8);
            FileUtils.writeLines(f, lines);
        }
        App.main("--bean", "-i", f.getAbsolutePath());
    }

}
