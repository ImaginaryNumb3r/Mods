package lib;

import parsing.xml.XMLTag;

/**
 * Creator: Patrick
 * Created: 29.05.2019
 * Purpose:
 */
public class XML {

    public static String getData(XMLTag tag) {
        return tag.toString().split(">")[1].split("<")[0];
    }

    public static String getData(XMLTag tag, String childName) {
        XMLTag node = getChild(tag, childName);

        return node.toString().split(">")[1].split("<")[0];
    }

    public static XMLTag getChild(XMLTag tag, String childName) {
        return tag.children().stream()
                .filter(node -> node.getName().equals(childName))
                .findFirst()
                .get();
    }
}
