package xyz.moment.selfcare.model;

import java.util.ArrayList;
import java.util.List;

public class Detail {
    private String dTitle;
    private List<String> items = new ArrayList<>();

    public Detail() {}

    public Detail(String dTitle, List<String> items) {
        this.dTitle = dTitle;
        this.items = items;
    }

    public String getdTitle() {
        return dTitle;
    }

    public void setdTitle(String dTitle) {
        this.dTitle = dTitle;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "dTitle='" + dTitle + '\'' +
                ", items=" + items +
                '}';
    }
}
