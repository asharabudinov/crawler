package com.win.alex;

import org.junit.Assert;
import org.junit.Test;

public class CrawlerTest {
    private static final String origin ="sample-0-origin.html";
    private static final String test1 ="sample-1-evil-gemini.html";
    private static final String test2 ="sample-2-container-and-clone.html";
    private static final String test3 ="sample-3-the-escape.html";
    private static final String test4 ="sample-4-the-mash.html";

    private static final String test1_result = "html > body > div > div > div[2] > div > div > div[1] > a[1]";
    private static final String test2_result = "html > body > div > div > div[2] > div > div > div[1] > div > a";
    private static final String test3_result = "html > body > div > div > div[2] > div > div > div[2] > a";
    private static final String test4_result = "html > body > div > div > div[2] > div > div > div[2] > a";

    @Test
    public void testCrawler() {
        String originPath = this.getClass().getClassLoader().getResource(origin).getPath();
        Crawler crawler = new Crawler();
        Assert.assertEquals(test1_result, crawler.lookup(new String[]{originPath, this.getClass().getClassLoader().getResource(test1).getPath()}).get());
        Assert.assertEquals(test2_result, crawler.lookup(new String[]{originPath, this.getClass().getClassLoader().getResource(test2).getPath()}).get());
        Assert.assertEquals(test3_result, crawler.lookup(new String[]{originPath, this.getClass().getClassLoader().getResource(test3).getPath()}).get());
        Assert.assertEquals(test4_result, crawler.lookup(new String[]{originPath, this.getClass().getClassLoader().getResource(test4).getPath()}).get());
    }

}
