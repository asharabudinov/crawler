package com.win.alex;

import org.jsoup.nodes.Element;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class XPathParser {

    /**
     * Builds XPath like string for  specified @param Element
     */
    public static String buildXPathString(Element element) {
        StringBuilder builder = new StringBuilder();
        recursivelyAppendParentNodes(element, builder);
        builder.replace(0, 3, "");
        return builder.toString();
    }

    private static void recursivelyAppendParentNodes(Element current, StringBuilder builder) {
        Element parent = current.parent();
        if (parent != null) {
            String childIndexPrefix = getChildIndexPrefix(parent, current);
            builder.insert(0, childIndexPrefix)
                    .insert(0, current.tag().getName())
                    .insert(0, " > ");
            recursivelyAppendParentNodes(parent, builder);
        }
    }

    private static String getChildIndexPrefix(Element parent, Element current) {
        AtomicInteger index = new AtomicInteger();

        parent.children().stream()
                .filter(node -> node.tagName().equals(current.tagName()))
                .collect(Collectors.toList()) // filtered TextNodes
                    .stream()
                    .filter(child -> {
                        if (child.attributes().equals(current.attributes()) && child.childNodes().equals(current.childNodes())) {
                            return true;
                        }
                        index.incrementAndGet(); // if attributes isn't equals increment index
                        return false;
        }).findFirst();

        if (index.get() > 0) {
            return String.format("[%d]", index.get());
        }
        return "";
    }

}
