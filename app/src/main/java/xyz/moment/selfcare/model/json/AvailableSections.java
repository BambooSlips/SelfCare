package xyz.moment.selfcare.model.json;

public class AvailableSections
{
    private String vasontId;

    private String title;

    private boolean excludedByDefault;

    private boolean isChecked;

    private int aggregateCount;

    private boolean isTopSection;

    public void setVasontId(String vasontId){
        this.vasontId = vasontId;
    }
    public String getVasontId(){
        return this.vasontId;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setExcludedByDefault(boolean excludedByDefault){
        this.excludedByDefault = excludedByDefault;
    }
    public boolean getExcludedByDefault(){
        return this.excludedByDefault;
    }
    public void setIsChecked(boolean isChecked){
        this.isChecked = isChecked;
    }
    public boolean getIsChecked(){
        return this.isChecked;
    }
    public void setAggregateCount(int aggregateCount){
        this.aggregateCount = aggregateCount;
    }
    public int getAggregateCount(){
        return this.aggregateCount;
    }
    public void setIsTopSection(boolean isTopSection){
        this.isTopSection = isTopSection;
    }
    public boolean getIsTopSection(){
        return this.isTopSection;
    }
}
