package xyz.moment.selfcare.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.moment.selfcare.database.HabitLab;
import xyz.moment.selfcare.model.Habit;
import xyz.moment.selfcare.R;

public class HabitAdapter extends BaseAdapter<Habit, HabitAdapter.HabitViewHolder> {
    private static final String TAG = "HabitAdapter";
    public HabitAdapter(Context context) {
        super(context);
    }
    @Override
    protected int onBindLayout() {
        return R.layout.habit_item;
    }

    @Override
    protected HabitViewHolder onCreateHolder(View view) {
        return new HabitViewHolder(view);
    }

    @Override
    protected void onBindData(HabitViewHolder holder, Habit habit, int positon) {
        if(habit == null)
            return;
        holder.tvHabitName.setText(habit.getHabitName());
        if(habit.getExecutedDate() != null && habit.getExecutedDate().before(new Date())) {
            holder.cbHabitDone.setChecked(true);
        }
        else
            holder.cbHabitDone.setChecked(false);

        holder.cbHabitDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habit.setExecutedDate(new Date());
                HabitLab habitLab = HabitLab.get(v.getContext());
                habitLab.updateHabit(habit);
                holder.cbHabitDone.setChecked(true);
                Log.d(TAG, "onClick: "+habit.getHabitName()+"--"+habit.getHID());
            }
        });
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView tvHabitName;
        CheckBox cbHabitDone;
        public HabitViewHolder(View itemView) {
            super(itemView);
            tvHabitName = itemView.findViewById(R.id.tvHabitName);
            cbHabitDone = itemView.findViewById(R.id.cbHabitDone);
        }
    }
}
