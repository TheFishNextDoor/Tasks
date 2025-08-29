package fun.sunrisemc.tasks.utils;

public class StringUtils {

    public static String normalizeString(String string) {
        if (string == null) {
            return null;
        }
        return string.trim().toLowerCase().replace(" ", "_").replace("-", "_");
    }
    
}
