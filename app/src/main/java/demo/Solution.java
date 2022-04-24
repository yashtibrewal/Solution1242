package demo;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.HashSet;
import java.util.concurrent.Callable;

class Solution {

    class FilterUrlService {
        private List<String> filteredUrls;
        private List<String> allUrls;
        private String hostname;
        private Future<List<String>> futureUrlStrings;

        FilterUrlService(String hostname, Future<List<String>> futureUrlString) {
            this.hostname = hostname;
            this.filteredUrls = new ArrayList<>();
            this.allUrls = new ArrayList<>();
            this.futureUrlStrings = futureUrlString;
        }

        public void waitForFutureUrlStrings() throws InterruptedException, ExecutionException {
            List<String> urlStrings = futureUrlStrings.get();
            this.allUrls = new ArrayList<>(urlStrings);
        }

        public void filterUrls() throws MalformedURLException {
            URL url;
            for (String entry : this.allUrls) {
                url = new URL(entry);
                if (url.getHost().equals(this.hostname)) {
                    filteredUrls.add(entry);
                }
            }
        }

        public List<String> getFilteredUrls() {
            return this.filteredUrls;
        }
    }

    class WebAPIService implements Callable<List<String>> {
        private String url;
        private HtmlParser htmlParser;

        WebAPIService(String url, HtmlParser htmlParser) {
            this.url = url;
            this.htmlParser = htmlParser;
        }

        @Override
        public List<String> call() {
            return this.htmlParser.getUrls(url);
        }
    }

    class WebCrawler {

        private HtmlParser htmlParser;
        private ExecutorService executorService;
        private List<Future<List<String>>> listOfFuturesStrings;
        private String hostname;

        WebCrawler(HtmlParser htmlParser, String hostname) {
            executorService = Executors.newFixedThreadPool(4);
            this.htmlParser = htmlParser;
            listOfFuturesStrings = new ArrayList<>();
            this.hostname = hostname;
        }

        public void setHtmlParser(HtmlParser htmlParser) {
            this.htmlParser = htmlParser;
        }

        public void shutDown() throws InterruptedException {
            executorService.shutdown();
            boolean allDone = true;
            for (Future<?> future : listOfFuturesStrings) {
                allDone &= future.isDone(); // check if future is done
            }
            System.out.println(allDone);
        }

        public Future<List<String>> pingAPI(String startingUrlString) throws InterruptedException, ExecutionException {

            WebAPIService webAPIService;
            Future<List<String>> webAPIServiceFuture;

            webAPIService = new WebAPIService(startingUrlString, htmlParser);
            webAPIServiceFuture = executorService.submit(webAPIService);
            listOfFuturesStrings.add(webAPIServiceFuture);
            return webAPIServiceFuture;
        }

        public List<String> getFilteredStrings(Future<List<String>> urlStringsFutures)
                throws MalformedURLException, InterruptedException, ExecutionException {

            FilterUrlService filterUrlService;
            filterUrlService = new FilterUrlService(this.hostname, urlStringsFutures);
            filterUrlService.waitForFutureUrlStrings();
            filterUrlService.filterUrls();
            return filterUrlService.getFilteredUrls();

        }

        public List<String> useWebServiceAndGetFilteredUrls(String startingUrlString)
                throws InterruptedException, ExecutionException, MalformedURLException {

            // Declarations
            Future<List<String>> urlStringsFutures;

            // submiting a thread
            urlStringsFutures = pingAPI(startingUrlString);

            // filtering the required urls
            return getFilteredStrings(urlStringsFutures);
        }

        public void runWebCrawler(String startingUrlString)
                throws MalformedURLException, InterruptedException, ExecutionException {

            // if its visited do not visit again.
            if (VisitHistoryUrlService.isVisited(startingUrlString))
                return;

            List<String> filteredUrls;
            VisitHistoryUrlService.addToVisited(startingUrlString);
            filteredUrls = useWebServiceAndGetFilteredUrls(startingUrlString);
            for (String entryUrl : filteredUrls) {
                runWebCrawler(entryUrl);
            }

        }
    }

    /**
     * Since hashset is not synchronized we are wrapping it in synchronized methods
     */
    class VisitHistoryUrlService {
        private VisitHistoryUrlService() {
        }

        private static HashSet<String> urlVisitHistory = new HashSet<>();

        public static synchronized boolean isVisited(String url) {
            return urlVisitHistory.contains(url);
        }

        public static synchronized boolean addToVisited(String url) {
            return urlVisitHistory.add(url);
        }

        public static synchronized List<String> getVisitedUrls() {
            return new ArrayList<>(urlVisitHistory);
        }
    }

    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        try {
            URL url = new URL(startUrl);
            WebCrawler webCrawler = new WebCrawler(htmlParser, url.getHost());
            webCrawler.runWebCrawler(startUrl);
            webCrawler.shutDown();
        } catch (MalformedURLException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return VisitHistoryUrlService.getVisitedUrls();
    }

    // public static void main(String[] args) throws MalformedURLException, InterruptedException, ExecutionException {
    //     Solution1242 solution1242 = new Solution1242();
    //     HtmlParser htmlParser = new HtmlParserImpl();
    //     String startUrl = "http://news.google.com";
    //     List<String> results = solution1242.crawl(startUrl, htmlParser);
    //     System.out.println(results);
    // }
}