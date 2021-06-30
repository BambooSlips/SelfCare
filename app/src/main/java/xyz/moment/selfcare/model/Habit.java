package xyz.moment.selfcare.model;

import java.util.Date;

public class Habit {
    private String HID;
    private String habitName;
    private Date executedDate;

    public Habit() {
    }

    public Habit(String habitName) {
        this.habitName = habitName;
    }

    public Habit(String HID, String habitName, Date executedDate) {
        this.HID = HID;
        this.habitName = habitName;
        this.executedDate = executedDate;
    }

    public String getHID() {
        return HID;
    }

    public void setHID(String HID) {
        this.HID = HID;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public Date getExecutedDate() {
        return executedDate;
    }

    public void setExecutedDate(Date executedDate) {
        this.executedDate = executedDate;
    }
}
