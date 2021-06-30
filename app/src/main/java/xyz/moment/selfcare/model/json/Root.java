package xyz.moment.selfcare.model.json;

import java.util.ArrayList;
import java.util.List;
public class Root
{
    private String query;

    private int totalSearchResults;

    private List<Results> results;

    private List<Filters> filters;

    private List<AvailableSections> availableSections;

    public void setQuery(String query){
        this.query = query;
    }
    public String getQuery(){
        return this.query;
    }
    public void setTotalSearchResults(int totalSearchResults){
        this.totalSearchResults = totalSearchResults;
    }
    public int getTotalSearchResults(){
        return this.totalSearchResults;
    }
    public void setResults(List<Results> results){
        this.results = results;
    }
    public List<Results> getResults(){
        return this.results;
    }
    public void setFilters(List<Filters> filters){
        this.filters = filters;
    }
    public List<Filters> getFilters(){
        return this.filters;
    }
    public void setAvailableSections(List<AvailableSections> availableSections){
        this.availableSections = availableSections;
    }
    public List<AvailableSections> getAvailableSections(){
        return this.availableSections;
    }
}
