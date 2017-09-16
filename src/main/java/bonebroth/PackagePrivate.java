package bonebroth;

import java.util.*;

/**
 * A class has package-private methods. The methods in this class may be always removed.
 */
final class PackagePrivate {

    private PackagePrivate() {
    }

    static ResourceBundle getResourceBundle(Class<?> c) {
        final String s = c.getSimpleName();
        final String baseName = c.getPackage().getName() + '/' + s.substring(0, 1).toLowerCase() + s.substring(1);
        return ResourceBundle.getBundle(baseName);
    }

}
