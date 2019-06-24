package lib;

/**
 * Creator: Patrick
 * Created: 29.05.2019
 * Purpose:
 */
public class StringUtils {

    public static String[] map(Object[] objects) {
        String[] strings = new String[objects.length];

        int i = 0;
        for (Object object : objects) {
            strings[i] = object.toString();
            ++i;
        }

        return strings;
    }

}
