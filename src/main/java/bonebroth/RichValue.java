package bonebroth;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * This class has redundant converter methods for dynamic languages.
 */
public final class RichValue implements StringBehavior, CharSequence {

    private static final String LIST_VALUE_SEPARATOR_PATTERN = "[\\|,]";

    private final String value;

    public static RichValue of(String s) {
        return new RichValue(s);
    }

    private static RichValue newValue(String s) {
        return new RichValue(s);
    }

    private RichValue(String value) {
        this.value = value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return value.subSequence(beginIndex, endIndex);
    }

    public RichValue toUpperCase() {
        return newValue(value.toUpperCase());
    }

    public RichValue getUpper() {
        return toUpperCase();
    }

    public RichValue toLowerCase() {
        return newValue(value.toLowerCase());
    }

    public RichValue getLower() {
        return toLowerCase();
    }

    public RichValue toCapitalCase() {
        return newValue(toCapitalCase(value));
    }

    public RichValue getCapital() {
        return toCapitalCase();
    }

    public RichValue toCamelCase() {
        return newValue(toCamelCase(value));
    }

    public RichValue getCamel() {
        return toCamelCase();
    }

    public RichValue toPascalCase() {
        return newValue(toPascalCase(value));
    }

    public RichValue getPascal() {
        return toPascalCase();
    }

    public List<RichValue> list() {
        return stream().map(x -> new RichValue(x)).collect(Collectors.toList());
    }

    public RichValue toSnakeCase() {
        return newValue(toSnakeCase(value));
    }

    public RichValue getSnake() {
        return toSnakeCase();
    }

    public RichValue toChainCase() {
        return newValue(toChainCase(value));
    }

    public RichValue getChain() {
        return toChainCase();
    }

    private Stream<String> stream() {
        return Pattern.compile(LIST_VALUE_SEPARATOR_PATTERN).splitAsStream(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
