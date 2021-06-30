package xyz.moment.selfcare.utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.moment.selfcare.model.Article;
import xyz.moment.selfcare.model.Detail;
import xyz.moment.selfcare.model.Section;

public class GetArticle {

    private static final String TAG = "GetArticle";

    public static Article get(String url) {
        Article article = null;
        String html = OkGetArt(url);
        Log.d(TAG, "get: html="+html);
        article = crawlArticle(html);
        return article;
    }

    public static Article crawlArticle(String html) {

        Document document = Jsoup.parse(html);
        String title = document.select("h1[class=topic__header__headermodify--title " +
                "topic__header__headermodify--title--animate]").text();
        String author = document.select("strong[class=topic__label topic__label--author]")
                .select("a").text();
        String topicExplanation = document.select("div[class=topic__explanation]").text();

        Elements sectionElements = document.select("div[class=topic__accordion]")
                                           .select("section");
        List<Section> sections = new ArrayList<>();
        for(Element element : sectionElements) {
            Detail detail = new Detail();
            List<String> items = new ArrayList<>();
            String sTitle = element.select("h2").text();
            String dTitle = element.select("div[class=topic__content] > div[class=para] > p").text();
            detail.setdTitle(dTitle);
            Log.d(TAG, "crawlArticle: sTitle="+sTitle);
            Elements detailElements = element.select("div").select("div[class=list]")
                                             .select("ul[class=bulleted]");
            for(Element ele : detailElements) {
//                String item = ele.select("li").select("div[class=para]").select("p").text();
                String item = ele.select("li > div[class=para] > p").text();
                String a,b;
                Log.d(TAG, "crawlArticle: detail item="+item);
                int j = 0;
                List<String> para = new ArrayList<>();
                char[] itemChars= item.toCharArray();
                for(int i = 1; i < item.length(); i++) {
                    if(Character.isSpaceChar(itemChars[i]) && !Character.isDigit(itemChars[i-1])
                            && itemChars[i-1] != 'X' && itemChars[i+1] != '('
                            &&itemChars[i+1] != '（'
                    ) {
                        if(itemChars[i+1] == '[')
                            continue;
                        Log.d(TAG, "crawlArticle: j = "+j+", i = "+i+"["+itemChars[j]+"--"+itemChars[i-1]+"]");
                        a = item.substring(j,i);
                        j = i;
                        a = "&#8226; " + a + "<br>";
                        Log.d(TAG, "crawlArticle: a="+a);
                        para.add(a);
                    }
                    if(i == itemChars.length-1) {
                        a = item.substring(j, i+1);
                        a = "&#8226; " + a + "<br>";
                        Log.d(TAG, "crawlArticle: a="+a);
                        para.add(a);
                    }
                }
//                detail.add();
                //detail.setItems(para.toString().replace("[", "").replace("]", "").replace(",",""));
                items.add(para.toString().replace("[", "").replace("]", "").replace(",",""));
                Log.d(TAG, "crawlArticle: detail item="+item);
            }

            detail.setItems(items);

            if("".equals(sTitle))
                continue;
            Section section = new Section(sTitle, detail);
            sections.add(section);
        }

        Elements ulElements = document.select("article[class=topic__article]")
                .select("div[class=topic__accordion]")
                .select("div[class=list]")
                .select("ul[class=bulleted]")
                .select("li > div[class=para] > p")
                ;

        List<String> topicItem = new ArrayList<>();
       /* Element topicElement = ulElements.first();
        String item = topicElement.select("p").text();
        topicItem.add(item);*/
        int i = 0;
        for(Element element : ulElements) {
            Log.d(TAG, "crawlArticle: element.outerHtml()="+element.outerHtml());
            String item = element.text();
            topicItem.add(item);
            i++;
            if(i > 4)
                break;
        }

        Article article = new Article(title, author, topicExplanation, topicItem, sections);
        //Log.d(TAG, "crawlArticle: article.getTitle()="+article.getTitle());
        Log.d(TAG, "crawlArticle: article.toString()="+article.toString());
        return article;
    }

    public static String OkGetArt(String url) {
        String html="";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try  {
            Response response = client.newCall(request).execute();
            //return
            html = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }
}
