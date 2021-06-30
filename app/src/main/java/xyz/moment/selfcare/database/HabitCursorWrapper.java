package xyz.moment.selfcare.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.moment.selfcare.model.Habit;

public class HabitCursorWrapper extends CursorWrapper {
    private static final String TAG = "HabitCursorWrapper";
    public HabitCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Habit getHabit() throws ParseException {
        String HID = getString(getColumnIndex(HabitDbSchema.HabitTable.Cols.HID));
        String habitName = getString(getColumnIndex(HabitDbSchema.HabitTable.Cols.HABITNAME));
        Date executedDate = null;
        if("".equals(getString(getColumnIndex(HabitDbSchema.HabitTable.Cols.EXECUTEDDATE))))
            executedDate = null;
        else {
            Log.d(TAG, "getHabit: getString(getColumnIndex(HabitDbSchema.HabitTable.Cols.EXECUTEDDATE))="
                    +getString(getColumnIndex(HabitDbSchema.HabitTable.Cols.EXECUTEDDATE)));
            executedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getString(getColumnIndex(HabitDbSchema.HabitTable.Cols.EXECUTEDDATE)));
            Log.d(TAG, "getHabit: "+executedDate.toString());
            Log.d(TAG, "getHabit: "+executedDate.getMonth());
            Log.d(TAG, "getHabit: "+executedDate.getDate());
            Log.d(TAG, "getHabit: "+executedDate.getHours());
            Log.d(TAG, "getHabit: "+executedDate.getMinutes());
        }

        return new Habit(HID, habitName, executedDate);
    }
}
