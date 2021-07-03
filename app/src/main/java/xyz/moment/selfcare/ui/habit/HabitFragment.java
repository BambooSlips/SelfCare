package xyz.moment.selfcare.ui.habit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xyz.moment.selfcare.adapter.BaseAdapter;
import xyz.moment.selfcare.adapter.HabitAdapter;
import xyz.moment.selfcare.database.HabitLab;
import xyz.moment.selfcare.databinding.FragmentHabitBinding;
import xyz.moment.selfcare.model.Habit;

public class HabitFragment extends Fragment {

    private static final String TAG = "HabitFragment";
    private HabitViewModel habitViewModel;
    private FragmentHabitBinding binding;
    private HabitLab habitLab;

    //private Button btnAddHabit;
    private FloatingActionButton btnAddHabit;
    private RecyclerView rcvHabits;
    private HabitAdapter habitAdapter;

    private List<String> habitsNames = new ArrayList<>();
    private List<Habit> habitList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);

        binding = FragmentHabitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        habitLab = HabitLab.get(getContext());

        try {
            initData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initData() throws ParseException {
        //习惯
        habitList.clear();
        habitsNames = habitLab.getHabitsNames();
        for(String habitName : habitsNames) {
            Log.d(TAG, "initData: habitName="+habitName);

            if (habitLab.isHabitDone(habitName, new Date())) {         //今日习惯已完成
                Log.d(TAG, "initData: "+habitName+" is done!!!");
            }
            else {                                                     //今日习惯未完成
                createHabits(habitName);
            }
        }

        for(String habitName : habitsNames) {
            if(habitLab.getHabitByName(habitName) == null)
                continue;
            habitList.add(habitLab.getHabitByName(habitName));
            //habitList=habitLab.getHabitsListByName(habitName);
            Log.d(TAG, "initData: habitLab.getHabitByName(habitName).getHabitName()+habitLab.getHabitByName(habitName).getHID()=" +
                    ""+habitLab.getHabitByName(habitName).getHabitName()+habitLab.getHabitByName(habitName).getHID());
        }
    }

    public void initView() {
       btnAddHabit = binding.btnAddHabit;
       rcvHabits = binding.rcvHabits;

       habitAdapter = new HabitAdapter(getContext());
       Log.d(TAG, "initView: habitList.size()="+habitList.size());
       habitAdapter.addAll(habitList);
       rcvHabits.setAdapter(habitAdapter);
       rcvHabits.setLayoutManager(new LinearLayoutManager(getContext()));

       btnAddHabit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showInputDialog(getContext(), "添加");

           }
       });

       habitAdapter.setItemClickListener(new BaseAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(Object o, int position) {

           }
       });

    }

    public Habit createHabits(String habitName) throws ParseException {
        Habit habit = null;
        //若数据库中已有同名习惯，则获取并返回它，否则创建新的同名习惯
        habit = habitLab.getHabitByName(habitName);
        if(habit == null) {
            habit = new Habit(habitName);
            habitLab.addHabit(habit);
        }
        return habit;
    }

    private void showInputDialog(Context context, String title) {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(context);

        AlertDialog.Builder inputDialog = new AlertDialog.Builder(context);
        inputDialog.setTitle(title).setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("".equals(editText.getText().toString()))
                            return;
                        Habit habit = new Habit(editText.getText().toString());
                        //在数据库中新建新习惯
                        habitLab.addHabit(habit);
                        try {
                            habit = habitLab.getHabitByName(habit.getHabitName());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //为RecyclerView添加新习惯
                        habitAdapter.add(habit);
                        rcvHabits.setAdapter(habitAdapter);
                        rcvHabits.setLayoutManager(new LinearLayoutManager(getContext()));
                        Toast.makeText(context, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ).show();
    }
}