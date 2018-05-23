package com.win.alex;

import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FreequencyComparationTest {
    @Test
    public void mapComparatorTest() {
        final Map<Element, AtomicInteger> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            map.put(new Element(""+i), new AtomicInteger(i));
        }
        Element element = new Crawler().getMostFrequentlyAppearedElement(map);
        Assert.assertEquals("4", element.tag().getName());
    }
}
