package demo;

import java.util.List;
import java.util.concurrent.Callable;

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
