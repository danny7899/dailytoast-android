package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 12/18/15.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "RecyclerViewAdapter";
    private ArrayList<DataObject> myDataset;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView author;
        TextView time;
        TextView description;
        TextView category;
        TextView views;
        TextView viewsDesc;
        TextView comment;
        TextView commentDesc;
        TextView postID;


        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text);
            author = (TextView) itemView.findViewById(R.id.author_text);
            time = (TextView) itemView.findViewById(R.id.date_text);
            description = (TextView) itemView.findViewById(R.id.content_text);
            category = (TextView) itemView.findViewById(R.id.category_text);
            views = (TextView) itemView.findViewById(R.id.viewCount_text);
            viewsDesc = (TextView) itemView.findViewById(R.id.viewDesc_text);
            comment = (TextView) itemView.findViewById(R.id.commentCount_text);
            commentDesc = (TextView) itemView.findViewById(R.id.commentCountDesc_text);
            postID = (TextView) itemView.findViewById(R.id.postID);

        }

    }

    public RecyclerViewAdapter(ArrayList<DataObject> myData) {
        this.myDataset = myData;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_cards, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.title.setText(myDataset.get(position).getmTitle());
        holder.author.setText(myDataset.get(position).getmAuthor());
        holder.time.setText(myDataset.get(position).getmTime());
        holder.description.setText(myDataset.get(position).getmPostDesc());
        holder.category.setText(myDataset.get(position).getmCategory());
        holder.views.setText(myDataset.get(position).getmViewsStr());
        holder.viewsDesc.setText(myDataset.get(position).getmViewsDesc());
        holder.comment.setText(myDataset.get(position).getmCommentsStr());
        holder.commentDesc.setText(myDataset.get(position).getmCommentsDesc());
        holder.postID.setText(myDataset.get(position).getmID());
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
