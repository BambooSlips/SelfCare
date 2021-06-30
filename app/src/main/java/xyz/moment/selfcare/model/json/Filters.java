package xyz.moment.selfcare.model.json;

public class Filters
{
    private String key;

    private int totalhit;

    private String displayName;

    private boolean isChecked;

    private boolean isDisabled;

    public void setKey(String key){
        this.key = key;
    }
    public String getKey(){
        return this.key;
    }
    public void setTotalhit(int totalhit){
        this.totalhit = totalhit;
    }
    public int getTotalhit(){
        return this.totalhit;
    }
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return this.displayName;
    }
    public void setIsChecked(boolean isChecked){
        this.isChecked = isChecked;
    }
    public boolean getIsChecked(){
        return this.isChecked;
    }
    public void setIsDisabled(boolean isDisabled){
        this.isDisabled = isDisabled;
    }
    public boolean getIsDisabled(){
        return this.isDisabled;
    }
}

