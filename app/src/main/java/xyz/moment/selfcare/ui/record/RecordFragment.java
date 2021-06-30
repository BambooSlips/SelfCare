package xyz.moment.selfcare.ui.record;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import xyz.moment.selfcare.R;
import xyz.moment.selfcare.database.HabitLab;
import xyz.moment.selfcare.databinding.FragmentRecordBinding;
import xyz.moment.selfcare.model.Habit;

public class RecordFragment extends Fragment {
    private static final String TAG = "RecordFragment";
    
    private RecordViewModel recordViewModel;
    private FragmentRecordBinding binding;
    private LineChart lineChart;

    private HabitLab habitLab;
    private int count, maxDay= 0, maxMonth = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initView();
        try {
            initData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initView() {
        lineChart = binding.lineChart;
    }

    public void initData() throws ParseException {
        habitLab = HabitLab.get(getContext());
        List<String> habitsNames = habitLab.getHabitsNames();
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        for(String habitName : habitsNames) {
            lineDataSets.add(habitData2lineDataSet(habitName));
        }
        LineData lineData = new LineData(lineDataSets);
        lineChart.setData(lineData);
        lineChart.getAxisRight().setEnabled(false);
        //上面右边效果图的部分代码，设置X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        //设置X轴坐标之间的最小间隔
        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
        xAxis.setLabelCount(7, false);
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.setAxisMinimum(maxDay-7);
        xAxis.setAxisMaximum((float) maxDay);
        xAxis.setLabelRotationAngle(45);
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.DEFAULT);


        xAxis.setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue(float value)
            {
                String label = "";
                if(value-7 < 1) {
                    //label = "";
                }
                label = maxMonth +"月"+(int)value+"日";
                return label;
            }
        });

        lineChart.invalidate();
    }

    public LineDataSet habitData2lineDataSet(String habitName) throws ParseException {
        List<Entry> entries = new ArrayList<>();
        List<Habit> myHabits = habitLab.getHabitsListByName(habitName);
        Log.d(TAG, "initData: myHabits.size()="+myHabits.size());
        int i = 0;
        for(Habit habit: myHabits) {
            if(habit.getExecutedDate() != null) {
                Date executedDate = habit.getExecutedDate();
                Log.d(TAG, "habitData2lineDataSet: "+executedDate.toString());
                Log.d(TAG, "habitData2lineDataSet: "+executedDate.getMonth()+"."+executedDate.getDay());
                Log.d(TAG, "habitData2lineDataSet: "+executedDate.getHours()+"."+executedDate.getMinutes());
                int X = executedDate.getDate();
                float Y = Float.parseFloat(executedDate.getHours()+"."+executedDate.getMinutes());
                if(maxDay < executedDate.getDate())
                    maxDay = executedDate.getDate();
                if(maxMonth < executedDate.getMonth()+1)
                    maxMonth = executedDate.getMonth()+1;
                Calendar ca = Calendar.getInstance();
                ca.setTime(executedDate);
                Log.d(TAG, "habitData2lineDataSet: (X="+X+",Y="+Y+")");
                entries.add(new Entry(X, Y));
                i++;
            }
        }
        count = i;
        LineDataSet lineDataSet = new LineDataSet(entries, habitName);
        // 设置曲线颜色
        //lineDataSet.setColor(Color.parseColor("#"+ (Math.random()+"").toString(16).slice(2,8));
        lineDataSet.setColor(getRandomColor());
        // 设置平滑曲线
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return lineDataSet;
    }

    private static int getRandomColor(){
        Random random=new Random();
        int r=0;
        int g=0;
        int b=0;
        for(int i=0;i<2;i++){
            //       result=result*10+random.nextInt(10);
            int temp=random.nextInt(16);
            r=r*16+temp;
            temp=random.nextInt(16);
            g=g*16+temp;
            temp=random.nextInt(16);
            b=b*16+temp;
        }
        return Color.rgb(r,g,b);
    }

}