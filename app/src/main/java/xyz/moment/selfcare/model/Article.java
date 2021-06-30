package xyz.moment.selfcare.model;

import java.util.List;

public class Article {
    private String title;
    private String author;
    private String topicExplanation;
    private List<String> topicItem;
    private List<Section> sections;

    public Article() {
    }

    public Article(String title, String author, String topicExplanation, List<String> topicItem, List<Section> sections) {
        this.title = title;
        this.author = author;
        this.topicExplanation = topicExplanation;
        this.topicItem = topicItem;
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", topicExplanation='" + topicExplanation + '\'' +
                ", topicItem=" + topicItem +
                ", sections=" + sections +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTopicExplanation() {
        return topicExplanation;
    }

    public void setTopicExplanation(String topicExplanation) {
        this.topicExplanation = topicExplanation;
    }

    public List<String> getTopicItem() {
        return topicItem;
    }

    public void setTopicItem(List<String> topicItem) {
        this.topicItem = topicItem;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}

