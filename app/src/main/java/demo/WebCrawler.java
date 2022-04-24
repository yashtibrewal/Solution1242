package demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class WebCrawler {

    private HtmlParser htmlParser;
    private ExecutorService executorService;
    private List<Future<List<String>>> listOfFuturesStrings;

    WebCrawler(HtmlParser htmlParser) {
        executorService = Executors.newFixedThreadPool(4);
        this.htmlParser = htmlParser;
        listOfFuturesStrings = new ArrayList<>();
    }

    public void setHtmlParser(HtmlParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    public void shutDown() throws InterruptedException {
        executorService.shutdown();
        boolean allDone = true;
        for(Future<?> future : listOfFuturesStrings){
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

    public List<String> getFilteredStrings(URL startingUrl, Future<List<String>> urlStringsFutures)
            throws MalformedURLException, InterruptedException, ExecutionException {

        FilterUrlService filterUrlService;
        filterUrlService = new FilterUrlService(startingUrl.getHost(), urlStringsFutures);
        filterUrlService.waitForFutureUrlStrings();
        filterUrlService.filterUrls();
        return filterUrlService.getFilteredUrls();

    }

    public List<String> useWebServiceAndGetFilteredUrls(String startingUrlString)
            throws InterruptedException, ExecutionException, MalformedURLException {

        // Declarations
        Future<List<String>> urlStringsFutures;
        URL startingUrl;

        startingUrl = new URL(startingUrlString);

        // submiting a thread
        urlStringsFutures = pingAPI(startingUrlString);

        // filtering the required urls
        return getFilteredStrings(startingUrl, urlStringsFutures);
    }

    public void runWebCrawler(String startingUrlString)
            throws MalformedURLException, InterruptedException, ExecutionException {
        
        // if its visited do not visit again.
        if(VisitHistoryUrlService.isVisited(startingUrlString))return;
        
        List<String> filteredUrls;
        VisitHistoryUrlService.addToVisited(startingUrlString);
        filteredUrls = useWebServiceAndGetFilteredUrls(startingUrlString);
        for (String entryUrl : filteredUrls) {
            runWebCrawler(entryUrl);
        }

    }
}
