package demo;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

class Solution1242 {
    public List<String> crawl(String startUrl, HtmlParser htmlParser) throws MalformedURLException, InterruptedException, ExecutionException {
        WebCrawler webCrawler = new WebCrawler(htmlParser);
        webCrawler.runWebCrawler(startUrl);
        webCrawler.shutDown();
        return VisitHistoryUrlService.getVisitedUrls();
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException, ExecutionException {
        Solution1242 solution1242 = new Solution1242();
        HtmlParser htmlParser = new HtmlParserImpl();
        String startUrl = "http://news.yahoo.com/news/topics/";
        List<String> results = solution1242.crawl(startUrl, htmlParser);
        System.out.println(results);
    }
}