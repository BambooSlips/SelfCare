package xyz.moment.selfcare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xyz.moment.selfcare.model.Habit;


public class HabitLab {
    private static final String TAG = "HabitLab";
    private static HabitLab sHabitLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private HabitLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new HabitBaseHelper(mContext).getWritableDatabase();
    }

    public static HabitLab get(Context context) {
        if (sHabitLab == null) {
            sHabitLab = new HabitLab(context);
        }
        return sHabitLab;
    }

    private static ContentValues getContentValues(Habit habit) {
        ContentValues values = new ContentValues();
        values.put(HabitDbSchema.HabitTable.Cols.HID, habit.getHID());
        values.put(HabitDbSchema.HabitTable.Cols.HABITNAME, habit.getHabitName());
        if(habit.getExecutedDate() == null)
            values.put(HabitDbSchema.HabitTable.Cols.EXECUTEDDATE, "");
        else
            values.put(HabitDbSchema.HabitTable.Cols.EXECUTEDDATE, new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format(habit.getExecutedDate()));
        return values;
    }

    public void addHabit(Habit habit) {
        ContentValues values = getContentValues(habit);

        mDatabase.insert(HabitDbSchema.HabitTable.NAME, null, values);
    }

    public void updateHabit(Habit habit) {
        String hidString = habit.getHID();
        ContentValues values = getContentValues(habit);
        mDatabase.update(HabitDbSchema.HabitTable.NAME, values, HabitDbSchema.HabitTable.Cols.HID +
                " = ?", new String[]{hidString});
    }

    public HabitCursorWrapper queryHabits(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                HabitDbSchema.HabitTable.NAME, //select  [] from tableName
                null,  //select *
                whereClause,   // where
                whereArgs,     // condition
                null,
                null,
                null
        );

        //return cursor;
        return new HabitCursorWrapper(cursor);
    }

    //按习惯名分组查询习惯
    public HabitCursorWrapper queryHabitsNames() {
        Cursor cursor = mDatabase.query(
                HabitDbSchema.HabitTable.NAME,
                new String[]{HabitDbSchema.HabitTable.Cols.HABITNAME},
                null,
                null,
                HabitDbSchema.HabitTable.Cols.HABITNAME,
                null,
                null
        );

        return  new HabitCursorWrapper(cursor);
    }

    //获取全部习惯名
    public List<String> getHabitsNames() {
       List<String>  names = new ArrayList<>();
       HabitCursorWrapper cursorWrapper = queryHabitsNames();
       cursorWrapper.moveToFirst();
       while (!cursorWrapper.isAfterLast()) {
           Log.d(TAG, "getHabitsNames: cursorWrapper.getString(0)="+cursorWrapper.getString(0));
           names.add(cursorWrapper.getString(0));
           cursorWrapper.moveToNext();
       }
       cursorWrapper.close();
       return names;
    }


    public List<Habit> getHabits() {
        List<Habit> habits = new ArrayList<>();
        HabitCursorWrapper cursorWrapper = queryHabits(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                habits.add(cursorWrapper.getHabit());
                cursorWrapper.moveToNext();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursorWrapper.close();
        }

        return habits;
    }

    public Habit getHabit(String id) throws ParseException {
        HabitCursorWrapper cursorWrapper = queryHabits(HabitDbSchema.HabitTable.Cols.HID +
                " = ?", new String[]{id});

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getHabit();
        } finally {
            cursorWrapper.close();
        }
    }

    //
    public Habit getHabitByName(String habitName) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String thisMoment = ft.format(new Date( ));
        Log.d(TAG, "getHabitByName: thisMoment="+thisMoment);
        HabitCursorWrapper cursorWrapper = queryHabits(HabitDbSchema.HabitTable.Cols.HABITNAME +
                " = ?  and trim("+HabitDbSchema.HabitTable.Cols.EXECUTEDDATE+") = '' ", new String[]{habitName});

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            Log.d(TAG, "getHabitByName: cursorWrapper.getHabit().getHabitName()="+cursorWrapper.getHabit().getHabitName());
            return cursorWrapper.getHabit();
        } finally {
            cursorWrapper.close();
        }
    }

    public List<Habit> getHabitsListByName(String habitName) throws ParseException {
        List<Habit> myHabits = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String thisMoment = ft.format(new Date( ));
        Log.d(TAG, "getHabitByName: thisMoment="+thisMoment);
        HabitCursorWrapper cursorWrapper = queryHabits(HabitDbSchema.HabitTable.Cols.HABITNAME +
                " = ? ", new String[]{habitName});

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                myHabits.add(cursorWrapper.getHabit());
                cursorWrapper.moveToNext();
            }
            return myHabits;
        } finally {
            cursorWrapper.close();
        }
    }

    //判断今日习惯是否完成
    public boolean isHabitDone(String habitName, Date date) {
        String datetime = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format(date);
        HabitCursorWrapper cursorWrapper = queryHabits(HabitDbSchema.HabitTable.Cols.HABITNAME+" = ? and date("
        + HabitDbSchema.HabitTable.Cols.EXECUTEDDATE + ")  == date(?) and "+HabitDbSchema.HabitTable.Cols.EXECUTEDDATE
                +" IS NOT NULL ", new String[]{habitName, datetime});

        if (cursorWrapper.getCount() == 0) {
            return false;
        }
        cursorWrapper.moveToFirst();
        cursorWrapper.close();
        return true;
    }
}
