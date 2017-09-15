package bonebroth;

/**
 * Utilities in context.
 */
public final class ContextUtilities {

    private ContextUtilities() {
    }

    public static RichValue enrich(String s) {
        return RichValue.of(s);
    }

}
