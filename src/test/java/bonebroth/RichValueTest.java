package bonebroth;

import static org.junit.Assert.assertEquals;
import java.util.function.*;
import org.junit.*;

public final class RichValueTest {

    @Test
    public void testGetUpper() {
        Function<String, String> f = x -> RichValue.of(x).getUpper().toString();
        assertEquals("TO_UPPER_CASE", f.apply("to_upper_case"));
    }

    @Test
    public void testGetLower() {
        Function<String, String> f = x -> RichValue.of(x).getLower().toString();
        assertEquals("tolowercase", f.apply("ToLowerCase"));
    }

    @Test
    public void testGetCapital() {
        Function<String, String> f = x -> RichValue.of(x).getCapital().toString();
        assertEquals("CapitalCase", f.apply("capitalCase"));
        assertEquals("CAPITAL", f.apply("CAPITAL"));
    }

    @Test
    public void testGetCamel() {
        Function<String, String> f = x -> RichValue.of(x).getCamel().toString();
        assertEquals("toCamelCase", f.apply("toCamelCase"));
        assertEquals("toCamelCase", f.apply("ToCamelCase"));
        assertEquals("toCamelCase", f.apply("TO_CAMEL_CASE"));
    }

    @Test
    public void testGetPascal() {
        Function<String, String> f = x -> RichValue.of(x).getPascal().toString();
        assertEquals("ToPascalCase", f.apply("toPascalCase"));
        assertEquals("ToPascalCase", f.apply("ToPascalCase"));
        assertEquals("ToPascalCase", f.apply("TO_PASCAL_CASE"));
    }

    @Test
    public void testGetSnake() {
        Function<String, String> f = x -> RichValue.of(x).getSnake().toString();
        assertEquals("to_snake_case", f.apply("toSnakeCase"));
        assertEquals("to_snake_case", f.apply("ToSnakeCase"));
        assertEquals("to_snake_case", f.apply("TO_SNAKE_CASE"));
    }

    @Test
    public void testGetChain() {
        Function<String, String> f = x -> RichValue.of(x).getChain().toString();
        assertEquals("to-chain-case", f.apply("toChainCase"));
        assertEquals("to-chain-case", f.apply("ToChainCase"));
        assertEquals("to-chain-case", f.apply("TO_CHAIN_CASE"));
        assertEquals("to-chain-case", f.apply("TO_CHAIN-CASE"));
    }

}
