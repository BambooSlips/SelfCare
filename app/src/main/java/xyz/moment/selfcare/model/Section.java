package xyz.moment.selfcare.model;

import android.text.Html;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private String title;
//    private List<String> detail;
    private Detail detail;

    public Section() {
    }

    public Section(String title, Detail detail) {
        this.title = title;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Section{" +
                "title='" + title + '\'' +
                ", detail=" + detail +
                '}';
    }
}
