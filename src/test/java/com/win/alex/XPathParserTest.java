package com.win.alex;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;



public class XPathParserTest {

    @Test
    public void testXpathLine() throws IOException {
        File htmlFile = new File("src/test/resources/test.html");
        Document doc = Jsoup.parse(htmlFile, "utf-8");
        Elements elements = doc.getElementsByAttributeValue("class", "target");
        Element element = elements.get(0);
        String xPathString = XPathParser.buildXPathString(element);
        String[] xPathElements = xPathString.split(" > ");
        Assert.assertEquals(8, xPathElements.length);
        Assert.assertEquals("html", xPathElements[0]);
        Assert.assertEquals("body", xPathElements[1]);
        Assert.assertEquals("div", xPathElements[2]);
        Assert.assertEquals("div[1]", xPathElements[3]);
        Assert.assertEquals("div[2]", xPathElements[4]);
        Assert.assertEquals("div", xPathElements[5]);
        Assert.assertEquals("ul", xPathElements[6]);
        Assert.assertEquals("li[3]", xPathElements[7]);

    }

}
