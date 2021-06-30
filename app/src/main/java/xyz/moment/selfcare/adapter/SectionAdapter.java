package xyz.moment.selfcare.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.moment.selfcare.R;
import xyz.moment.selfcare.model.Section;

public class SectionAdapter extends BaseAdapter<Section, SectionAdapter.SectionViewHodler>{

    private static final String TAG = "SectionAdapter";

    public SectionAdapter(Context context) {
        super(context);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.section_item;
    }

    @Override
    protected SectionViewHodler onCreateHolder(View view) {
        return new SectionViewHodler(view);
    }

    @Override
    protected void onBindData(SectionViewHodler holder, Section section, int position) {
        holder.tvSectionTitle.setText(section.getTitle());
        String item = "";
        holder.tvDetailTitle.setText(section.getDetail().getdTitle());
        for(String s : section.getDetail().getItems()) {
            item += s;
        }
        holder.tvSectionDetail.setText(Html.fromHtml(item));
    }

    static class SectionViewHodler extends RecyclerView.ViewHolder {
        //ImageView ivSongImage;
        TextView tvSectionTitle;
        TextView tvDetailTitle;
        TextView tvSectionDetail;
        LinearLayout llSectionExplanation;
        LinearLayout llSectionTitle;
        ImageButton ibSectionArrow;
        int shortAnimationDuration = 200;
        boolean isExpanded = true;

        public SectionViewHodler(@NonNull View itemView) {
            super(itemView);
            tvSectionTitle = itemView.findViewById(R.id.tvSectionTitle);
            tvDetailTitle = itemView.findViewById(R.id.tvDetailTitle);
            tvSectionDetail = itemView.findViewById(R.id.tvSectionDetail);
            llSectionExplanation = itemView.findViewById(R.id.llSectionExplanation);
            llSectionTitle = itemView.findViewById(R.id.llSectionTitle);
            ibSectionArrow = itemView.findViewById(R.id.ibSectionArrow);

            llSectionTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isExpanded) {
                        setViewsVisibility(llSectionExplanation, View.GONE);
                        ibSectionArrow.setImageResource(R.drawable.expand_arrow);
                        isExpanded = false;
                    }
                    else {
                        setViewsVisibility(llSectionExplanation, View.VISIBLE);
                        ibSectionArrow.setImageResource(R.drawable.collapse_arrow);
                        isExpanded = true;
                    }
                }
            });
        }

        public void setViewsVisibility(LinearLayout linearLayout,int viewsVisibility) {
            float a;
            if(viewsVisibility == View.GONE)
                a = 0f;
            else
                a = 1f;
            linearLayout.animate()
                    .alpha(a)
                    .setDuration(shortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            linearLayout.setVisibility(viewsVisibility);
                        }
                    });
        }
    }
}
