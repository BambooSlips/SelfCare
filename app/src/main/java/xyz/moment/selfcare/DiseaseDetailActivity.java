package xyz.moment.selfcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import xyz.moment.selfcare.adapter.SectionAdapter;
import xyz.moment.selfcare.model.Article;
import xyz.moment.selfcare.utils.GetArticle;

public class DiseaseDetailActivity extends AppCompatActivity {

    private static final String TAG = "DiseaseDetailActivity";
    private int shortAnimationDuration;

    private Article article = new Article();
    private String url = null;

    private SectionAdapter sectionAdapter;
    private TextView tvArticleTitle;
    private TextView tvAuthor;
    private TextView tvTopicExplanation;
    private TextView tvTopicItems;
    private RecyclerView sectionRecyclerView;
    private CardView cvFirst;
    private CardView cvSecond;
    private CardView cvThird;
    private ImageButton ibArrow;
    private LinearLayout llExplanation;
    private LinearLayout llTop;

    private boolean isExpanded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏导航栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_disease_detail);

        initView();
        initData();
    }

    public void initView() {
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        tvArticleTitle = findViewById(R.id.tvArticleTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvTopicExplanation = findViewById(R.id.tvTopicExplanation);
        tvTopicItems = findViewById(R.id.tvTopicItems);
        sectionRecyclerView = findViewById(R.id.sectionRecyclerView);
        cvFirst = findViewById(R.id.cvFirst);
        ibArrow = findViewById(R.id.ibArrow);
        llExplanation = findViewById(R.id.llExplanation);
        llTop = findViewById(R.id.llTop);

        sectionRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_item_right_slipe));
        sectionAdapter = new SectionAdapter(DiseaseDetailActivity.this);

        llTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpanded) {
                    setViewsVisibility(View.GONE);
                    ibArrow.setImageResource(R.drawable.expand_arrow);
                    isExpanded = false;
                }
                else {
                    setViewsVisibility(View.VISIBLE);
                    ibArrow.setImageResource(R.drawable.collapse_arrow);
                    isExpanded = true;
                }
            }
        });
//        sectionRecyclerView.setLis
        /*sectionRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                LinearLayoutManager layoutManager = (LinearLayoutManager) sectionRecyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                //当前RecyclerView的所有子项个数
                int totalItemCount = layoutManager.getItemCount();

                if(firstVisibleItemPosition == 0) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setCardViewsVisibility(View.VISIBLE);
                    Log.d(TAG, "onTouch: VISIBLE");
                }
                else {

                    setCardViewsVisibility(View.GONE);
                    Log.d(TAG, "onTouch: GONE");
                }

                Log.d(TAG, "onTouch: firstVisibleItemPosition="+firstVisibleItemPosition);
                
                *//*if(sectionRecyclerView.canScrollVertically(-1) && !sectionRecyclerView.canScrollVertically(1)) {           //如果能再向下滚动 不能向上
                    Log.d(TAG, "onTouch: before="+cvFirst.getVisibility());
                    setCardViewsVisibility(View.VISIBLE);
                    Log.d(TAG, "onTouch: VISIBLE");
                }

                if(sectionRecyclerView.canScrollVertically(-1) && sectionRecyclerView.canScrollVertically(1)) {           //如果能再向下滚动 能向上
                    setCardViewsVisibility(View.GONE);
                    Log.d(TAG, "onTouch: GONE");
                }

                if(!sectionRecyclerView.canScrollVertically(-1) && sectionRecyclerView.canScrollVertically(1)) {           //如果不能再向下滚动 能向上
                    setCardViewsVisibility(View.GONE);
                    Log.d(TAG, "onTouch: GONE");
                }*//*

                return false;
            }
        });*/
        //sectionRecyclerView.getScrollState()

        sectionRecyclerView.scheduleLayoutAnimation();
    }

    public void setViewsVisibility(int viewsVisibility) {
        float a;
        if(viewsVisibility == View.GONE)
            a = 0f;
        else
            a = 1f;
        llExplanation.animate()
                .alpha(a)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        llExplanation.setVisibility(viewsVisibility);
                    }
                });
    }

    public void initData() {
        url = getIntent().getStringExtra("diseaseLink");
        Log.d(TAG, "initData: url=" + url);
        new Thread(networkTask).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            tvArticleTitle.setText(article.getTitle());                  //标题
            tvAuthor.setText(article.getAuthor());                       //作者
            tvTopicExplanation.setText(article.getTopicExplanation());   //主题解释
            //tvTopicItems.setText(article.getTopicItem().toString());
            String topicItem = "";
            for(String item : article.getTopicItem()) {                  //概要
                item = "&#8226; "+  item + "<br>";
                topicItem += Html.fromHtml(item);
            }
            tvTopicItems.setText(topicItem);
            sectionAdapter.addAll(article.getSections());                //小节
            sectionRecyclerView.setAdapter(sectionAdapter);
            sectionRecyclerView.setLayoutManager(new LinearLayoutManager(DiseaseDetailActivity.this));
        }
    };

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            article = GetArticle.get(url);
            Log.d(TAG, "run: "+article.getTitle());
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "got the article!");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
}