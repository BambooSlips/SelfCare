package xyz.moment.selfcare.model;

public class User {
    private String UID;
    private String username;
    private String password;
    private String gender;
    private String birthday;
    private float height;
    private float weight;

    public User(String UID, String username, String password, String gender, String birthday, float height, float weight) {
        this.UID = UID;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
    }

    public User(String username, String password, String gender, String birthday, float height, float weight) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
    }

    public User(String username, String password, String gender, String birthday) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
    }

    public User() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
