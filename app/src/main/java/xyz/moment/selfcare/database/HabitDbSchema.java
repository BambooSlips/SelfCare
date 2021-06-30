package xyz.moment.selfcare.database;

public class HabitDbSchema {
    public static final class HabitTable {
        public static final String NAME = "habits";

        public static final class Cols {
            public static final String HID = "_id";
            public static final String HABITNAME = "habitname";
            public static final String EXECUTEDDATE = "executedDate";
        }
    }
}
