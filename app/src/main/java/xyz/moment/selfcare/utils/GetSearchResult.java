package xyz.moment.selfcare.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.moment.selfcare.model.SearchResult;
import xyz.moment.selfcare.model.json.Results;
import xyz.moment.selfcare.model.json.Root;

/*
* 所需数据所在网页是动态页面 直接用OkHttp无法获取完整页面数据
* 通过浏览器控制台审查元素发现一个API
* 所需数据在该API返回的数据中以JSON中的HTML形式出现
* 因此，使用Jsoup爬取该HTML可得所需数据
*/
public class GetSearchResult {

    private static final String TAG = "GetSearchResult";

    public static List<SearchResult> search(String keywords) throws JSONException {
        String url = "https://www.msdmanuals.cn/Redesign/SearchResults/GetSearchResults?query=" +
                keywords + "&oldQuery=&queryLink=https%3A%2F%2Fwww.msdmanuals.cn%2Fhome%2FSearchResults%3Fquery%3D%25E6%2584%259F%25E5%2586%2592&icd9=&icd10=&page=1&isFirstTimeSearch=true&selectedFilters=all&skipItems=0&itemsToLoad=10&referrer=https%3A%2F%2Fwww.msdmanuals.cn%2Fhome%2FSearchResults%3Fquery%3D%25E6%2584%259F%25E5%2586%2592&_=1624323231283" + keywords;
        Log.d(TAG, "search: url=" + url);
        String json = OkGetResult(url);
        return DealWithResults(DealWithJSON(json));
    }

    //获取JSON
    public static String OkGetResult(String url) {
        String json = "";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            //return
            json = response.body().string()
                    .replace("\\r\\n", " ")
                    .replace("\\u003c", "<")
                    .replace("\\u003e", ">")
                    .replace("\\u0027", "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    //将获取的JSON转化为Results对象的集合
    public static List<Results> DealWithJSON(String json) throws JSONException {
        Log.d(TAG, "DealWithJSON: json=" + json);
        JSONObject jsonObject = new JSONObject(json);
        Root root = new Root();
        root.setTotalSearchResults(Integer.parseInt(jsonObject.getString("totalSearchResults")));

        JSONArray results = jsonObject.getJSONArray("results");
        List<Results> resultsList = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject htmlObject = results.getJSONObject(i);
            Results htmlResults = new Results();
            htmlResults.setHtml(htmlObject.getString("html"));
            Log.d(TAG, "DealWithJSON: htmlResults.getHtml()" + htmlResults.getHtml());
            resultsList.add(htmlResults);
        }

        root.setResults(resultsList);
        return resultsList;
    }

    //用爬虫爬取指定数据
    public static SearchResult crawlPage(String html) {
        SearchResult searchResult = null;
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div");
        String title = elements.select("div[class=search__result--main]").select("a[class=search__result--title]").text();
        String type = elements.select("div[class=search__result--type]").select("h1").text();
        String link = "https://www.msdmanuals.cn/"+elements.select("div[class=search__result--main]").select("a[class=search__result--title]").attr("href");
        String shortInfo = elements.select("div[class=search__result--main]").select("p[class=search__result--shortinfo]").text();
        if(title!=null && !"".equals(title)) {
            searchResult = new SearchResult(type, title, link, shortInfo);
            Log.d(TAG, "crawlPage: result=" + searchResult.toString());
        }
        return searchResult;
    }

    //将爬取数据转化为SearchResult对象
    public static List<SearchResult> DealWithResults(List<Results> htmlResults) {
        List<SearchResult> results = new ArrayList<>();
        for (Results htmlResult : htmlResults) {
            Log.d(TAG, "DealWithResults: " + htmlResult.getHtml());
            String html = htmlResult.getHtml().replace("\\\"", "\"");
            Log.d(TAG, "DealWithResults: html=" + html);
            SearchResult searchResult = crawlPage(html);
            if(searchResult!=null) {
                results.add(searchResult);
            }
        }
        Log.d(TAG, "DealWithResults: results.size()=" + results.size());
        return results;
    }
}
