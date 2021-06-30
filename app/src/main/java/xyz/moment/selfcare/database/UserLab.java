package xyz.moment.selfcare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import xyz.moment.selfcare.model.User;


public class UserLab {
    private static final String TAG = "UserLab";
    private static UserLab sUserLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private UserLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new UserBaseHelper(mContext).getWritableDatabase();
    }

    public static UserLab get(Context context) {
        if (sUserLab == null) {
            sUserLab = new UserLab(context);
        }
        return sUserLab;
    }

    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserDbSchema.UserTable.Cols.UUID, user.getUID());
        values.put(UserDbSchema.UserTable.Cols.USERNAME, user.getUsername());
        values.put(UserDbSchema.UserTable.Cols.PASSWORD, user.getPassword());
        values.put(UserDbSchema.UserTable.Cols.GENDER, user.getGender());
        values.put(UserDbSchema.UserTable.Cols.BIRTHDAY, user.getBirthday());
        values.put(UserDbSchema.UserTable.Cols.HEIGHT, user.getHeight());
        values.put(UserDbSchema.UserTable.Cols.WEIGHT, user.getWeight());

        return values;
    }

    public void addUser(User user) {
        ContentValues values = getContentValues(user);

        mDatabase.insert(UserDbSchema.UserTable.NAME, null, values);
    }

    public void updateUser(User user) {
        String uuidString = user.getUID().toString();
        ContentValues values = getContentValues(user);
        mDatabase.update(UserDbSchema.UserTable.NAME, values, UserDbSchema.UserTable.Cols.UUID +
                " = ?", new String[]{uuidString});
    }

    public UserCursorWrapper queryUsers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                UserDbSchema.UserTable.NAME, //select  [] from tableName
                null,  //select *
                whereClause,   // where
                whereArgs,     // condition
                null,
                null,
                null
        );

        //return cursor;
        return new UserCursorWrapper(cursor);
    }


    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        UserCursorWrapper cursorWrapper = queryUsers(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                users.add(cursorWrapper.getUser());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return users;
    }

    public User getUser(String id) {
        UserCursorWrapper cursorWrapper = queryUsers(UserDbSchema.UserTable.Cols.UUID +
                " = ?", new String[]{id});

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getUser();
        } finally {
            cursorWrapper.close();
        }
    }

    public User getUser(String username, String password) {
        UserCursorWrapper cursorWrapper = queryUsers(UserDbSchema.UserTable.Cols.USERNAME +
                " = ? and " + UserDbSchema.UserTable.Cols.PASSWORD + " = ? ", new String[]{username, password});

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getUser();
        } finally {
            cursorWrapper.close();
        }
    }

    public boolean isAUser(String username, String password) {
        UserCursorWrapper cursorWrapper = queryUsers(UserDbSchema.UserTable.Cols.USERNAME +
                " = ? and " + UserDbSchema.UserTable.Cols.PASSWORD + " = ? ", new String[]{username, password});
        //Log.d(TAG, "isAUser: ");
        try {
            if (cursorWrapper.getCount() == 0) {
                return false;
            }
            cursorWrapper.moveToFirst();
            return true;
        } finally {
            cursorWrapper.close();
        }
    }
}
