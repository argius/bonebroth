package bonebroth;

import java.util.stream.*;
import org.apache.commons.lang3.*;

public interface StringBehavior {

    default String[] splitWords(String s) {
        if (s.contains("_") || s.contains("-")) {
            return s.split("_|-");
        }
        else {
            // assume camel case
            return StringUtils.splitByCharacterTypeCamelCase(s);
        }
    }

    default String toCapitalCase(String s) {
        return StringUtils.capitalize(s);
    }

    default String toCamelCase(String s) {
        return StringUtils.uncapitalize(toPascalCase(s));
    }

    default String toPascalCase(String s) {
        return Stream.of(splitWords(s)).map(String::toLowerCase).map(this::toCapitalCase).collect(Collectors.joining());
    }

    default String toSnakeCase(String s) {
        return String.join("_", splitWords(s)).toLowerCase();
    }

    default String toChainCase(String s) {
        return String.join("-", splitWords(s)).toLowerCase();
    }
}
