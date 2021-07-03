package xyz.moment.selfcare.ui.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tbruyelle.rxpermissions3.RxPermissions;

import org.json.JSONException;

import java.util.List;

import xyz.moment.selfcare.DiseaseDetailActivity;
import xyz.moment.selfcare.adapter.DiseaseAdapter;
import xyz.moment.selfcare.databinding.FragmentHomeBinding;
import xyz.moment.selfcare.model.SearchResult;
import xyz.moment.selfcare.utils.GetSearchResult;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TextView etSymptom;
    private Button btnSearch;
    private RecyclerView mRecyclerView;

    private List<SearchResult> resultList;
    private DiseaseAdapter diseaseAdapter;
//    private Context mContext;
    private int mPosition = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        initData();
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //申请网络权限
    public void initData() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.INTERNET).subscribe(
                granted -> {
                    if (granted) {
                        // All requested permissions are granted

                    } else {
                        // At least one permission is denied
                        //Toast.makeText(HomeFragment.this, "无法访问网络!",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "initData: 无法访问网络!");
                    }
                }
        );
    }

    public void initView() {
        etSymptom = binding.etSymptom;
        btnSearch = binding.btnSearch;
        //tvDiseaseName = binding.tvDiseaseName;
        mRecyclerView = binding.mRecyclerView;

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(networkTask).start();
            }
        });

        //init adapter
        diseaseAdapter = new DiseaseAdapter(getContext());
        //点击疾病
        diseaseAdapter.setItemClickListener((object, position) -> {
            mPosition = position;
            //Log.d(TAG, "initView: resultList.get(mPosition).getTitle()="+resultList.get(mPosition).getTitle());
            Intent intent = new Intent(getContext(), DiseaseDetailActivity.class);
            intent.putExtra("diseaseLink", resultList.get(mPosition).getLink());
            startActivity(intent);
        });

    }

    public List<SearchResult> searchSymptom() throws JSONException {

        return GetSearchResult.search(etSymptom.getText().toString());
    }

    /*public void searchSymptom() throws JSONException {
        GetSearchResult.search(etSymptom.getText().toString());
    }*/

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.d(TAG, "handleMessage: val="+val);
            //tvDiseaseName.setText(resultList.get(0).getTitle());

            diseaseAdapter.clear();
            diseaseAdapter.addAll(resultList);
            mRecyclerView.setAdapter(diseaseAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            // 需要重新启动布局时调用此方法
            mRecyclerView.scheduleLayoutAnimation();
        }
    };

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            try {
                resultList = searchSymptom();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "search completed!");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
}