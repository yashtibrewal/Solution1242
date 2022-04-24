package demo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public static synchronized List<String> getVisitedUrls(){
        return new ArrayList<>(urlVisitHistory);
    }
}
