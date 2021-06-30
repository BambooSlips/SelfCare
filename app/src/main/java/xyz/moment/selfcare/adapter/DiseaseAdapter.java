package xyz.moment.selfcare.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import xyz.moment.selfcare.model.SearchResult;
import xyz.moment.selfcare.R;

public class DiseaseAdapter extends BaseAdapter<SearchResult, DiseaseAdapter.DiseaseViewHodler>{

    public DiseaseAdapter(Context context) {
        super(context);
    }
    @Override
    protected int onBindLayout() {
        return R.layout.disease_item;
    }

    @Override
    protected DiseaseViewHodler onCreateHolder(View view) {
        return new DiseaseViewHodler(view);
    }

    @Override
    protected void onBindData(DiseaseViewHodler holder, SearchResult searchResult, int positon) {
        holder.tvDiseaseItemName.setText(searchResult.getTitle());
        holder.tvShortInfo.setText(searchResult.getShortInfo());
    }


    static class DiseaseViewHodler extends RecyclerView.ViewHolder {

        //ImageView ivSongImage;
        TextView tvDiseaseItemName;
        TextView tvShortInfo;

        public DiseaseViewHodler(@NonNull View itemView) {
            super(itemView);
            tvDiseaseItemName = itemView.findViewById(R.id.tvDiseaseItemName);
            tvShortInfo = itemView.findViewById(R.id.tvShortInfo);
        }
    }
}
