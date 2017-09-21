package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/27/15.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.CategoryDataHolder> {
    private static String LOG_TAG = "RecyclerViewAdapter";
    private ArrayList<CategoryData> myDataset;

    public static class CategoryDataHolder extends RecyclerView.ViewHolder {

        TextView category;
        TextView count;


        public CategoryDataHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.category_text);
            count = (TextView) itemView.findViewById(R.id.categoryCount_text);

        }

    }

    public CategoryCardAdapter(ArrayList<CategoryData> myData) {
        this.myDataset = myData;
    }

    @Override
    public CategoryDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_cards, parent, false);

        CategoryDataHolder categoryDataHolder = new CategoryDataHolder(view);
        return categoryDataHolder;
    }

    @Override
    public void onBindViewHolder(CategoryDataHolder holder, int position) {
        holder.category.setText(myDataset.get(position).getmCatName());
        holder.count.setText(myDataset.get(position).getmCatCountStr());
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
