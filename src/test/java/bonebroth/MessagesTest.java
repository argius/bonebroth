package bonebroth;

import static org.junit.Assert.assertThat;
import org.hamcrest.*;
import org.junit.*;

public final class MessagesTest {

    @Test
    public void testMessage() {
        assertThat(Messages.message("dummy"), Matchers.allOf(Matchers.startsWith("!"), Matchers.endsWith("!")));
        assertThat(Messages.message("xxx", 1), Matchers.allOf(Matchers.startsWith("!"), Matchers.endsWith("[1]")));
    }

}
