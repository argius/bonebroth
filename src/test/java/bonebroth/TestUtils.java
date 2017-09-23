package bonebroth;

import java.io.*;
import java.nio.charset.*;
import org.apache.commons.io.*;

public final class TestUtils {

    private static final Class<?> CLASS = App.class; // for resource loading

    private TestUtils() {
    }

    public static void writeResourceToFile(String resoureLocation, File file) throws IOException {
        try (InputStream res = CLASS.getResourceAsStream(resoureLocation); FileWriter fw = new FileWriter(file)) {
            IOUtils.copy(res, fw, StandardCharsets.UTF_8);
        }
    }

}
