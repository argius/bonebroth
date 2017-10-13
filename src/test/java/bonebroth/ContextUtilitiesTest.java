package bonebroth;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public final class ContextUtilitiesTest {

    @Test
    public void testEnrich() {
        assertEquals("context_utilities", ContextUtilities.enrich("ContextUtilities").toSnakeCase().toString());
    }

}
