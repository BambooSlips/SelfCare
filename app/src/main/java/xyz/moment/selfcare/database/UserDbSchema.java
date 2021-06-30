package xyz.moment.selfcare.database;

public class UserDbSchema {
    public static final class UserTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String UUID = "_id";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String GENDER = "gender";
            public static final String BIRTHDAY = "birthday";
            public static final String HEIGHT = "height";
            public static final String WEIGHT = "weight";
        }
    }
}
