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

import java.security.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        //?????????
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        //?????????????????????????????????
        for(String habitName : habitsNames) {
            lineDataSets.add(habitData2lineDataSet(habitName));
        }
        LineData lineData = new LineData(lineDataSets);
        lineChart.setData(lineData);
        //???????????????Y???
        lineChart.getAxisRight().setEnabled(false);
        //??????X???
        XAxis xAxis = lineChart.getXAxis();
        // ??????X????????????
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        //??????X??????????????????????????????
        xAxis.setGranularity(1f);
        //??????X???????????????????????????????????????true,??????????????????????????????????????????????????????????????????????????????????????????6???false???
        xAxis.setLabelCount(7, false);
        //??????X???????????????????????????????????????????????????????????????????????????????????????????????????
        Long createTime = new Date().getTime();
        //???????????????????????????????????????????????????????????????
        Long time = createTime - ((createTime + 28800000) % (86400000));
        //???????????????????????????????????????
        xAxis.setAxisMinimum(time-604800000);
        xAxis.setAxisMaximum(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d(TAG, "initData: "+formatter.format(time)+"--"+formatter.format(time-604800000));
        //X???????????????45???
        xAxis.setLabelRotationAngle(45);
        xAxis.setDrawGridLines(false);

        //Y???
        YAxis yAxis = lineChart.getAxisLeft();
        //???????????????15??????
        yAxis.setGranularity(900000);
        //yAxis.setLabelCount(24, false);
//        yAxis.setAxisMinimum(0);


        /*Long executedTime = executedDate.getTime();
        Long time = executedTime - ((executedTime + 28800000) % (86400000));
        Long X = time;
        //float Y = Float.parseFloat(executedDate.getHours()+"."+executedDate.getMinutes());
        float Y = executedTime - time - 28800000;*/
        Long standTime = createTime - time - 28800000;

        Long yMin = standTime - ((standTime + 28800000) % (86400000));
        Long yMax = yMin + 86399999;
        Log.d(TAG, "initData: yMax="+formatter.format(yMax)+"--"+formatter.format(yMin));
        //yAxis.setAxisMaximum(yMax);
        //yAxis.setAxisMaximum(yMin);


        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.DEFAULT);


        //??????X???????????????
        xAxis.setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue(float value)
            {
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
                return formatter.format(value);
            }
        });

        //??????Y???????????????
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                return formatter.format(value);
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
            //??????????????????????????????
            if(habit.getExecutedDate() != null) {
                Date executedDate = habit.getExecutedDate();
                Log.d(TAG, "habitData2lineDataSet: "+executedDate.toString());
                Log.d(TAG, "habitData2lineDataSet: "+executedDate.getMonth()+"."+executedDate.getDay());
                Log.d(TAG, "habitData2lineDataSet: "+executedDate.getHours()+"."+executedDate.getMinutes());
                Long executedTime = executedDate.getTime();
                Long time = executedTime - ((executedTime + 28800000) % (86400000));
                Long X = time;
                //float Y = Float.parseFloat(executedDate.getHours()+"."+executedDate.getMinutes());
                //????????????????????????1970?????????????????????Y???????????????
                float Y = executedTime - time - 28800000;
                /*if(maxDay < executedDate.getDate())
                    maxDay = executedDate.getDate();
                if(maxMonth < executedDate.getMonth()+1)
                    maxMonth = executedDate.getMonth()+1;
                Calendar ca = Calendar.getInstance();
                ca.setTime(executedDate);
                 */
                //Timestamp ts=new Timestamp(executedTime);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.d(TAG, "habitData2lineDataSet: (X="+X+",Y="+Y+")"+formatter.format(X)+"--"+formatter.format(Y));
                entries.add(new Entry(X, Y));
                i++;
            }
        }
        LineDataSet lineDataSet = new LineDataSet(entries, habitName);
        // ??????????????????
        //lineDataSet.setColor(Color.parseColor("#"+ (Math.random()+"").toString(16).slice(2,8));
        lineDataSet.setColor(getRandomColor());
        // ??????????????????
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                return formatter.format(value);
            }
        });
        return lineDataSet;
    }

    //??????????????????
    private static int getRandomColor(){
        Random random=new Random();
        int r=0;
        int g=0;
        int b=0;
        for(int i=0;i<2;i++){
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