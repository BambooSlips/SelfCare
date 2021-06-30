package xyz.moment.selfcare.model;

public class SearchResult {

    private String type;
    private String title;
    private String link;
    private String shortInfo;

    public SearchResult() {
    }

    public SearchResult(String type, String title, String link, String shortInfo) {
        this.type = type;
        this.title = title;
        this.link = link;
        this.shortInfo = shortInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShortInfo() {
        return shortInfo;
    }

    public void setShortInfo(String shortInfo) {
        this.shortInfo = shortInfo;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", shortInfo='" + shortInfo + '\'' +
                '}';
    }
}
