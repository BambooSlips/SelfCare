package xyz.moment.selfcare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "selfCareBase_db";

    public UserBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //首次创建数据库将调用此方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ UserDbSchema.UserTable.NAME + "(" + UserDbSchema.UserTable.Cols.UUID
                + "  integer primary key, " + UserDbSchema.UserTable.Cols.USERNAME+", "
                + UserDbSchema.UserTable.Cols.PASSWORD+", " + UserDbSchema.UserTable.Cols.GENDER
                +", "+ UserDbSchema.UserTable.Cols.BIRTHDAY+", "+UserDbSchema.UserTable.Cols.HEIGHT
                +", "+UserDbSchema.UserTable.Cols.WEIGHT+ ")");

        db.execSQL("create table "+ HabitDbSchema.HabitTable.NAME + "(" + HabitDbSchema.HabitTable.Cols.HID
                + "  integer primary key, " + HabitDbSchema.HabitTable.Cols.HABITNAME+", "
                + HabitDbSchema.HabitTable.Cols.EXECUTEDDATE+ ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
