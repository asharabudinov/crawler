package com.win.alex;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Crawler {

    private static final Logger log = LoggerFactory.getLogger(Crawler.class);
    private static String CHARSET_NAME = "utf8";
    private final String KEY = "id";
    private final String VALUE = "make-everything-ok-button";


    public Optional<String> lookup(String[] args) {
        File originFile = new File(args[0]);
        File targetFile = new File(args[1]);

        Attributes attributes = initializeAttributes(args);

        Optional<Element> originElement = findElementByAttributes(attributes, originFile);

        Optional<String> result = originElement.map(toLookFor -> {
            Optional<Element> diffElement = findElementByAttributes(toLookFor.attributes(), targetFile);
            return diffElement.map(XPathParser::buildXPathString).orElseThrow(() -> new RuntimeException("Element not found"));
        });
        result.ifPresent(xpath -> log.info("XPath to element like {} from origin, in {}: \n {}", attributes.asList(),
                args[1].substring(args[1].lastIndexOf("/") + 1, args[1].length()), xpath));
        return result;
    }

    /**
     * Initialize origin file target element attributes or set default one
     * @param args command line args
     * @return Attributes object
     */
    private Attributes initializeAttributes(String[] args) {
        Attributes attributes = new Attributes();
        if (args.length > 2 && !args[2].isEmpty() && !args[3].isEmpty()) {
            log.info("Using custom params: " + args[2] +"="+ args[3]);
            attributes.put(args[2], args[3]);
        } else {
            attributes.put(KEY, VALUE);
        }
        return attributes;
    }

    /**
     * Finds all elements for each attribute
     * @param attributes by which target element should be found
     * @param htmlFile where Element will be looked for
     * @return Element which had most hits for attributes
     */
    private Optional<Element> findElementByAttributes(Attributes attributes, File htmlFile) {
        try {
            Document doc = Jsoup.parse(htmlFile, CHARSET_NAME);
            final Map<Element, AtomicInteger> elementsByAppearance = new HashMap<>();

            attributes.asList().forEach(attribute -> {
                Elements targets = doc.getElementsByAttributeValue(attribute.getKey(), attribute.getValue());
                targets.forEach(target -> {
                    AtomicInteger count = elementsByAppearance.get(target);
                    if(count == null) {
                        count = new AtomicInteger();
                    }
                    count.incrementAndGet();
                    elementsByAppearance.put(target, count);
                });
            });
            return Optional.ofNullable(getMostFrequentlyAppearedElement(elementsByAppearance));

        } catch (IOException e) {
            log.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    public Element getMostFrequentlyAppearedElement(Map<Element, AtomicInteger> unsortMap) {
        List<Map.Entry<Element, AtomicInteger>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort(Comparator.comparing(entry -> entry.getValue().get(), Comparator.reverseOrder()));
        return list.get(0).getKey();
    }


}
