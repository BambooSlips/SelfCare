package xyz.moment.selfcare.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import xyz.moment.selfcare.model.User;


public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String uuidString = getString(getColumnIndex(UserDbSchema.UserTable.Cols.UUID));
        String username = getString(getColumnIndex(UserDbSchema.UserTable.Cols.USERNAME));
        String password = getString(getColumnIndex(UserDbSchema.UserTable.Cols.PASSWORD));
        String gender = getString(getColumnIndex(UserDbSchema.UserTable.Cols.GENDER));
        String birthday = getString(getColumnIndex(UserDbSchema.UserTable.Cols.BIRTHDAY));
        float height = getFloat(getColumnIndex(UserDbSchema.UserTable.Cols.HEIGHT));
        float weight = getFloat(getColumnIndex(UserDbSchema.UserTable.Cols.WEIGHT));

        return new User(uuidString,username,password,gender,birthday,height,weight);
    }
}
