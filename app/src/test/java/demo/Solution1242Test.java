package demo;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

public class Solution1242Test {
    
    @Test
    public void testCrawler1() throws MalformedURLException, InterruptedException, ExecutionException{
        Solution1242 solution1242 = new Solution1242();
        HtmlParser htmlParser = new HtmlParserImpl();
        String startUrl = "http://news.yahoo.com/news/topics/";
        List<String> results = solution1242.crawl(startUrl, htmlParser);
        List<String> expected = new ArrayList<String>();
        expected.add("http://news.yahoo.com");
        expected.add("http://news.yahoo.com/news");
        expected.add("http://news.yahoo.com/news/topics/");
        expected.add("http://news.yahoo.com/us");
        assertEquals(expected, results);
    }

}
