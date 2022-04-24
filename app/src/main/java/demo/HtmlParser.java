package demo;
import java.util.ArrayList;
import java.util.List;

interface HtmlParser {
    public List<String> getUrls(String url);
}
class HtmlParserImpl implements HtmlParser{

    // @Override
    // public List<String> getUrls(String url) {
    //     ArrayList<String> list = new ArrayList<>();
    //     if(url.equals("http://news.yahoo.com/news/topics/")){
    //         list.add("http://news.yahoo.com/news");
    //         list.add("http://news.google.com");
    //         list.add("http://news.yahoo.com/");
    //     }else if(url.equals("http://news.yahoo.com/")){
    //         list.add("http://news.yahoo.com/us");
    //     }else if(url.equals("http://news.yahoo.com/news")){
    //         list.add("http://news.google.com");
    //     }
    //     return list;
    // }

    @Override
    public List<String> getUrls(String url) {
        ArrayList<String> list = new ArrayList<>();
        if(url.equals("http://news.google.com")){
            list.add("http://news.yahoo.com/news");
            list.add("http://news.yahoo.com/news/topics/");
            list.add("http://news.yahoo.com/");
        }else if(url.equals("http://news.yahoo.com/")){
            list.add("http://news.yahoo.com/news/topics/");
        }else if(url.equals("http://news.yahoo.com/news/topics/")){
            list.add("http://news.yahoo.com/news");
        }
        return list;
    }
    
}