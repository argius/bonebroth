package bonebroth;

import static org.junit.Assert.assertEquals;
import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import org.apache.velocity.*;
import org.junit.*;
import org.junit.rules.*;

public final class ContextHelperTest {

    @Rule
    public TestName testName = new TestName();

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void testReadCsv() throws IOException {
        final String methodName = testName.getMethodName();
        File f = new File(tmpFolder.getRoot(), methodName + ".csv");
        try (InputStream is = getClass().getResourceAsStream("/example.csv")) {
            FileUtils.copyInputStreamToFile(is, f);
        }
        VelocityContext ctx = new VelocityContext();
        System.out.println(f.getAbsolutePath());
        ContextHelper.readCsv(f, ctx, ',');
        assertEquals("Example", ctx.get("className"));
        @SuppressWarnings("unchecked")
        List<FieldItem> a = (List<FieldItem>) ctx.get("items");
        assertEquals("example_message", a.get(0).getId().toSnakeCase().toString());
        assertEquals("String", a.get(0).getType());
        assertEquals("example message", a.get(0).getName());
        assertEquals("\"hello\"", a.get(0).getValue());
        assertEquals("endOfData", a.get(2).getId().toCamelCase().toString());
        assertEquals("boolean", a.get(2).getType());
        assertEquals("end of data", a.get(2).getName());
        assertEquals("false", a.get(2).getValue());
    }
}
