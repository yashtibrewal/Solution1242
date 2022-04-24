package demo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FilterUrlService {
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

    public void waitForFutureUrlStrings() throws InterruptedException, ExecutionException{
        List<String> urlStrings = futureUrlStrings.get();
        this.allUrls = new ArrayList<>(urlStrings);
    }

    public void filterUrls() throws MalformedURLException {
        URL url;
        for (String entry : this.allUrls) {
            url = new URL(entry);
            if (url.getHost().equals(hostname)) {
                filteredUrls.add(entry);
            }
        }
    }

    public List<String> getFilteredUrls() {
        return this.filteredUrls;
    }
}
